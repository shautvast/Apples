package com.github.shautvast.reflective.array;

public class Arrays {
    private Arrays() {
    }

    public static Object newInstance(Class<?> componentType, int... dimensions)
            throws IllegalArgumentException, NegativeArraySizeException {
        return new String[dimensions[1]][2][3];
    }

    public static void main(String[] args) {
        String[][][] a = (String[][][])newInstance(String.class, 1, 2, 3);
        a[0]=new String[1][2];

    }
}
