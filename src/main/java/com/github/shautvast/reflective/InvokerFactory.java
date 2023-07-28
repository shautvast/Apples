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
        ClassNode classNode = ASM.createDefaultClassNode("Invoker" + UUID.randomUUID(), SUPER);

        MethodNode invokerMethod = new MethodNode(ACC_PUBLIC,
                "invoke", "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", null, null);
        invokerMethod.instructions.add(new VarInsnNode(ALOAD, 1));
        invokerMethod.instructions.add(new TypeInsnNode(CHECKCAST, m.getMetaClass().getRawName()));
        invokerMethod.instructions.add(new MethodInsnNode(INVOKEVIRTUAL, m.getMetaClass().getRawName(), m.getName(), m.getDescriptor()));
        invokerMethod.instructions.add(new InsnNode(ARETURN));
        classNode.methods.add(invokerMethod);

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classNode.accept(classWriter);
        byte[] byteArray = classWriter.toByteArray();

        try (FileOutputStream out = new FileOutputStream("C.class")) {
            out.write(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteClassLoader.INSTANCE.addClass(classNode.name, byteArray);
        try {
            return ok((AbstractInvoker) ByteClassLoader.INSTANCE.loadClass(classNode.name).getConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 ClassNotFoundException e) {
            return err(e);
        }
    }


}
