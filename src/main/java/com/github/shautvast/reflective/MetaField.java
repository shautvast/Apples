package com.github.shautvast.reflective;

public class MetaField {

    private final String name;
    private final int modifiers;

    private final Class<?> type;

    public MetaField(String name, int modifiers, Class<?> type) {
        this.name = name;
        this.modifiers = modifiers;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getModifiers() {
        return modifiers;
    }
}
