package com.github.shautvast.reflective.tomap;

import com.github.shautvast.reflective.java.Java;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.*;

import java.lang.reflect.Modifier;
import java.util.UUID;

import static org.objectweb.asm.Opcodes.*;

class ToMapFactory extends ClassVisitor {
    public static final String SUPER_NAME = Java.internalName(AbstractToMap.class.getName());
    private boolean isRecord = false;
    final ClassNode classNode = new ClassNode();

    private String classToMap;

    private MethodNode mappifyMethod;


    public ToMapFactory() {
        super(ASM9);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if (superName.equals("java/lang/Record")) {
            isRecord = true;
        }
        this.classToMap = name;
        classNode.name = "ToMap" + UUID.randomUUID();
        classNode.superName = SUPER_NAME;
        classNode.version = V11;
        classNode.access = ACC_PUBLIC;
        MethodNode constructor = new MethodNode(ACC_PUBLIC, Java.INIT, Java.ZERO_ARGS_VOID, null, null);
        constructor.instructions.add(new VarInsnNode(ALOAD, 0));
        constructor.instructions.add(new MethodInsnNode(INVOKESPECIAL, SUPER_NAME, Java.INIT, Java.ZERO_ARGS_VOID));
        constructor.instructions.add(new InsnNode(RETURN));
        classNode.methods.add(constructor);

        mappifyMethod = new MethodNode(ACC_PUBLIC,
                "toMap", "(Ljava/lang/Object;)Ljava/util/Map;", null, null);
        classNode.methods.add(mappifyMethod);
        add(new TypeInsnNode(NEW, "java/util/HashMap"));
        add(new InsnNode(DUP));
        add(new MethodInsnNode(INVOKESPECIAL, "java/util/HashMap", Java.INIT, Java.ZERO_ARGS_VOID));
        add(new VarInsnNode(ASTORE, 2));
    }

    @Override
    public MethodVisitor visitMethod(int access, String methodname,
                                     String desc, String signature, String[] exceptions) {
        if (!Java.hasArgs(desc) && access == Modifier.PUBLIC && isRecord ||
                (methodname.startsWith("get") || (methodname.startsWith("is")) && desc.equals("()Z"))) {
            visitGetter(methodname, Java.asProperty(methodname, isRecord), Java.getReturnType(desc));
        }
        return null;
    }

    private void visitGetter(String getterMethodName, String propertyName, String returnType) {
        add(new VarInsnNode(ALOAD, 0));
        add(new VarInsnNode(ALOAD, 2));
        add(new LdcInsnNode(propertyName));
        add(new VarInsnNode(ALOAD, 1));
        add(new TypeInsnNode(CHECKCAST, Java.internalName(classToMap)));
        add(new MethodInsnNode(INVOKEVIRTUAL, classToMap, getterMethodName, "()" + returnType));
        add(new MethodInsnNode(INVOKEVIRTUAL, classNode.name, "add", "(Ljava/util/HashMap;Ljava/lang/String;"+translate(returnType)+")V"));
    }

    private String translate(String typeDesc){
        if (typeDesc.startsWith("L")){
            return Java.OBJECT;
        } else {
            return typeDesc;
        }
    }

    @Override
    public void visitEnd() {
        add(new VarInsnNode(ALOAD, 2));
        add(new InsnNode(ARETURN));
    }

    private void add(AbstractInsnNode ins) {
        mappifyMethod.instructions.add(ins);
    }
}
