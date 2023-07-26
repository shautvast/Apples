package nl.sander.reflective.compare;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShortsTest {

    @Test
    void nullEqualsNull() {
        assertTrue(Compare.compare((short) 0, (short) 0).areEqual());
    }

    @Test
    void nullEqualsNullShort() {
        assertTrue(Compare.compare((short) 0, Short.valueOf((short) 0)).areEqual());
    }

    @Test
    void nullShortEqualsNull() {
        assertTrue(Compare.compare(Short.valueOf((short) 0), (short) 0).areEqual());
    }

    @Test
    void nullNotEqualsSome() {
        assertEquals(Result.unequal("0 != 1"), Compare.compare((short) 0, (short) 1));
    }

    @Test
    void SomeNotEqualsNull() {
        assertEquals(Result.unequal("1 != 0"), Compare.compare((short) 1, (short) 0));
    }

    @Test
    void nilshortNotEqualsOne() {
        assertEquals(Result.unequal("java.lang.Short: 0 != 1"), Compare.compare(Short.valueOf((short) 0), (short) 1));
    }

    @Test
    void nilNotEqualsOneshort() {
        assertEquals(Result.unequal("0 != java.lang.Short: 1"), Compare.compare((short) 0, Short.valueOf((short) 1)));
    }

    @Test
    void nullNotEqualsNilshort() {
        assertEquals(Result.unequal("null != java.lang.Short: 0"), Compare.compare(null, Short.valueOf((short) 0)));
    }

    @Test
    void nullNotEqualsNil() {
        assertEquals(Result.unequal("null != 0"), Compare.compare(null, (short) 0));
    }

    @Test
    void nilshortNotEqualsNull() {
        assertEquals(Result.unequal("java.lang.Short: 0 != null"), Compare.compare(Short.valueOf((short) 0), null));
    }

    @Test
    void nilNotEqualsNull() {
        assertEquals(Result.unequal("0 != null"), Compare.compare((short) 0, null));
    }

    @Test
    void shortNotEqualsString() {
        assertEquals(Result.unequal("0 != \"true\""), Compare.compare((short) 0, "true"));
    }

    @Test
    void StringNotEqualsshort() {
        assertEquals(Result.unequal("\"false\" != 0"), Compare.compare("false", (short) 0));
    }
}
