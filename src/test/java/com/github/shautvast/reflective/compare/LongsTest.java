package com.github.shautvast.reflective.compare;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LongsTest {

    @Test
    void nullEqualsNull() {
        assertEquals(Result.SAME, Compare.compare(0, 0));
    }

    @Test
    void nullNotEqualsSome() {
        assertEquals(Result.unequal("0 != 1"), Compare.compare(0, 1));
    }

    @Test
    void SomeNotEqualsNull() {
        assertEquals(Result.unequal("1 != 0"), Compare.compare(1, 0));
    }

    @Test
    void nilLongNotEqualsOne() {
        assertEquals(Result.unequal("java.lang.Long: 0 != 1"), Compare.compare(Long.valueOf(0), 1));
    }

    @Test
    void nilNotEqualsOneLong() {
        assertEquals(Result.unequal("0 != java.lang.Long: 1"), Compare.compare(0, Long.valueOf(1)));
    }

    @Test
    void nilEqualsNilLong() {
        assertTrue(Compare.compare(0, Long.valueOf(0)).areEqual());
    }

    @Test
    void nullNotEqualsNilLong() {
        assertEquals(Result.unequal("null != java.lang.Long: 0"), Compare.compare(null, Long.valueOf(0)));
    }

    @Test
    void nullNotEqualsNil() {
        assertEquals(Result.unequal("null != 0"), Compare.compare(null, 0));
    }

    @Test
    void nilLongNotEqualsNull() {
        assertEquals(Result.unequal("java.lang.Long: 0 != null"), Compare.compare(Long.valueOf(0), null));
    }

    @Test
    void nilNotEqualsNull() {
        assertEquals(Result.unequal("0 != null"), Compare.compare(0, null));
    }

    @Test
    void longNotEqualsString() {
        assertEquals(Result.unequal("0 != \"0\""), Compare.compare(0, "0"));
    }

    @Test
    void StringNotEqualsLong() {
        assertEquals(Result.unequal("\"false\" != 0"), Compare.compare("false", 0));
    }
}
