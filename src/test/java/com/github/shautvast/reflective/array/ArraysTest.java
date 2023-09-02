package com.github.shautvast.reflective.array;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArraysTest {

    @Test
    void test1Dim() {
        Object o = ArrayFactory.newArray(String.class, 1);
        assertTrue(o instanceof String[]);
    }

    @Test
    void test2Dims() {
        int[][] ints = (int[][])ArrayFactory.newArray(int[].class, 1);
        assertEquals(1, ints.length);
        ints[0] = new int[1]; // will fail if array is not correctly created
        assertEquals(1, ints[0].length);
    }

    @Test
    void test3Dims() {
        String[][][] array = (String[][][]) ArrayFactory.newArray(String.class, 6, 7, 8);
        assertEquals(6, array.length);
        assertEquals(7, array[0].length);
        assertEquals(8, array[0][0].length);
    }
}
