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

    public static String internalName(String className) {
        return className.replaceAll("\\.", "/");
    }

    public static String internalName(Class<?> type) {
        return internalName(type.getName());
    }

    public static String externalName(String internalName){
        return internalName.replaceAll("/",".");
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
}