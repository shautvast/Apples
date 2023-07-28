package com.github.shautvast.reflective;

import com.github.shautvast.reflective.compare.AbstractComparator;
import com.github.shautvast.reflective.java.Java;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.ASM9;

public class MetaClassFactory extends ClassVisitor {
    public static final String SUPER = Java.internalName(AbstractComparator.class);

    final Class<?> javaClass;

    private MetaClass metaClassToBuild;

    public MetaClassFactory(Class<?>javaClass) {
        super(ASM9);
        this.javaClass = javaClass;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        metaClassToBuild = new MetaClass(javaClass, name);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        metaClassToBuild.addField(access, name, descriptor);
        return null;
    }

    @Override
    public MethodVisitor visitMethod(int access, String methodname,
                                     String descriptor, String signature, String[] exceptions) {
        if (!Java.isConstructor(methodname)) {
            metaClassToBuild.addMethod(access, methodname, descriptor);
        } else {
            metaClassToBuild.addConstructor(access, methodname, descriptor);
        }
        return null;
    }

    public MetaClass getMetaClass() {
        return metaClassToBuild;
    }
}
