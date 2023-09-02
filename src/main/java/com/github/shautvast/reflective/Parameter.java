package com.github.shautvast.reflective;

public class Parameter<T> {
    private final Class<T> type;
    private final String descriptor;

    public Parameter(Class<T> type, String descriptor) {
        if (type ==null){
            throw new IllegalArgumentException("Type cannot be null");
        }
        this.type = type;
        this.descriptor = descriptor;
    }

    public Class<T> getType() {
        return type;
    }

    public boolean isVoid(){
        return type == Void.class;
    }

    public String getDescriptor() {
        return descriptor;
    }
}
