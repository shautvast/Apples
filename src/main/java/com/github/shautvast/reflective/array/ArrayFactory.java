package com.github.shautvast.reflective.array;

import com.github.shautvast.reflective.java.ASM;
import com.github.shautvast.reflective.java.ByteClassLoader;
import com.github.shautvast.reflective.java.Java;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ARETURN;

/**
 * Factory class for dynamically creating arrays
 */
public class ArrayFactory {

    private static final ConcurrentMap<String, ArrayCreator> cache = new ConcurrentHashMap<>();

    /**
     * Creates a new array of the specified type and dimensions
     *
     * @param elementType the array element type
     * @param dimensions  array of ints, ie {10} means 1 dimension, length 10. {10,20} means 2 dimensions, size 10 by 20
     * @return an object that you can cast to the expected array type
     */
    public static Object newArray(Class<?> elementType, int... dimensions) {
        String elementTypeName = Java.internalName(elementType);
        String syntheticClassName = syntheticClassName(elementTypeName, dimensions);

        return cache.computeIfAbsent(syntheticClassName,
                k -> createSyntheticArrayCreator(elementTypeName, syntheticClassName, dimensions)).newInstance();
    }

    private static String syntheticClassName(String elementTypeName, int[] dimensions) {
        return "RAC" + elementTypeName
                .replaceAll("[/.\\[;]", "")
                .toLowerCase() + "L" + dimensions.length;
    }

    private static ArrayCreator createSyntheticArrayCreator(String componentType, String name, int... dimensions) {
        ClassNode classNode = createASMClassNode(componentType, name, dimensions);
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

    private static ClassNode createASMClassNode(String componentType, String name, int[] dimensions) {
        ClassNode classNode = ASM.createDefaultClassNode(name,
                Java.internalName(ArrayCreator.class));
        MethodNode methodNode = new MethodNode(ACC_PUBLIC,
                "newInstance", "()Ljava/lang/Object;", null, null);
        classNode.methods.add(methodNode);
        InsnList insns = methodNode.instructions;
        Arrays.stream(dimensions).forEach(d -> insns.add(new LdcInsnNode(d)));
        insns.add(new MultiANewArrayInsnNode(createArrayType(componentType, dimensions), dimensions.length));
        insns.add(new InsnNode(ARETURN));
        return classNode;
    }

    private static String createArrayType(String componentType, int[] dimensions) {
        StringBuilder s = new StringBuilder();
        s.append("[".repeat(dimensions.length));
        boolean isObject = !componentType.startsWith("[") && componentType.contains("/");
        if (isObject) {
            s.append("L");
            s.append(componentType);
        } else {
            s.append(Java.mapPrimitiveOrArrayName(componentType));
        }
        if (isObject) {
            s.append(";");
        }
        return s.toString();
    }

}
