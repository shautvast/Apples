package com.github.shautvast.reflective;

import com.github.shautvast.reflective.java.ASM;
import com.github.shautvast.reflective.java.ByteClassLoader;
import com.github.shautvast.reflective.java.Java;
import com.github.shautvast.rusty.Result;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import static com.github.shautvast.rusty.Result.err;
import static com.github.shautvast.rusty.Result.ok;
import static org.objectweb.asm.Opcodes.*;

public class InvokerFactory {

    public static final String SUPER = Java.internalName(AbstractInvoker.class);

    public static Result<AbstractInvoker> of(MetaMethod m) {
        ClassNode classNode = ASM.createDefaultClassNode("Invoker" + m.getName() + m.getDescriptor().replaceAll("[()/;\\[]", ""), SUPER);

        MethodNode invokerMethod = new MethodNode(ACC_PUBLIC,
                "invoke", "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", null, null);
        invokerMethod.instructions.add(new VarInsnNode(ALOAD, 1));
        invokerMethod.instructions.add(new TypeInsnNode(CHECKCAST, m.getMetaClass().getRawName()));
        for (int i = 0; i < m.getParameters().size(); i++) {
            invokerMethod.instructions.add(new VarInsnNode(ALOAD, 2));
            if (i < 6) {
                invokerMethod.instructions.add(new InsnNode(3 + i)); //ICONST_X
            } else {
                invokerMethod.instructions.add(new LdcInsnNode(i));
            }
            invokerMethod.instructions.add(new InsnNode(AALOAD));
            invokerMethod.instructions.add(new TypeInsnNode(CHECKCAST, m.getParameters().get(i).getDescriptor()));
        }
        ;
        invokerMethod.instructions.add(new MethodInsnNode(INVOKEVIRTUAL, m.getMetaClass().getRawName(), m.getName(), m.getDescriptor()));

        convertReturnTypeForPrimitives(m, invokerMethod);

        invokerMethod.instructions.add(new InsnNode(ARETURN));
        classNode.methods.add(invokerMethod);

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classNode.accept(classWriter);
        byte[] byteArray = classWriter.toByteArray();

//        try (FileOutputStream out = new FileOutputStream("C.class")) {
//            out.write(byteArray);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        ByteClassLoader.INSTANCE.addClass(classNode.name, byteArray);
        try {
            return ok((AbstractInvoker) ByteClassLoader.INSTANCE.loadClass(classNode.name).getConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 ClassNotFoundException e) {
            return err(e);
        }
    }

    private static void convertReturnTypeForPrimitives(MetaMethod m, MethodNode invokerMethod) {
        switch (m.getReturnParameter().getDescriptor()) {
            case "V":
                invokerMethod.instructions.add(new InsnNode(ACONST_NULL));
                break;
            case "Z":
                invokerMethod.instructions.add(new MethodInsnNode(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;"));
                break;
            case "I":
                invokerMethod.instructions.add(new MethodInsnNode(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;"));
                break;
            case "J":
                invokerMethod.instructions.add(new MethodInsnNode(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;"));
                break;
            case "F":
                invokerMethod.instructions.add(new MethodInsnNode(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;"));
                break;
            case "D":
                invokerMethod.instructions.add(new MethodInsnNode(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;"));
                break;
            case "C":
                invokerMethod.instructions.add(new MethodInsnNode(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;"));
                break;
            case "S":
                invokerMethod.instructions.add(new MethodInsnNode(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;"));
                break;
            case "B":
                invokerMethod.instructions.add(new MethodInsnNode(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;"));
                break;
        }
    }


}
