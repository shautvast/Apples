package com.github.shautvast.reflective.compare;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StringTest {

    @Test
    void test()  {
        assertTrue(Compare.compare("left", "left").areEqual());
    }
}
