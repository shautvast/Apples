package com.github.shautvast.reflective.array.base;

public abstract class DoubleArrayAccessor implements ArrayAccessor {

    public abstract void set(Object array, int index, double value);
    public abstract double get(Object array, int index);
}
