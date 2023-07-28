package com.github.shautvast.reflective;

import com.github.shautvast.reflective.java.Java;
import org.objectweb.asm.ClassReader;

public class Reflective {

    public static MetaClass getMetaClass(Class<?> type) {
        try {
            ClassReader cr = Java.getClassReader(type);

            MetaClassFactory factory = new MetaClassFactory(type);
            cr.accept(factory, ClassReader.SKIP_FRAMES);
            return factory.getMetaClass();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
