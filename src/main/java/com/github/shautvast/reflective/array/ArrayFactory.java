package com.github.shautvast.reflective.array;

import com.github.shautvast.reflective.array.base.*;

/**
 * Public interface for dynamically working with arrays (create, set and get operations)
 */
public class ArrayFactory {


    /**
     * Creates a new array of the specified type and dimensions
     *
     * @param elementType the array element type
     * @param dimensions  array of ints, ie {10} means 1 dimension, length 10. {10,20} means 2 dimensions, size 10 by 20
     * @return an object that you can cast to the expected array type
     */
    public static Object newInstance(Class<?> elementType, int... dimensions) {
        return ArrayHandlerFactory.getCreatorInstance(elementType, dimensions).newInstance();
    }

    /**
     * Sets an Object value on an array
     *
     * @param array the array on which the value is set
     * @param index the array index
     * @param value the value to set
     */
    public static void set(Object array, int index, Object value) {
        ArrayHandlerFactory.getSetterInstance(ObjectArraySetter.class, typeChecked(array)).set(array, index, value);
    }

    /**
     * Sets an int value on an array
     *
     * @param array the array on which the value is set
     * @param index the array index
     * @param value the value to set
     */
    public static void set(Object array, int index, int value) {
        ArrayHandlerFactory.getSetterInstance(IntArraySetter.class, typeChecked(array)).set(array, index, value);
    }

    /**
     * Sets a byte value on an array
     *
     * @param array the array on which the value is set
     * @param index the array index
     * @param value the value to set
     */
    public static void set(Object array, int index, byte value) {
        ArrayHandlerFactory.getSetterInstance(ByteArraySetter.class, typeChecked(array)).set(array, index, value);
    }

    /**
     * Sets a short value on an array
     *
     * @param array the array on which the value is set
     * @param index the array index
     * @param value the value to set
     */
    public static void set(Object array, int index, short value) {
        ArrayHandlerFactory.getSetterInstance(ShortArraySetter.class, typeChecked(array)).set(array, index, value);
    }

    /**
     * Sets a long value on an array
     *
     * @param array the array on which the value is set
     * @param index the array index
     * @param value the value to set
     */
    public static void set(Object array, int index, long value) {
        ArrayHandlerFactory.getSetterInstance(LongArraySetter.class, typeChecked(array)).set(array, index, value);
    }

    /**
     * Sets a byte value on an array
     *
     * @param array the array on which the value is set
     * @param index the array index
     * @param value the value to set
     */
    public static void set(Object array, int index, float value) {
        ArrayHandlerFactory.getSetterInstance(FloatArraySetter.class, typeChecked(array)).set(array, index, value);
    }

    /**
     * Sets a double value on an array
     *
     * @param array the array on which the value is set
     * @param index the array index
     * @param value the value to set
     */
    public static void set(Object array, int index, double value) {
        ArrayHandlerFactory.getSetterInstance(DoubleArraySetter.class, typeChecked(array)).set(array, index, value);
    }

    public static void set(Object array, int index, char value) {
        ArrayHandlerFactory.getSetterInstance(CharArraySetter.class, typeChecked(array)).set(array, index, value);
    }

    public static void set(Object array, int index, boolean value) {
        ArrayHandlerFactory.getSetterInstance(BooleanArraySetter.class, typeChecked(array)).set(array, index, value);
    }

    /*
     * Only checks if object is an array, not the type of that array. TODO
     */
    private static Class<?> typeChecked(Object array) {
        Class<?> arrayType = array.getClass();
        if (!arrayType.isArray()) {
            throw new IllegalArgumentException("This is not an array");
        }
        return arrayType;
    }
}
