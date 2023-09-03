package com.github.shautvast.reflective.array;

import com.github.shautvast.reflective.array.base.*;

import java.lang.reflect.Array;

/**
 * Public interface for dynamically working with arrays (create, set and get operations)
 * <p>
 * Drop in replacement for java.lang.reflect.Array
 */
public final class ArrayFactory {

    private ArrayFactory() {
    }

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
        ArrayHandlerFactory.getAccessorInstance(ObjectArrayAccessor.class, typeChecked(array)).set(array, index, value);
    }

    /**
     * Gets a Object value from an array
     *
     * @param array the array to access
     * @param index
     * @return the value of the array at the index
     */
    public static Object get(Object array, int index) {
        return ArrayHandlerFactory.getAccessorInstance(ObjectArrayAccessor.class, typeChecked(array)).get(array, index);
    }

    /**
     * Sets an int value on an array
     *
     * @param array the array on which the value is set
     * @param index the array index
     * @param value the value to set
     */
    public static void setInt(Object array, int index, int value) {
        ArrayHandlerFactory.getAccessorInstance(IntArrayAccessor.class, typeChecked(array)).set(array, index, value);
    }

    /**
     * Gets an int value from an array
     *
     * @param array the array to access
     * @param index
     * @return the value of the array at the index
     */
    public static int getInt(Object array, int index) {
        return ArrayHandlerFactory.getAccessorInstance(IntArrayAccessor.class, typeChecked(array)).get(array, index);
    }

    /**
     * Sets a byte value on an array
     *
     * @param array the array on which the value is set
     * @param index the array index
     * @param value the value to set
     */
    public static void setByte(Object array, int index, byte value) {
        ArrayHandlerFactory.getAccessorInstance(ByteArrayAccessor.class, typeChecked(array)).set(array, index, value);
    }

    /**
     * Gets a byte value from an array
     *
     * @param array the array to access
     * @param index
     * @return the value of the array at the index
     */
    public static byte getByte(Object array, int index) {
        return ArrayHandlerFactory.getAccessorInstance(ByteArrayAccessor.class, typeChecked(array)).get(array, index);
    }

    /**
     * Sets a short value on an array
     *
     * @param array the array on which the value is set
     * @param index the array index
     * @param value the value to set
     */
    public static void setShort(Object array, int index, short value) {
        ArrayHandlerFactory.getAccessorInstance(ShortArrayAccessor.class, typeChecked(array)).set(array, index, value);
    }

    /**
     * Gets a short value from an array
     *
     * @param array the array to access
     * @param index
     * @return the value of the array at the index
     */
    public static short getShort(Object array, int index) {
        return ArrayHandlerFactory.getAccessorInstance(ShortArrayAccessor.class, typeChecked(array)).get(array, index);
    }

    /**
     * Sets a long value on an array
     *
     * @param array the array on which the value is set
     * @param index the array index
     * @param value the value to set
     */
    public static void setLong(Object array, int index, long value) {
        ArrayHandlerFactory.getAccessorInstance(LongArrayAccessor.class, typeChecked(array)).set(array, index, value);
    }

    /**
     * Gets a long value from an array
     *
     * @param array the array to access
     * @param index
     * @return the value of the array at the index
     */
    public static long getLong(Object array, int index) {
        return ArrayHandlerFactory.getAccessorInstance(LongArrayAccessor.class, typeChecked(array)).get(array, index);
    }

    /**
     * Sets a byte value on an array
     *
     * @param array the array on which the value is set
     * @param index the array index
     * @param value the value to set
     */
    public static void setFloat(Object array, int index, float value) {
        ArrayHandlerFactory.getAccessorInstance(FloatArrayAccessor.class, typeChecked(array)).set(array, index, value);
    }

    /**
     * Gets a float value from an array
     *
     * @param array the array to access
     * @param index
     * @return the value of the array at the index
     */
    public static float getFloat(Object array, int index) {
        return ArrayHandlerFactory.getAccessorInstance(FloatArrayAccessor.class, typeChecked(array)).get(array, index);
    }

    /**
     * Sets a double value on an array
     *
     * @param array the array on which the value is set
     * @param index the array index
     * @param value the value to set
     */
    public static void setDouble(Object array, int index, double value) {
        ArrayHandlerFactory.getAccessorInstance(DoubleArrayAccessor.class, typeChecked(array)).set(array, index, value);
    }

    /**
     * Gets a double value from an array
     *
     * @param array the array to access
     * @param index
     * @return the value of the array at the index
     */
    public static double getDouble(Object array, int index) {
        return ArrayHandlerFactory.getAccessorInstance(DoubleArrayAccessor.class, typeChecked(array)).get(array, index);
    }

    /**
     * Sets a char value on an array
     *
     * @param array the array on which the value is set
     * @param index the array index
     * @param value the value to set
     */
    public static void setChar(Object array, int index, char value) {
        ArrayHandlerFactory.getAccessorInstance(CharArrayAccessor.class, typeChecked(array)).set(array, index, value);
    }

    /**
     * Gets a char value from an array
     *
     * @param array the array to access
     * @param index
     * @return the value of the array at the index
     */
    public static char getChar(Object array, int index) {
        return ArrayHandlerFactory.getAccessorInstance(CharArrayAccessor.class, typeChecked(array)).get(array, index);
    }

    /**
     * Sets a boolean value on an array
     *
     * @param array the array on which the value is set
     * @param index the array index
     * @param value the value to set
     */
    public static void setBoolean(Object array, int index, boolean value) {
        ArrayHandlerFactory.getAccessorInstance(BooleanArrayAccessor.class, typeChecked(array)).set(array, index, value);
    }

    /**
     * Gets a boolean value from an array
     *
     * @param array the array to access
     * @param index
     * @return the value of the array at the index
     */
    public static boolean getBoolean(Object array, int index) {
        return ArrayHandlerFactory.getAccessorInstance(BooleanArrayAccessor.class, typeChecked(array)).get(array, index);
    }

    /*
     * Only checks if object is an array, not the type of that array. TODO
     */
    private static Class<?> typeChecked(Object array) {
        if (array == null) {
            throw new NullPointerException("Argument is null");
        }
        Class<?> arrayType = array.getClass();
        if (!arrayType.isArray()) {
            throw new IllegalArgumentException("Argument is not an array");
        }
        return arrayType;
    }

}
