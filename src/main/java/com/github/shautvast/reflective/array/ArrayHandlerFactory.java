package com.github.shautvast.reflective.array;

import com.github.shautvast.reflective.array.base.ArrayCreator;
import com.github.shautvast.reflective.array.base.ArrayAccessor;
import com.github.shautvast.reflective.array.base.ObjectArrayAccessor;
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

    /* Cache for the compiled accessor classes.
     *  The outer Map contains the ArrayAccessor type (some primitive or Object Accessor)
     *  That maps to the concrete calculated ArrayAccessor instance name which maps to the instance itself.
     *  TODO see if this can be optimized
     */
    private static final Map<Class<? extends ArrayAccessor>, Map<String, Object>> accessorCache = new ConcurrentHashMap<>();


    /*
     * generic method for creating array accessors (primitives and objects)
     */
    @SuppressWarnings("unchecked")
    static <T extends ArrayAccessor> T getAccessorInstance(Class<T> accessorBaseType, Class<?> arrayType) {
        String arrayTypeName = Java.internalName(arrayType);
        String syntheticClassName = "com/shautvast/reflective/array/ArrayAccessor_"
                + Java.javaName(arrayTypeName) + Java.getNumDimensions(arrayType);

        return (T) accessorCache.computeIfAbsent(accessorBaseType, k -> new ConcurrentHashMap<>()).
                computeIfAbsent(syntheticClassName,
                        k -> ArrayHandlerFactory.createSyntheticArrayAccessor(accessorBaseType, arrayTypeName, syntheticClassName));
    }

    /*  creates an instance of an ArrayCreator of the specified type */
    static ArrayCreator getCreatorInstance(Class<?> elementType, int... dimensions) {
        String elementTypeName = Java.internalName(elementType);
        String syntheticClassName = "com/shautvast/reflective/array/ArrayCreator_"
                + Java.javaName(elementTypeName) + dimensions.length;

        return creatorCache.computeIfAbsent(syntheticClassName,
                k -> ArrayHandlerFactory.createSyntheticArrayCreator(elementTypeName, syntheticClassName, dimensions));
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

    /* Creates the ASM ClassNode for an ArrayAccessor */
    static <T> T createSyntheticArrayAccessor(Class<T> accessorType, String arrayType, String name) {
        ClassNode classNode = ASM.createDefaultClassNode(name, Java.internalName(accessorType));
        classNode.methods.add(createSetMethodNode(accessorType, arrayType));
        classNode.methods.add(createGetMethodNode(accessorType, arrayType));
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
            return accessorType.cast(ByteClassLoader.INSTANCE.loadClass(classNode.name).getConstructor().newInstance());
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

    /* Creates the set method for ArrayAccessor classes */
    private static MethodNode createSetMethodNode(Class<?> accessorType, String arrayType) {
        String elementType;
        if (accessorType == ObjectArrayAccessor.class) {
            elementType = "Ljava/lang/Object;";
        } else {
            elementType = arrayType.substring(1);
        }
        MethodNode methodNode = new MethodNode(ACC_PUBLIC,
                "set", "(Ljava/lang/Object;I" + elementType + ")V", null, null);
        int[] opcodes = arrayStoreInstructions(elementType);
        InsnList insns = methodNode.instructions;
        insns.add(new VarInsnNode(ALOAD, 1));
        insns.add(new TypeInsnNode(CHECKCAST, arrayType));
        insns.add(new VarInsnNode(ILOAD, 2));
        insns.add(new VarInsnNode(opcodes[0], 3));
        insns.add(new InsnNode(opcodes[1]));
        insns.add(new InsnNode(RETURN));

        return methodNode;
    }

    private static MethodNode createGetMethodNode(Class<?> accessorType, String arrayType) {
        String elementType;
        if (accessorType == ObjectArrayAccessor.class) {
            elementType = "Ljava/lang/Object;";
        } else {
            elementType = arrayType.substring(1);
        }
        MethodNode methodNode = new MethodNode(ACC_PUBLIC,
                "get", "(Ljava/lang/Object;I)" + elementType, null, null);
        InsnList insns = methodNode.instructions;
        int[] opcodes = arrayLoadAndReturnInstructions(elementType);
        insns.add(new VarInsnNode(ALOAD, 1));
        insns.add(new TypeInsnNode(CHECKCAST, arrayType));
        insns.add(new VarInsnNode(ILOAD, 2));
        insns.add(new InsnNode(opcodes[0]));
        insns.add(new InsnNode(opcodes[1]));

        return methodNode;
    }

    /*
     * gets the pair of appropriate load (type) and store (array of type) instructions
     */
    private static int[] arrayStoreInstructions(String type) {
        switch (type) {
            case "B":
            case "Z":
                return new int[]{ILOAD, BASTORE};
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

    /*
     * gets the pair of appropriate load (type) and store (array of type) instructions
     */
    private static int[] arrayLoadAndReturnInstructions(String type) {
        switch (type) {
            case "B":
            case "Z":
                return new int[]{BALOAD, IRETURN};
            case "I":
                return new int[]{IALOAD, IRETURN};
            case "S":
                return new int[]{SALOAD, IRETURN};
            case "C":
                return new int[]{CALOAD, IRETURN};
            case "J":
                return new int[]{LALOAD, LRETURN};
            case "F":
                return new int[]{FALOAD, FRETURN};
            case "D":
                return new int[]{DALOAD, DRETURN};
            default:
                return new int[]{AALOAD, ARETURN};
        }
    }
}
