package com.github.shautvast.reflective.array.base;

public abstract class ByteArrayAccessor implements ArrayAccessor {

    public abstract void set(Object array, int index, byte value);

    public abstract byte get(Object array, int index);
}
