package nl.sander.apples;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LongsTest {

    @Test
    void nullEqualsNull() {
        assertEquals(Result.SAME, Apples.compare(0, 0));
    }

    @Test
    void nullNotEqualsSome() {
        assertEquals(Result.unequal("0 != 1"), Apples.compare(0, 1));
    }

    @Test
    void SomeNotEqualsNull() {
        assertEquals(Result.unequal("1 != 0"), Apples.compare(1, 0));
    }

    @Test
    void nilLongNotEqualsOne() {
        assertEquals(Result.unequal("java.lang.Long: 0 != 1"), Apples.compare(Long.valueOf(0), 1));
    }

    @Test
    void nilNotEqualsOneLong() {
        assertEquals(Result.unequal("0 != java.lang.Long: 1"), Apples.compare(0, Long.valueOf(1)));
    }

    @Test
    void nilEqualsNilLong() {
        assertTrue(Apples.compare(0, Long.valueOf(0)).areEqual());
    }

    @Test
    void nullNotEqualsNilLong() {
        assertEquals(Result.unequal("null != java.lang.Long: 0"), Apples.compare(null, Long.valueOf(0)));
    }

    @Test
    void nullNotEqualsNil() {
        assertEquals(Result.unequal("null != 0"), Apples.compare(null, 0));
    }

    @Test
    void nilLongNotEqualsNull() {
        assertEquals(Result.unequal("java.lang.Long: 0 != null"), Apples.compare(Long.valueOf(0), null));
    }

    @Test
    void nilNotEqualsNull() {
        assertEquals(Result.unequal("0 != null"), Apples.compare(0, null));
    }

    @Test
    void longNotEqualsString() {
        assertEquals(Result.unequal("0 != \"0\""), Apples.compare(0, "0"));
    }

    @Test
    void StringNotEqualsLong() {
        assertEquals(Result.unequal("\"false\" != 0"), Apples.compare("false", 0));
    }
}
