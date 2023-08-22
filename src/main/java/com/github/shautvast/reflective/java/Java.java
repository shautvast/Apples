package com.github.shautvast.reflective.java;

import org.objectweb.asm.ClassReader;

import java.io.IOException;

import static java.lang.reflect.Array.newInstance;

/*
 * common utils not for external use
 */
public class Java {

    public static final String INIT = "<init>";
    public static final String ZERO_ARGS_VOID = "()V";
    public static final String OBJECT = "Ljava/lang/Object;";

    public static final String BOOLEAN = "java/lang/Boolean";
    public static final String INTEGER = "java/lang/Integer";
    public static final String LONG = "java/lang/Long";
    public static final String FLOAT = "java/lang/Float";
    public static final String DOUBLE = "java/lang/Double";
    public static final String CHAR = "java/lang/Character";
    public static final String SHORT = "java/lang/Short";
    public static final String BYTE = "java/lang/Byte";

    public static String internalName(String className) {
        return className.replaceAll("\\.", "/");
    }

    public static String internalName(Class<?> type) {
        return internalName(type.getName());
    }

    public static String externalName(String internalName) {
        return internalName.replaceAll("/", ".");
    }

    public static boolean hasArgs(String desc) {
        return desc.charAt(1) != ')';
    }

    public static String asProperty(String getter, boolean isRecord) {
        if (isRecord) {
            return getter;
        } else {
            if (getter.startsWith("get")) {
                return getter.substring(3, 4).toLowerCase() + getter.substring(4);
            } else {
                return getter.substring(2, 3).toLowerCase() + getter.substring(3);
            }
        }
    }

    public static String getReturnType(String desc) {
        return desc.substring(2);
    }

    public static ClassReader getClassReader(Object value) throws ClassNotFoundException {
        return getClassReader(value.getClass());
    }

    public static ClassReader getClassReader(Class<?> type) throws ClassNotFoundException {
        try {
            return new ClassReader(type.getName());
        } catch (IOException e) {
            throw new ClassNotFoundException(type.getName());
        }
    }

    public static boolean isConstructor(String methodname) {
        return INIT.equals(methodname);
    }

    public static Class<?> getTypeFrom(String typedesc) {
        switch (typedesc) {
            case "B":
                return byte.class;
            case "I":
                return int.class;
            case "J":
                return long.class;
            case "Z":
                return boolean.class;
            case "C":
                return char.class;
            case "S":
                return short.class;
            case "F":
                return float.class;
            case "D":
                return double.class;

            default:
                try {
                    if (typedesc.startsWith("L")) {
                        return Class.forName(externalName(typedesc.substring(1)));
                    }
                    if (typedesc.startsWith("[") && !typedesc.endsWith(";")) {
                        return Class.forName(externalName(typedesc.substring(1)));
                    }
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
        }

        throw new RuntimeException();
    }

    public static Class<?> getClassFromDescriptor(String descriptor) {
        int arrayDims = 0;
        while (descriptor.startsWith("[")) {
            arrayDims++;
            descriptor = descriptor.substring(1);
        } //could be cheaper

        if (descriptor.startsWith("L") && descriptor.endsWith(";")) {
            try {
                String className = descriptor.substring(1, descriptor.length() - 1).replaceAll("/", ".");
                Class<?> clazz = Class.forName(className);
                if (arrayDims > 0) {
                    clazz = newInstance(clazz, new int[arrayDims]).getClass();
                }
                return clazz;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e); // not supposed to happen
            }
        } else {
            char typeChar = descriptor.charAt(0);
            boolean isArray = arrayDims != 0;
            switch (typeChar) {
                case 'B':
                    return isArray ? newInstance(byte.class, new int[arrayDims]).getClass() : byte.class;
                case 'C':
                    return isArray ? newInstance(char.class, new int[arrayDims]).getClass() : char.class;
                case 'D':
                    return isArray ? newInstance(double.class, new int[arrayDims]).getClass() : double.class;
                case 'F':
                    return isArray ? newInstance(float.class, new int[arrayDims]).getClass() : float.class;
                case 'I':
                    return isArray ? newInstance(int.class, new int[arrayDims]).getClass() : int.class;
                case 'J':
                    return isArray ? newInstance(long.class, new int[arrayDims]).getClass() : long.class;
                case 'S':
                    return isArray ? newInstance(short.class, new int[arrayDims]).getClass() : short.class;
                case 'Z':
                    return isArray ? newInstance(boolean.class, new int[arrayDims]).getClass() : boolean.class;
                case 'V':
                    return Void.class;
                default:
                    throw new RuntimeException(new ClassNotFoundException("unknown descriptor: " + descriptor)); //must not happen
            }
        }
    }
}
