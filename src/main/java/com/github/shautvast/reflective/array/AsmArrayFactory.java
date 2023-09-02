package com.github.shautvast.reflective.array;

import com.github.shautvast.reflective.array.base.ArrayCreator;
import com.github.shautvast.reflective.array.base.ObjectArraySetter;
import com.github.shautvast.reflective.java.ASM;
import com.github.shautvast.reflective.java.ByteClassLoader;
import com.github.shautvast.reflective.java.Java;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.objectweb.asm.Opcodes.*;

class AsmArrayFactory {
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

    private static MethodNode createNewInstanceMethodNode(String componentType, int[] dimensions) {
        MethodNode methodNode = new MethodNode(ACC_PUBLIC,
                "newInstance", "()Ljava/lang/Object;", null, null);
        InsnList insns = methodNode.instructions;
        Arrays.stream(dimensions).forEach(d -> insns.add(new LdcInsnNode(d)));
        insns.add(new MultiANewArrayInsnNode(Java.createArrayType(componentType, dimensions), dimensions.length));
        insns.add(new InsnNode(ARETURN));
        return methodNode;
    }

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
}
