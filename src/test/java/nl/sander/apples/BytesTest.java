package nl.sander.apples;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BytesTest {

    @Test
    void nilEqualsNil() {
        assertEquals(Result.SAME, Apples.compare((byte) 0, (byte) 0));
    }

    @Test
    void nilNotEqualsSome() {
        assertEquals(Result.unequal("0 != 1"), Apples.compare((byte) 0, (byte) 1));
    }

    @Test
    void OneNotEqualsNil() {
        assertEquals(Result.unequal("1 != 0"), Apples.compare((byte) 1, (byte) 0));
    }

    @Test
    void nilByteNotEqualsOne() {
        assertEquals(Result.unequal("java.lang.Byte: 0 != 1"), Apples.compare(Byte.valueOf((byte) 0), (byte) 1));
    }

    @Test
    void nilNotEqualsOneByte() {
        assertEquals(Result.unequal("0 != java.lang.Byte: 1"), Apples.compare((byte) 0, Byte.valueOf((byte) 1)));
    }

    @Test
    void nullNotEqualsNilByte() {
        assertEquals(Result.unequal("null != java.lang.Byte: 0"), Apples.compare(null, Byte.valueOf((byte) 0)));
    }

    @Test
    void nullNotEqualsNil() {
        assertEquals(Result.unequal("null != 0"), Apples.compare(null, (byte) 0));
    }

    @Test
    void nilByteNotEqualsNull() {
        assertEquals(Result.unequal("java.lang.Byte: 0 != null"), Apples.compare(Byte.valueOf((byte) 0), null));
    }

    @Test
    void nilNotEqualsNull() {
        assertEquals(Result.unequal("0 != null"), Apples.compare((byte) 0, null));
    }

    @Test
    void byteNotEqualsString() {
        assertEquals(Result.unequal("0 != \"true\""), Apples.compare((byte) 0, "true"));
    }

    @Test
    void StringNotEqualsByte() {
        assertEquals(Result.unequal("\"false\" != 0"), Apples.compare("false", (byte) 0));
    }
}
