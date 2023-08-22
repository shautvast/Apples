package com.github.shautvast.reflective;

import com.github.shautvast.reflective.java.ASM;
import com.github.shautvast.reflective.java.ByteClassLoader;
import com.github.shautvast.reflective.java.Java;
import com.github.shautvast.rusty.Result;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.lang.reflect.InvocationTargetException;

import static com.github.shautvast.reflective.java.Java.DOUBLE;
import static com.github.shautvast.reflective.java.Java.FLOAT;
import static com.github.shautvast.reflective.java.Java.INTEGER;
import static com.github.shautvast.reflective.java.Java.LONG;
import static com.github.shautvast.reflective.java.Java.*;
import static com.github.shautvast.rusty.Result.err;
import static com.github.shautvast.rusty.Result.ok;
import static org.objectweb.asm.Opcodes.*;

public class InvokerFactory {

    public static final String SUPER = Java.internalName(AbstractInvoker.class);

    public static final String VALUEOF = "valueOf";

    /**
     * Creates an invoker for a single method
     *
     * @param method the MetaMethod representing the method to invoke
     * @return a generated instance of an AbstractInvoker
     */
    public static Result<AbstractInvoker> of(MetaMethod method) {
        // new ASM ClassNode with default constructor
        String className = "Invoker" + method.getMetaClass().getJavaClass().getSimpleName() + method.getName() + method.getDescriptor().replaceAll("[()/;\\[]", "");
        if (ByteClassLoader.INSTANCE.isDefined(className)) {
            return getInvoker(className);
        }

        ClassNode classNode = ASM.createDefaultClassNode(className, SUPER);

        // the invoker method
        MethodNode invokerMethod = new MethodNode(ACC_PUBLIC,
                "invoke", "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", null, null);
        // local var #1 is the class to invoke the method on
        InsnList insns = invokerMethod.instructions;
        insns.add(new VarInsnNode(ALOAD, 1));
        // it comes in as Object and has to be cast to it's actual type
        insns.add(new TypeInsnNode(CHECKCAST, method.getMetaClass().getRawName()));

        // the arguments are passed as varargs / array of Object
        for (int i = 0; i < method.getParameters().size(); i++) {
            insns.add(new VarInsnNode(ALOAD, 2));
            if (i < 6) {
                insns.add(new InsnNode(3 + i)); //ICONST_X
            } else {
                insns.add(new LdcInsnNode(i));
            }
            // put argument on the stack
            insns.add(new InsnNode(AALOAD));
            insns.add(new TypeInsnNode(CHECKCAST, Java.internalName(mapPrimitiveToWrapper(method.getParameters().get(i).getType()).getName())));
            unwrapPrimitive(method.getParameters().get(i), insns);
        }
        // call the method
        insns.add(new MethodInsnNode(INVOKEVIRTUAL, method.getMetaClass().getRawName(), method.getName(), method.getDescriptor()));
        // if the returned argument is primitive, wrap it as an Object
        wrapPrimitive(method.getReturnParameter(), invokerMethod.instructions);
        // return the object on the stack
        insns.add(new InsnNode(ARETURN));

        // add the method to the class
        classNode.methods.add(invokerMethod);

        // create the bytecode
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classNode.accept(classWriter);
        byte[] byteArray = classWriter.toByteArray();

//        try (FileOutputStream out = new FileOutputStream("C"+method.getName() + ".class")) {
//            out.write(byteArray);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // load it into the JVM
        ByteClassLoader.INSTANCE.addClass(className, byteArray);
        return getInvoker(classNode.name);
    }

    private static Result<AbstractInvoker> getInvoker(String name) {
        try {
            return ok((AbstractInvoker) ByteClassLoader.INSTANCE.loadClass(name).getConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 ClassNotFoundException e) {
            return err(e);
        }
    }

    private static void unwrapPrimitive(Parameter<?> parameter, InsnList insns) {
        switch (parameter.getDescriptor()) {
            case "B":
                insns.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/Byte", "byteValue", "()B"));
                break;
            case "S":
                insns.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/Short", "shortValue", "()S"));
                break;
            case "I":
                insns.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I"));
                break;
            case "Z":
                insns.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z"));
                break;
            case "J":
                insns.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/Long", "longValue", "()J"));
                break;
            case "F":
                insns.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/Float", "floatValue", "()F"));
                break;
            case "D":
                insns.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D"));
                break;
            case "C":
                insns.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/Character", "charValue", "()C"));
        }
    }

    /*
     * wrap primitives in their wrapper
     */
    private static void wrapPrimitive(Parameter<?> parameter, InsnList insns) {
        switch (parameter.getDescriptor()) {
            case "V":
                insns.add(new InsnNode(ACONST_NULL));
                break;
            case "Z":
                insns.add(new MethodInsnNode(INVOKESTATIC, BOOLEAN, VALUEOF, "(Z)Ljava/lang/Boolean;"));
                break;
            case "I":
                insns.add(new MethodInsnNode(INVOKESTATIC, INTEGER, VALUEOF, "(I)Ljava/lang/Integer;"));
                break;
            case "J":
                insns.add(new MethodInsnNode(INVOKESTATIC, LONG, VALUEOF, "(J)Ljava/lang/Long;"));
                break;
            case "F":
                insns.add(new MethodInsnNode(INVOKESTATIC, FLOAT, VALUEOF, "(F)Ljava/lang/Float;"));
                break;
            case "D":
                insns.add(new MethodInsnNode(INVOKESTATIC, DOUBLE, VALUEOF, "(D)Ljava/lang/Double;"));
                break;
            case "C":
                insns.add(new MethodInsnNode(INVOKESTATIC, CHAR, VALUEOF, "(C)Ljava/lang/Character;"));
                break;
            case "S":
                insns.add(new MethodInsnNode(INVOKESTATIC, SHORT, VALUEOF, "(S)Ljava/lang/Short;"));
                break;
            case "B":
                insns.add(new MethodInsnNode(INVOKESTATIC, BYTE, VALUEOF, "(B)Ljava/lang/Byte;"));
                break;
        }
    }

    private static Class<?> mapPrimitiveToWrapper(Class<?> type) {
        if (type == int.class) {
            return Integer.class;
        } else if (type == byte.class) {
            return Byte.class;
        } else if (type == short.class) {
            return Short.class;
        } else if (type == long.class) {
            return Long.class;
        } else if (type == float.class) {
            return Float.class;
        } else if (type == double.class) {
            return Double.class;
        } else if (type == char.class) {
            return Character.class;
        } else if (type == boolean.class) {
            return Boolean.class;
        } else {
            return type;
        }
    }


}
