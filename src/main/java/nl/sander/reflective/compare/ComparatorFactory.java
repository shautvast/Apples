package nl.sander.reflective.compare;

import nl.sander.reflective.java.Java;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.*;

import java.lang.reflect.Modifier;
import java.util.UUID;

import static org.objectweb.asm.Opcodes.*;

class ComparatorFactory extends ClassVisitor {

    public static final String SUPER = Java.internalName(AbstractComparator.class);


    private boolean isRecord = false;

    public ComparatorFactory() {
        super(ASM9);
    }

    final ClassNode classNode = new ClassNode();

    private String classToMap;

    private MethodNode compareMethod;

    private int localVarIndex = 0;

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if (superName.equals("java/lang/Record")) {
            isRecord = true;
        }
        this.classToMap = name;
        classNode.name = "Apple" + UUID.randomUUID();
        classNode.superName = SUPER;
        classNode.version = V11;
        classNode.access = ACC_PUBLIC;
        MethodNode constructor = new MethodNode(ACC_PUBLIC, Java.INIT, Java.ZERO_ARGS_VOID, null, null);
        constructor.instructions.add(new VarInsnNode(ALOAD, 0));
        constructor.instructions.add(new MethodInsnNode(INVOKESPECIAL, SUPER, Java.INIT, Java.ZERO_ARGS_VOID));
        constructor.instructions.add(new InsnNode(RETURN));
        classNode.methods.add(constructor);

        compareMethod = new MethodNode(ACC_PUBLIC,
                "compare", "(Ljava/lang/Object;Ljava/lang/Object;)L" + Java.internalName(Result.class) + ";", null, null);
        classNode.methods.add(compareMethod);
        add(new VarInsnNode(ALOAD, 0));
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
        add(new LdcInsnNode(propertyName));
        add(new VarInsnNode(ALOAD, 1));
        add(new TypeInsnNode(CHECKCAST, Java.internalName(classToMap)));
        add(new MethodInsnNode(INVOKEVIRTUAL, classToMap, getterMethodName, "()" + returnType));

        add(new VarInsnNode(ALOAD, 2));
        add(new TypeInsnNode(CHECKCAST, Java.internalName(classToMap)));
        add(new MethodInsnNode(INVOKEVIRTUAL, classToMap, getterMethodName, "()" + returnType));

        add(new MethodInsnNode(INVOKESTATIC, Java.internalName(Compare.class), "compare", "("
                + getSignature(returnType)
                + ")L" + Java.internalName(Result.class) + ";"));
        add(new VarInsnNode(ASTORE, 3 + (localVarIndex++)));
    }

    private String getSignature(String returnType) {
        String type;
        if (returnType.startsWith("L")) {
            type = "Ljava/lang/Object;";
        } else {
            type = returnType;
        }
        return "Ljava/lang/String;" + type + type;
    }

    @Override
    public void visitEnd() {
        if (localVarIndex < 6) {
            add(new InsnNode(3 + localVarIndex));
        } else {
            add(new LdcInsnNode(localVarIndex));
        }
        add(new TypeInsnNode(ANEWARRAY, Java.internalName(Result.class)));

        for (int i = 0; i < localVarIndex; i++) {
            add(new InsnNode(DUP));
            if (i < 6) {
                add(new InsnNode(3 + i));
            } else {
                add(new LdcInsnNode(i));
            }
            add(new VarInsnNode(ALOAD, 3 + i));
            add(new InsnNode(AASTORE));
        }

        add(new MethodInsnNode(INVOKESTATIC, Java.internalName(Result.class), "merge", "([L" + Java.internalName(Result.class) + ";)L" + Java.internalName(Result.class) + ";"));
        add(new InsnNode(ARETURN));
    }

    private void add(AbstractInsnNode ins) {
        compareMethod.instructions.add(ins);
    }


}
