package com.github.shautvast.reflective;

import com.github.shautvast.reflective.compare.AbstractComparator;
import com.github.shautvast.reflective.java.Java;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.ClassNode;

import static org.objectweb.asm.Opcodes.ASM9;

public class MetaClassFactory extends ClassVisitor {
    public static final String SUPER = Java.internalName(AbstractComparator.class);

    private boolean isRecord = false;

    final ClassNode classNode = new ClassNode();

    private MetaClass metaClassToBuild;

    public MetaClassFactory() {
        super(ASM9);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        metaClassToBuild = new MetaClass(Java.externalName(name));
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        metaClassToBuild.addField(access, name, descriptor);
        return null;
    }

    @Override
    public MethodVisitor visitMethod(int access, String methodname,
                                     String descriptor, String signature, String[] exceptions) {
        metaClassToBuild.addMethod(access, methodname, descriptor);
        return null;
    }

    public MetaClass getMetaClass() {
        return metaClassToBuild;
    }
}
