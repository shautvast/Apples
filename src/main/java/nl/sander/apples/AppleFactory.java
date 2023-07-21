package nl.sander.apples;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.*;

import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.UUID;

import static org.objectweb.asm.Opcodes.*;

 class AppleFactory extends ClassVisitor {

    public static final String SUPER = javaName(BaseApple.class.getName());

    public static final String INIT = "<init>";
    public static final String ZERO_ARGS_VOID = "()V";

    private boolean isRecord = false;

    public AppleFactory() {
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
        classNode.version = V20;
        classNode.access = ACC_PUBLIC;
        MethodNode constructor = new MethodNode(ACC_PUBLIC, INIT, ZERO_ARGS_VOID, null, null);
        constructor.instructions.add(new VarInsnNode(ALOAD, 0));
        constructor.instructions.add(new MethodInsnNode(INVOKESPECIAL, SUPER, INIT, ZERO_ARGS_VOID));
        constructor.instructions.add(new InsnNode(RETURN));
        classNode.methods.add(constructor);

        compareMethod = new MethodNode(ACC_PUBLIC,
                "compare", "(Ljava/lang/Object;Ljava/lang/Object;)Lnl/sander/apples/Result;", null, null);
        classNode.methods.add(compareMethod);
        add(new VarInsnNode(ALOAD,0));
    }

    public MethodVisitor visitMethod(int access, String methodname,
                                     String desc, String signature, String[] exceptions) {
        if (!hasArgs(desc) && access == Modifier.PUBLIC && isRecord ||
                (methodname.startsWith("get") || (methodname.startsWith("is")) && desc.equals("()Z"))) {
            int startIndex;
            if (isRecord) {
                startIndex = 0;
            } else {
                if (methodname.startsWith("is")) {
                    startIndex = 2;
                } else {
                    startIndex = 3;
                }
            }

            visitGetter(correctName(methodname, startIndex), getReturnType(desc));
        }
        return null;
    }

    private void visitGetter(String getterMethodName, String returnType) {
        add(new VarInsnNode(ALOAD, 1));
        add(new TypeInsnNode(CHECKCAST, javaName(classToMap)));
        add(new MethodInsnNode(INVOKEVIRTUAL, classToMap, getterMethodName, "()" + returnType));

        add(new VarInsnNode(ALOAD, 2));
        add(new TypeInsnNode(CHECKCAST, javaName(classToMap)));
        add(new MethodInsnNode(INVOKEVIRTUAL, classToMap, getterMethodName, "()" + returnType));

        add(new MethodInsnNode(INVOKESTATIC, "nl/sander/apples/Apples", "compare", "(Ljava/lang/Object;Ljava/lang/Object;)Lnl/sander/apples/Result;"));
        add(new VarInsnNode(ASTORE, 3 + (localVarIndex++)));
    }

    private String correctName(String getterMethodName, int startIndex) {
        String tmp = getterMethodName.substring(startIndex);
        return tmp.substring(0, 1).toLowerCase() + tmp.substring(1);
    }

    @Override
    public void visitEnd() {
        if (localVarIndex < 6) {
            add(new InsnNode(3 + localVarIndex));
        } else {
            add(new LdcInsnNode(localVarIndex));
        }
        add(new TypeInsnNode(ANEWARRAY, "nl/sander/apples/Result"));

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

        add(new MethodInsnNode(INVOKESTATIC, "nl/sander/apples/Result", "merge", "([Lnl/sander/apples/Result;)Lnl/sander/apples/Result;"));
        add(new InsnNode(ARETURN));
    }

    private void add(AbstractInsnNode ins) {
        compareMethod.instructions.add(ins);
    }

    private String getReturnType(String desc) {
        return desc.substring(2);
    }

    private boolean hasArgs(String desc) {
        return desc.charAt(1) != ')';
    }

    private static String javaName(String className) {
        return className.replaceAll("\\.", "/");
    }

}
