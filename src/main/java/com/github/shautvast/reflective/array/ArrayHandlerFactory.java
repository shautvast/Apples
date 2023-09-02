package com.github.shautvast.reflective.array;

import com.github.shautvast.reflective.array.base.ArrayCreator;
import com.github.shautvast.reflective.array.base.ArraySetter;
import com.github.shautvast.reflective.array.base.ObjectArraySetter;
import com.github.shautvast.reflective.java.ASM;
import com.github.shautvast.reflective.java.ByteClassLoader;
import com.github.shautvast.reflective.java.Java;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.objectweb.asm.Opcodes.*;

class ArrayHandlerFactory {
    /* cache for the compiled creator classes */
    private static final Map<String, ArrayCreator> creatorCache = new ConcurrentHashMap<>();

    /* Cache for the compiled setter classes.
     *  The outer Map contains the ArraySetter type (some primitive or Object Setter)
     *  That maps to the concrete calculated ArraySetter instance name which maps to the instance itself.
     *  TODO see if this can be optimized
     */
    private static final Map<Class<? extends ArraySetter>, Map<String, Object>> setterCache = new ConcurrentHashMap<>();


    /*
     * generic method for creating array setters (primitives and objects)
     */
    @SuppressWarnings("unchecked")
    static <T extends ArraySetter> T getSetterInstance(Class<T> setterBaseType, Class<?> arrayType) {
        String arrayTypeName = Java.internalName(arrayType);
        String syntheticClassName = "com/shautvast/reflective/array/ArraySetter_"
                + javaName(arrayTypeName) + Java.getNumDimensions(arrayType);

        return (T) setterCache.computeIfAbsent(setterBaseType, k -> new ConcurrentHashMap<>()).
                computeIfAbsent(syntheticClassName,
                        k -> ArrayHandlerFactory.createSyntheticArraySetter(setterBaseType, arrayTypeName, syntheticClassName));
    }

    /*  creates an instance of an ArrayCreator of the specified type */
    static ArrayCreator getCreatorInstance(Class<?> elementType, int... dimensions) {
        String elementTypeName = Java.internalName(elementType);
        String syntheticClassName = "com/shautvast/reflective/array/ArrayCreator_"
                + javaName(elementTypeName) + dimensions.length;

        return creatorCache.computeIfAbsent(syntheticClassName,
                k -> ArrayHandlerFactory.createSyntheticArrayCreator(elementTypeName, syntheticClassName, dimensions));
    }

    /* strips all disallowed characters from a classname */
    private static String javaName(String arrayTypeName) {
        return arrayTypeName
                .replaceAll("[/.\\[;]", "")
                .toLowerCase();
    }

    /* Creates the ASM ClassNode for an ArrayCreator */
    static ArrayCreator createSyntheticArrayCreator(String elementType, String name, int... dimensions) {
        ClassNode classNode = ASM.createDefaultClassNode(name, Java.internalName(ArrayCreator.class));
        classNode.methods.add(createNewInstanceMethodNode(elementType, dimensions));
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classNode.accept(classWriter);

        byte[] byteArray = classWriter.toByteArray();

//        try (FileOutputStream out = new FileOutputStream(name + ".class")) {
//            out.write(byteArray);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        ByteClassLoader.INSTANCE.addClass(classNode.name, byteArray);

        try {
            return (ArrayCreator) (ByteClassLoader.INSTANCE.loadClass(classNode.name).getConstructor().newInstance());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /* Creates the ASM ClassNode for an ArraySetter */
    static <T> T createSyntheticArraySetter(Class<T> setterType, String arrayType, String name) {
        ClassNode classNode = ASM.createDefaultClassNode(name, Java.internalName(setterType));
        classNode.methods.add(createSetMethodNode(setterType, arrayType));
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classNode.accept(classWriter);

        byte[] byteArray = classWriter.toByteArray();

        try (FileOutputStream out = new FileOutputStream("A.class")) {
            out.write(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteClassLoader.INSTANCE.addClass(classNode.name, byteArray);

        try {
            return setterType.cast(ByteClassLoader.INSTANCE.loadClass(classNode.name).getConstructor().newInstance());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /* Creates the newInstance method for ArrayCreator classes */
    private static MethodNode createNewInstanceMethodNode(String componentType, int[] dimensions) {
        MethodNode methodNode = new MethodNode(ACC_PUBLIC,
                "newInstance", "()Ljava/lang/Object;", null, null);
        InsnList insns = methodNode.instructions;
        Arrays.stream(dimensions).forEach(d -> insns.add(new LdcInsnNode(d)));
        insns.add(new MultiANewArrayInsnNode(Java.createArrayType(componentType, dimensions), dimensions.length));
        insns.add(new InsnNode(ARETURN));
        return methodNode;
    }

    /* Creates the set method for ArraySetter classes */
    private static MethodNode createSetMethodNode(Class<?> setterType, String arrayType) {
        String elementType;
        if (setterType == ObjectArraySetter.class) {
            elementType = "Ljava/lang/Object;";
        } else {
            elementType = arrayType.substring(1);
        }
        MethodNode methodNode = new MethodNode(ACC_PUBLIC,
                "set", "(Ljava/lang/Object;I" + elementType + ")V", null, null);
        int[] opcodes = getInstructions(elementType);
        InsnList insns = methodNode.instructions;
        insns.add(new VarInsnNode(ALOAD, 1));
        insns.add(new TypeInsnNode(CHECKCAST, arrayType));
        insns.add(new VarInsnNode(ILOAD, 2));
        insns.add(new VarInsnNode(opcodes[0], 3));
        insns.add(new InsnNode(opcodes[1]));
        insns.add(new InsnNode(RETURN));

        return methodNode;
    }

    /*
     * gets the pair of appropriate load (type) and store (array of type) instructions
     */
    private static int[] getInstructions(String type) {
        switch (type) {
            case "B":
            case "Z":
                return new int[]{ILOAD, BASTORE}; // load int, store byte??
            case "S":
                return new int[]{ILOAD, SASTORE};
            case "I":
                return new int[]{ILOAD, IASTORE};
            case "J":
                return new int[]{LLOAD, LASTORE};
            case "F":
                return new int[]{FLOAD, FASTORE};
            case "D":
                return new int[]{DLOAD, DASTORE};
            case "C":
                return new int[]{ILOAD, CASTORE};
            default:
                return new int[]{ALOAD, AASTORE};
        }
    }
}
