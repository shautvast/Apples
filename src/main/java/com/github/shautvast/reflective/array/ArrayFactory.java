package com.github.shautvast.reflective.array;

import com.github.shautvast.reflective.array.base.*;
import com.github.shautvast.reflective.java.Java;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Factory class for dynamically working with arrays
 */
public class ArrayFactory {

    private static final Map<String, ArrayCreator> creatorCache = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Map<String, Object>> setterCache = new ConcurrentHashMap<>();

    /**
     * Creates a new array of the specified type and dimensions
     *
     * @param elementType the array element type
     * @param dimensions  array of ints, ie {10} means 1 dimension, length 10. {10,20} means 2 dimensions, size 10 by 20
     * @return an object that you can cast to the expected array type
     */
    public static Object newInstance(Class<?> elementType, int... dimensions) {
        return getCreatorInstance(elementType, dimensions).newInstance();
    }

    /**
     * Sets an Object value on an array
     * @param array the array on which the value is set
     * @param index the array index
     * @param value the value to set
     */
    public static void set(Object array, int index, Object value) {
        getSetterInstance(ObjectArraySetter.class, typeChecked(array)).set(array, index, value);
    }

    /**
     * Sets an int value on an array
     * @param array the array on which the value is set
     * @param index the array index
     * @param value the value to set
     */
    public static void set(Object array, int index, int value) {
        getSetterInstance(IntArraySetter.class, typeChecked(array)).set(array, index, value);
    }

    /**
     * Sets a byte value on an array
     * @param array the array on which the value is set
     * @param index the array index
     * @param value the value to set
     */
    public static void set(Object array, int index, byte value) {
        getSetterInstance(ByteArraySetter.class, typeChecked(array)).set(array, index, value);
    }

    /**
     * Sets a short value on an array
     *
     * @param array the array on which the value is set
     * @param index the array index
     * @param value the value to set
     */
    public static void set(Object array, int index, short value) {
        getSetterInstance(ShortArraySetter.class, typeChecked(array)).set(array, index, value);
    }

    /**
     * Sets a long value on an array
     *
     * @param array the array on which the value is set
     * @param index the array index
     * @param value the value to set
     */
    public static void set(Object array, int index, long value) {
        getSetterInstance(LongArraySetter.class, typeChecked(array)).set(array, index, value);
    }

    /**
     * Sets a byte value on an array
     *
     * @param array the array on which the value is set
     * @param index the array index
     * @param value the value to set
     */
    public static void set(Object array, int index, float value) {
        getSetterInstance(FloatArraySetter.class, typeChecked(array)).set(array, index, value);
    }

    /**
     * Sets a double value on an array
     *
     * @param array the array on which the value is set
     * @param index the array index
     * @param value the value to set
     */
    public static void set(Object array, int index, double value) {
        getSetterInstance(DoubleArraySetter.class, typeChecked(array)).set(array, index, value);
    }

    public static void set(Object array, int index, char value) {
        getSetterInstance(CharArraySetter.class, typeChecked(array)).set(array, index, value);
    }

    public static void set(Object array, int index, boolean value) {
        getSetterInstance(BooleanArraySetter.class, typeChecked(array)).set(array, index, value);
    }

    private static Class<?> typeChecked(Object array) {
        Class<?> arrayType = array.getClass();
        if (!arrayType.isArray()) {
            throw new IllegalArgumentException("This is not an array");
        }
        return arrayType;
    }

    @SuppressWarnings("unchecked")
    private static <T> T getSetterInstance(Class<T> setterBaseType, Class<?> arrayType) {
        String arrayTypeName = Java.internalName(arrayType);
        String syntheticClassName = getSyntheticClassName(arrayType, arrayTypeName);

        return (T) setterCache.computeIfAbsent(setterBaseType, k -> new ConcurrentHashMap<>()).
                computeIfAbsent(syntheticClassName,
                        k -> AsmArrayFactory.createSyntheticArraySetter(setterBaseType, arrayTypeName, syntheticClassName));
    }

    private static String getSyntheticClassName(Class<?> arrayType, String arrayTypeName) {
        return "com/shautvast/reflective/array/ArraySetter_"
                + javaName(arrayTypeName) + Java.getNumDimensions(arrayType);
    }

    private static ArrayCreator getCreatorInstance(Class<?> elementType, int... dimensions) {
        String elementTypeName = Java.internalName(elementType);
        String syntheticClassName = "com/shautvast/reflective/array/ArrayCreator_"
                + javaName(elementTypeName) + dimensions.length;

        return creatorCache.computeIfAbsent(syntheticClassName,
                k -> AsmArrayFactory.createSyntheticArrayCreator(elementTypeName, syntheticClassName, dimensions));
    }

    private static String javaName(String arrayTypeName) {
        return arrayTypeName
                .replaceAll("[/.\\[;]", "")
                .toLowerCase();
    }
}
