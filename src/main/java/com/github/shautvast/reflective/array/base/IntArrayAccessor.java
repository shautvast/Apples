package com.github.shautvast.reflective.array.base;

public abstract class IntArrayAccessor implements ArrayAccessor {
    public abstract void set(Object array, int index, int value);

    public abstract int get(Object array, int index);
}
