package nl.sander.apples;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShortsTest {

    @Test
    void nullEqualsNull() {
        assertTrue(Apples.compare((short) 0, (short) 0).areEqual());
    }

    @Test
    void nullEqualsNullShort() {
        assertTrue(Apples.compare((short) 0, Short.valueOf((short) 0)).areEqual());
    }

    @Test
    void nullShortEqualsNull() {
        assertTrue(Apples.compare(Short.valueOf((short) 0), (short) 0).areEqual());
    }

    @Test
    void nullNotEqualsSome() {
        assertEquals(Result.unequal("0 != 1"), Apples.compare((short) 0, (short) 1));
    }

    @Test
    void SomeNotEqualsNull() {
        assertEquals(Result.unequal("1 != 0"), Apples.compare((short) 1, (short) 0));
    }

    @Test
    void nilshortNotEqualsOne() {
        assertEquals(Result.unequal("java.lang.Short: 0 != 1"), Apples.compare(Short.valueOf((short) 0), (short) 1));
    }

    @Test
    void nilNotEqualsOneshort() {
        assertEquals(Result.unequal("0 != java.lang.Short: 1"), Apples.compare((short) 0, Short.valueOf((short) 1)));
    }

    @Test
    void nullNotEqualsNilshort() {
        assertEquals(Result.unequal("null != java.lang.Short: 0"), Apples.compare(null, Short.valueOf((short) 0)));
    }

    @Test
    void nullNotEqualsNil() {
        assertEquals(Result.unequal("null != 0"), Apples.compare(null, (short) 0));
    }

    @Test
    void nilshortNotEqualsNull() {
        assertEquals(Result.unequal("java.lang.Short: 0 != null"), Apples.compare(Short.valueOf((short) 0), null));
    }

    @Test
    void nilNotEqualsNull() {
        assertEquals(Result.unequal("0 != null"), Apples.compare((short) 0, null));
    }

    @Test
    void shortNotEqualsString() {
        assertEquals(Result.unequal("0 != \"true\""), Apples.compare((short) 0, "true"));
    }

    @Test
    void StringNotEqualsshort() {
        assertEquals(Result.unequal("\"false\" != 0"), Apples.compare("false", (short) 0));
    }
}
