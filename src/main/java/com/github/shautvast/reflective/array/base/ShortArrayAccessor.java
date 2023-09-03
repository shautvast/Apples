package com.github.shautvast.reflective.array.base;

public abstract class ShortArrayAccessor implements ArrayAccessor {

    public abstract void set(Object array, int index, short value);

    public abstract short get(Object array, int index);
}
