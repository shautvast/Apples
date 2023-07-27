package com.github.shautvast.reflective.tomap;


import com.github.shautvast.reflective.java.ByteClassLoader;
import com.github.shautvast.reflective.java.Java;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Turns any class (beans with getters/setters and records) into a HashMap.
 */
public class ToMap {
    private static final ConcurrentMap<Class<?>, AbstractToMap> cache = new ConcurrentHashMap<>();

    /**
     * @param value some bean or record (so use wisely if the class is not of that pattern)
     * @return Map<String, Object>
     * @throws Exception if the class that you want to mappify somehow can't be read from the classpath
     */
    public static Map<String, Object> map(Object value) throws Exception {
        try {
            return cache.computeIfAbsent(value.getClass(), k ->
                    createNew(value)).toMap(value);
        } catch (RuntimeException e) {
            throw new Exception(e.getCause());
        }
    }

    private static AbstractToMap createNew(Object value) {
        try{
        ClassReader cr = Java.getClassReader(value);
        ToMapFactory factory = new ToMapFactory();
        cr.accept(factory, ClassReader.SKIP_FRAMES);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        factory.classNode.accept(classWriter);
        byte[] byteArray = classWriter.toByteArray();

        ByteClassLoader.INSTANCE.addClass(factory.classNode.name, byteArray);
        return (AbstractToMap) ByteClassLoader.INSTANCE.loadClass(factory.classNode.name).getConstructor().newInstance();}
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
