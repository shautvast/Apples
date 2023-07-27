package com.github.shautvast.reflective;

import java.util.*;

public class MetaClass {

    private final String name;
    private final Map<String, MetaField> fields;
    private final Map<String, MetaMethod> methods;

    public MetaClass(String name) {
        this.name = name;
        this.fields = new HashMap<>();
        this.methods = new HashMap<>();
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

    void addField(int access, String name, String descriptor) {
        fields.put(name, new MetaField(name));
    }

    public void addMethod(int access, String methodname, String descriptor) {
        methods.put(methodname, new MetaMethod(methodname));
    }
}
