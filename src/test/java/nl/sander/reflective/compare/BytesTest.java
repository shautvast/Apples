package nl.sander.reflective.compare;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BytesTest {

    @Test
    void nilEqualsNil() {
        assertEquals(Result.SAME, Compare.compare((byte) 0, (byte) 0));
    }

    @Test
    void nilNotEqualsSome() {
        assertEquals(Result.unequal("0 != 1"), Compare.compare((byte) 0, (byte) 1));
    }

    @Test
    void OneNotEqualsNil() {
        assertEquals(Result.unequal("1 != 0"), Compare.compare((byte) 1, (byte) 0));
    }

    @Test
    void nilByteNotEqualsOne() {
        assertEquals(Result.unequal("java.lang.Byte: 0 != 1"), Compare.compare(Byte.valueOf((byte) 0), (byte) 1));
    }

    @Test
    void nilNotEqualsOneByte() {
        assertEquals(Result.unequal("0 != java.lang.Byte: 1"), Compare.compare((byte) 0, Byte.valueOf((byte) 1)));
    }

    @Test
    void nullNotEqualsNilByte() {
        assertEquals(Result.unequal("null != java.lang.Byte: 0"), Compare.compare(null, Byte.valueOf((byte) 0)));
    }

    @Test
    void nullNotEqualsNil() {
        assertEquals(Result.unequal("null != 0"), Compare.compare(null, (byte) 0));
    }

    @Test
    void nilByteNotEqualsNull() {
        assertEquals(Result.unequal("java.lang.Byte: 0 != null"), Compare.compare(Byte.valueOf((byte) 0), null));
    }

    @Test
    void nilNotEqualsNull() {
        assertEquals(Result.unequal("0 != null"), Compare.compare((byte) 0, null));
    }

    @Test
    void byteNotEqualsString() {
        assertEquals(Result.unequal("0 != \"true\""), Compare.compare((byte) 0, "true"));
    }

    @Test
    void StringNotEqualsByte() {
        assertEquals(Result.unequal("\"false\" != 0"), Compare.compare("false", (byte) 0));
    }
}
