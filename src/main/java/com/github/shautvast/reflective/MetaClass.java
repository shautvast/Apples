package com.github.shautvast.reflective;

import com.github.shautvast.reflective.java.Java;

import java.util.*;

public class MetaClass {

    private final String name;
    private final String rawName;
    private final Map<String, MetaField> fields;
    private final Map<String, MetaMethod> methods;
    private final Set<MetaMethod> constructors;
    private final Class<?> javaClass;

    public MetaClass(Class<?> javaClass, String rawName) {
        this.javaClass = javaClass;
        this.name = Java.externalName(rawName);
        this.rawName = rawName;
        this.fields = new HashMap<>();
        this.methods = new HashMap<>();
        this.constructors = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public Set<MetaField> getFields() {
        return Set.copyOf(fields.values());
    }

    public Set<MetaMethod> getMethods() {
        return Set.copyOf(methods.values());
    }

    // should account for overloading!
    public Optional<MetaMethod> getMethod(String name) {
        return Optional.ofNullable(methods.get(name));
    }

    public Set<MetaMethod> getConstructors() {
        return constructors;
    }

    void addField(int access, String name, String descriptor) {
        fields.put(name, new MetaField(name, access)); //ASM access same as reflect modifiers?
    }

    public void addMethod(int access, String name, String descriptor) {
        methods.put(name, new MetaMethod(this, name, access, descriptor));
    }

    public void addConstructor(int access, String methodname, String descriptor) {

    }

    public Class<?> getJavaClass() {
        return javaClass;
    }

    public String getRawName() {
        return rawName;
    }
}
