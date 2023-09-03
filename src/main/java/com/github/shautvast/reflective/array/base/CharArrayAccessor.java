package com.github.shautvast.reflective.array.base;

public abstract class CharArrayAccessor implements ArrayAccessor {

    public abstract void set(Object array, int index, char value);
    public abstract char get(Object array, int index);
}
