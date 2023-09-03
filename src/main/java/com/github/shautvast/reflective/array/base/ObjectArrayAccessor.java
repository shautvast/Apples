package com.github.shautvast.reflective.array.base;

public abstract class ObjectArrayAccessor implements ArrayAccessor {
    public abstract void set(Object array, int index, Object value);
    public abstract Object get(Object array, int index);
}
