package com.github.shautvast.reflective.java;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import static org.objectweb.asm.Opcodes.*;

public class ASM {
    private ASM() {
    }

    public static ClassNode createDefaultClassNode(String name, String superClass) {
        ClassNode classNode = new ClassNode(Opcodes.ASM9);
        classNode.name = name;
        classNode.superName = superClass;
        classNode.version = V11;
        classNode.access = ACC_PUBLIC;
        MethodNode constructor = new MethodNode(ACC_PUBLIC, Java.INIT, Java.ZERO_ARGS_VOID, null, null);
        constructor.instructions.add(new VarInsnNode(ALOAD, 0));
        constructor.instructions.add(new MethodInsnNode(INVOKESPECIAL, superClass, Java.INIT, Java.ZERO_ARGS_VOID));
        constructor.instructions.add(new InsnNode(RETURN));
        classNode.methods.add(constructor);
        return classNode;
    }
}
