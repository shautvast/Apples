package com.github.shautvast.reflective.array.base;

public abstract class LongArrayAccessor implements ArrayAccessor {

    public abstract void set(Object array, int index, long value);

    public abstract long get(Object array, int index);
}
