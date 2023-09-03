package com.github.shautvast.reflective.array;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ArrayFactoryTest {

    @Test
    void testCreate1DimShortArray() {
        Object o = ArrayFactory.newInstance(short.class, 1);
        assertTrue(o instanceof short[]);
    }

    @Test
    void testCreate2DimIntArray() {
        int[][] ints = (int[][]) ArrayFactory.newInstance(int[].class, 1);
        assertEquals(1, ints.length);
        ints[0] = new int[1]; // will fail if array is not correctly created
        assertEquals(1, ints[0].length);
    }

    @Test
    void testCreate3DimStringArray() {
        String[][][] array = (String[][][]) ArrayFactory.newInstance(String.class, 6, 7, 8);
        assertEquals(6, array.length);
        assertEquals(7, array[0].length);
        assertEquals(8, array[0][0].length);
    }

    @Test
    void testSetObject() {
        String[] strings = new String[1];
        ArrayFactory.set(strings, 0, "helloworld");
        assertArrayEquals(new String[]{"helloworld"}, strings);
    }

    @Test
    void testSetInt() {
        int[] ints = new int[1];
        ArrayFactory.setInt(ints, 0, 11);
        assertArrayEquals(new int[]{11}, ints);
    }

    @Test
    void testSetByte() {
        byte[] bytes = new byte[1];
        ArrayFactory.setByte(bytes, 0, (byte) 11);
        assertArrayEquals(new byte[]{11}, bytes);
    }

    @Test
    void testSetShort() {
        short[] shorts = new short[1];
        ArrayFactory.setShort(shorts, 0, (short) 11);
        assertArrayEquals(new short[]{11}, shorts);
    }

    @Test
    void testSetLong() {
        long[] longs = new long[1];
        ArrayFactory.setLong(longs, 0, 11L);
        assertArrayEquals(new long[]{11}, longs);
    }

    @Test
    void testSetFloat() {
        float[] floats = new float[1];
        ArrayFactory.setFloat(floats, 0, 11.1F);
        assertArrayEquals(new float[]{11.1F}, floats);
    }

    @Test
    void testSetDouble() {
        double[] doubles = new double[1];
        ArrayFactory.setDouble(doubles, 0, 11.1D);
        assertArrayEquals(new double[]{11.1D}, doubles);
    }

    @Test
    void testSetBoolean() {
        boolean[] booleans = new boolean[]{false};
        ArrayFactory.setBoolean(booleans, 0, true);
        assertArrayEquals(new boolean[]{true}, booleans);
    }

    @Test
    void testSetChar() {
        char[] chars = new char[]{'C'};
        ArrayFactory.setChar(chars, 0, 'D');
        assertArrayEquals(new char[]{'D'}, chars);
    }

    @Test
    void testGetFromObjectArray() {
        String[] strings = new String[]{"helloworld"};
        Object o = ArrayFactory.get(strings, 0);
        assertTrue(o instanceof String);
        String string = (String) o;
        assertEquals("helloworld", string);
    }

    @Test
    void testGetFromByteArray() {
        byte[] bytes = new byte[]{17};
        byte b = ArrayFactory.getByte(bytes, 0);
        assertEquals(17, b);
    }

    @Test
    void testGetFromShortArray() {
        short[] shorts = new short[]{17};
        short s = ArrayFactory.getShort(shorts, 0);
        assertEquals(17, s);
    }

    @Test
    void testGetFromIntArray() {
        int[] ints = new int[]{17};
        int i = ArrayFactory.getInt(ints, 0);
        assertEquals(17, i);
    }

    @Test
    void testGetFromLongArray() {
        long[] longs = new long[]{17};
        long l = ArrayFactory.getLong(longs, 0);
        assertEquals(17, l);
    }

    @Test
    void testGetFromCharArray() {
        char[] chars = new char[]{17};
        char c = ArrayFactory.getChar(chars, 0);
        assertEquals(17, c);
    }

    @Test
    void testGetFromBooleanArray() {
        boolean[] booleans = new boolean[]{true};
        boolean b = ArrayFactory.getBoolean(booleans, 0);
        assertEquals(true, b);
    }

    @Test
    void testGetFromFloatArray() {
        float[] floats = new float[]{17.5F};
        float f = ArrayFactory.getFloat(floats, 0);
        assertEquals(17.5, f);
    }

    @Test
    void testGetFromDoubleArray() {
        double[] doubles = new double[]{17.5F};
        double d = ArrayFactory.getDouble(doubles, 0);
        assertEquals(17.5, d);
    }

    @Test
    void arrayIndexOutOfBoundsException() {
        double[] doubles = new double[]{};
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayFactory.getDouble(doubles, 0));
    }

    @Test
    void notAnArray() {
        assertThrows(IllegalArgumentException.class, () -> ArrayFactory.get("foo", 0));
    }

    @Test
    void arrayIsNull() {
        assertThrows(NullPointerException.class, () -> ArrayFactory.get(null, 0));
    }
}