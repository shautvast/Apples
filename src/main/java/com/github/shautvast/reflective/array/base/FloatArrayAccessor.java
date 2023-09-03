package com.github.shautvast.reflective.array.base;

public abstract class FloatArrayAccessor implements ArrayAccessor {

    public abstract void set(Object array, int index, float value);

    public abstract float get(Object array, int index);
}
