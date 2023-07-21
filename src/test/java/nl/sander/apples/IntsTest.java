package nl.sander.apples;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntsTest {

    @Test
    void nilEqualsNil() {
        assertEquals(Result.SAME, Apples.compare(0, 0));
    }

    @Test
    void nilEqualsNilInteger() {
        assertEquals(Result.SAME, Apples.compare(0, Integer.valueOf(0)));
    }

    @Test
    void nilIntegerEqualsNil() {
        assertEquals(Result.SAME, Apples.compare(Integer.valueOf(0), 0));
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
    void nilByteNotEqualsOne() {
        assertEquals(Result.unequal("java.lang.Integer: 0 != 1"), Apples.compare(Integer.valueOf(0), 1));
    }

    @Test
    void nilNotEqualsOneByte() {
        assertEquals(Result.unequal("0 != java.lang.Integer: 1"), Apples.compare(0, Integer.valueOf(1)));
    }

    @Test
    void nullNotEqualsNilByte() {
        assertEquals(Result.unequal("null != java.lang.Integer: 0"), Apples.compare(null, Integer.valueOf(0)));
    }

    @Test
    void nullNotEqualsNil() {
        assertEquals(Result.unequal("null != 0"), Apples.compare(null, 0));
    }

    @Test
    void nilByteNotEqualsNull() {
        assertEquals(Result.unequal("java.lang.Integer: 0 != null"), Apples.compare(Integer.valueOf(0), null));
    }

    @Test
    void nilNotEqualsNull() {
        assertEquals(Result.unequal("0 != null"), Apples.compare(0, null));
    }

    @Test
    void intNotEqualsString() {
        assertEquals(Result.unequal("0 != \"true\""), Apples.compare(0, "true"));
    }

    @Test
    void StringNotEqualsInt() {
        assertEquals(Result.unequal("\"0\" != 0"), Apples.compare("0", 0));
    }
}
