package com.github.shautvast.reflective.java;

import org.objectweb.asm.ClassReader;

import java.io.IOException;

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

    public static Class<?> toClass(String descriptor) {
        System.out.println("desc;" + descriptor);
        try {
            if (descriptor.length() == 1) {
                return mapIfPrimitive(descriptor.charAt(0));
            }
            if (!descriptor.startsWith("[") && descriptor.endsWith(";")) {
                descriptor = descriptor.substring(0, descriptor.length() - 1);
            }
            int i = 0;
            while (descriptor.charAt(i) == '[' && i < 256) {
                i++;
            }
            assert i < 256;
            String dims = descriptor.substring(0, i);
            String type = descriptor.substring(i);
            if (i == 0 && type.startsWith("L")) {
                type = type.substring(1);
            }
            type = Java.externalName(type);
            Class<?> aClass = Class.forName(dims + type);
            System.out.println(aClass);
            return aClass;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static Class<?> mapIfPrimitive(char descriptor) {
        switch (descriptor) {
            case 'B':
                return byte.class;
            case 'S':
                return short.class;
            case 'I':
                return int.class;
            case 'J':
                return long.class;
            case 'F':
                return float.class;
            case 'D':
                return double.class;
            case 'C':
                return char.class;
            case 'Z':
                return boolean.class;
            case 'V':
                return Void.class;
            default:
                System.out.println("desc:" + descriptor);
                return null;
        }
    }

    public static String mapPrimitiveOrArrayName(String type) {
        switch (type) {
            case "byte":
                return "B";
            case "short":
                return "S";
            case "int":
                return "I";
            case "long":
                return "J";
            case "float":
                return "F";
            case "double":
                return "D";
            case "char":
                return "C";
            case "boolean":
                return "Z";
            default:
                if (type.startsWith("[") || type.contains("/")) {
                    return type;
                } else {
                    throw new IllegalArgumentException(type + "?");
                }
        }
    }
}
