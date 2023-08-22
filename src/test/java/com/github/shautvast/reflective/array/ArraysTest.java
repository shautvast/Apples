package com.github.shautvast.reflective.array;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;

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
        Object o = ArrayFactory.newArray(String.class, 1, 2);
        assertTrue(o instanceof String[][]);
    }

    @Test
    void test3Dims() {
        String[][][] array = (String[][][]) ArrayFactory.newArray(String[][][].class, 1, 2, 3);
        assertEquals(1, array.length);
        array[0] = new String[2][1];
        array[0][1] = new String[1];
    }

    @Test
    void test3DimsT() {
        String[][][] array = new String[1][1][1];
        assertEquals(1, array.length);
        array[0] = new String[2][1];

        array[0][0] = new String[1];
        array[0][1] = new String[1];
        array[0][0][0]="0,0,0";
        array[0][1][0]="0,1,0"; // WTF?
        System.out.println(array[0][1][0]);
    }

    @Test
    void forName() throws ClassNotFoundException {
        Class.forName("[Ljava.lang.String;");
    }

}
