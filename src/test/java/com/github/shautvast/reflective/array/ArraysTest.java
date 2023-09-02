package com.github.shautvast.reflective.array;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ArraysTest {

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
        ArrayFactory.set(ints, 0, 11);
        assertArrayEquals(new int[]{11}, ints);
    }

    @Test
    void testSetByte() {
        byte[] bytes = new byte[1];
        ArrayFactory.set(bytes, 0, (byte) 11);
        assertArrayEquals(new byte[]{11}, bytes);
    }

    @Test
    void testSetShort() {
        short[] shorts = new short[1];
        ArrayFactory.set(shorts, 0, (short) 11);
        assertArrayEquals(new short[]{11}, shorts);
    }

    @Test
    void testSetLong() {
        long[] longs = new long[1];
        ArrayFactory.set(longs, 0, 11L);
        assertArrayEquals(new long[]{11}, longs);
    }

    @Test
    void testSetFloat() {
        float[] floats = new float[1];
        ArrayFactory.set(floats, 0, 11.1F);
        assertArrayEquals(new float[]{11.1F}, floats);
    }

    @Test
    void testSetDouble() {
        double[] doubles = new double[1];
        ArrayFactory.set(doubles, 0, 11.1D);
        assertArrayEquals(new double[]{11.1D}, doubles);
    }

    @Test
    void testSetBoolean() {
        boolean[] booleans = new boolean[]{false};
        ArrayFactory.set(booleans, 0, true);
        assertArrayEquals(new boolean[]{true}, booleans);
    }

    @Test
    void testSetChar() {
        char[] chars = new char[]{'C'};
        ArrayFactory.set(chars, 0, 'D');
        assertArrayEquals(new char[]{'D'}, chars);
    }
}