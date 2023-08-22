package com.github.shautvast.reflective.array;

import com.github.shautvast.reflective.java.ASM;
import com.github.shautvast.reflective.java.ByteClassLoader;
import com.github.shautvast.reflective.java.Java;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.io.FileOutputStream;
import java.io.IOException;

import static org.objectweb.asm.Opcodes.*;

public class ArrayFactory {

    /*
     should become
     public static <T> T newArray(Class<T> componentType)
     or
     public static <T> T newArray(Class<T> componentType, int... dimensions)
     */
    public static Object newArray(Class<?> componentType, int... dimensions) {
        return  newArray(componentType.getName(), dimensions);
    }

    public static Object newArray(String componentType, int... dimensions) {
        ClassNode classNode = ASM.createDefaultClassNode(
                "ReflectiveCreator" + dimensions.length,
                Java.internalName(ArrayCreator.class));
        MethodNode methodNode = new MethodNode(ACC_PUBLIC,
                "newInstance", "()Ljava/lang/Object;", null, null);
        classNode.methods.add(methodNode);
        InsnList insns = methodNode.instructions;

        int localVarIndex = 0;
        StringBuilder arrayType = new StringBuilder(dimensions.length);
        String post = "";
        String pre = "";
        insns.add(new LdcInsnNode(1));
        insns.add(new LdcInsnNode(1));
        insns.add(new LdcInsnNode(1));
        insns.add(new MultiANewArrayInsnNode("[[[Ljava/lang/String;",3));

//        for (int dim : dimensions) {
//            insns.add(new LdcInsnNode(dim));
//            String type = arrayType + pre + Java.internalName(componentType) + post;
//            insns.add(new TypeInsnNode(ANEWARRAY, type));
//            insns.add(new VarInsnNode(ASTORE, ++localVarIndex));
//            arrayType.append("[");
//            pre = "L";
//            post = ";";
//
//        }

//        insns.add(new VarInsnNode(ALOAD, localVarIndex));
        insns.add(new InsnNode(ARETURN));
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classNode.accept(classWriter);
        byte[] byteArray = classWriter.toByteArray();

        try (FileOutputStream out = new FileOutputStream("A.class")) {
            out.write(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // load it into the JVM
        ByteClassLoader.INSTANCE.addClass(classNode.name, byteArray);
        try {
            return ((ArrayCreator) ByteClassLoader.INSTANCE.loadClass(classNode.name).getConstructor().newInstance()).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
