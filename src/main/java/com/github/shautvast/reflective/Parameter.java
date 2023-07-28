package com.github.shautvast.reflective;

public class Parameter<T> {
    private final Class<T> type;
    private final String descriptor;

    public Parameter(Class<T> type, String descriptor) {
        this.type = type;
        this.descriptor = descriptor;
    }

    public Class<T> getType() {
        return type;
    }

    public boolean isVoid(){
        return type == Void.class;
    }

    public boolean isPrimitive(){
        return !descriptor.startsWith("L");
    }

    public String getDescriptor() {
        return descriptor;
    }
}
