package com.github.shautvast.reflective;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReflectiveTest {

    @Test
    void test(){
        assertEquals("java.lang.String",Reflective.getMetaForClass(String.class).getName());
    }
}
