package com.github.shautvast.reflective.array.base;

public abstract class BooleanArrayAccessor implements ArrayAccessor {
    public abstract void set(Object array, int index, boolean value);
    public abstract boolean get(Object array, int index);
}
