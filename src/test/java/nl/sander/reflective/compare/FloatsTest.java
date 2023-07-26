package nl.sander.reflective.compare;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FloatsTest {

    @Test
    void floatImprecisionLeft() {
        assertTrue(Compare.compare(2 / 3F, 0.66F, 2).areEqual(), (2 / 3F) + " != " + 0.66F);
    }

    @Test
    void floatImprecisionRight() {
        assertTrue(Compare.compare(0.66F, 2 / 3F, 2).areEqual(), (2 / 3F) + " != " + 0.66F);
    }

    @Test
    void nilFEqualsNilF() {
        assertTrue(Compare.compare(0F, 0F).areEqual());
    }

    @Test
    void nilFEqualsNilFloat() {
        assertTrue(Compare.compare(0F, Float.valueOf(0)).areEqual());
    }

    @Test
    void nilFEqualsNilL() {
        assertTrue(Compare.compare(0F, 0L).areEqual());
    }

    @Test
    void nilEqualsNilF() {
        assertTrue(Compare.compare(0L, 0F).areEqual());
    }

    @Test
    void nullNotEqualsSome() {
        assertEquals(Result.unequal("0.0 != 1.0"), Compare.compare(0F, 1F));
    }

    @Test
    void SomeNotEqualsNull() {
        assertEquals(Result.unequal("1.0 != 0.0"), Compare.compare(1F, 0F));
    }

    @Test
    void nilByteNotEqualsOne() {
        assertEquals(Result.unequal("java.lang.Float: 0.0 != 1.0"), Compare.compare(Float.valueOf(0), 1F));
    }

    @Test
    void nilNotEqualsOneByte() {
        assertEquals(Result.unequal("0.0 != java.lang.Float: 1.0"), Compare.compare(0F, Float.valueOf(1)));
    }

    @Test
    void nullNotEqualsNilFloat() {
        assertEquals(Result.unequal("null != java.lang.Float: 0.0"), Compare.compare(null, Float.valueOf(0)));
    }

    @Test
    void nullNotEqualsNil() {
        assertEquals(Result.unequal("null != 0.0"), Compare.compare(null, 0F));
    }

    @Test
    void nilFloatNotEqualsNull()  {
        assertEquals(Result.unequal("java.lang.Float: 0.0 != null"), Compare.compare(Float.valueOf(0), null));
    }

    @Test
    void nilNotEqualsNull() {
        assertEquals(Result.unequal("0.0 != null"), Compare.compare(0F, null));
    }

    @Test
    void floatNotEqualsString() {
        assertEquals(Result.unequal("0.0 != \"true\""), Compare.compare(0F, "true"));
    }

    @Test
    void StringNotEqualsFloat() {
        assertEquals(Result.unequal("\"false\" != 0.0"), Compare.compare("false", 0F));
    }
}
