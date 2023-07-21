package nl.sander.apples;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("ConstantValue")
class FloatsTest {

    @Test
    void floatImprecisionLeft() {
        assertTrue(Apples.compare(2 / 3F, 0.66F, 2).areEqual(), (2 / 3F) + " != " + 0.66F);
    }

    @Test
    void floatImprecisionRight() {
        assertTrue(Apples.compare(0.66F, 2 / 3F, 2).areEqual(), (2 / 3F) + " != " + 0.66F);
    }

    @Test
    void nilFEqualsNilF() {
        assertTrue(Apples.compare(0F, 0F).areEqual());
    }

    @Test
    void nilFEqualsNilFloat() {
        assertTrue(Apples.compare(0F, Float.valueOf(0)).areEqual());
    }

    @Test
    void nilFEqualsNilL() {
        assertTrue(Apples.compare(0F, 0L).areEqual());
    }

    @Test
    void nilEqualsNilF() {
        assertTrue(Apples.compare(0L, 0F).areEqual());
    }

    @Test
    void nullNotEqualsSome() {
        assertEquals(Result.unequal("0.0 != 1.0"), Apples.compare(0F, 1F));
    }

    @Test
    void SomeNotEqualsNull() {
        assertEquals(Result.unequal("1.0 != 0.0"), Apples.compare(1F, 0F));
    }

    @Test
    void nilByteNotEqualsOne() {
        assertEquals(Result.unequal("java.lang.Float: 0.0 != 1.0"), Apples.compare(Float.valueOf(0), 1F));
    }

    @Test
    void nilNotEqualsOneByte() {
        assertEquals(Result.unequal("0.0 != java.lang.Float: 1.0"), Apples.compare(0F, Float.valueOf(1)));
    }

    @Test
    void nullNotEqualsNilFloat() {
        assertEquals(Result.unequal("null != java.lang.Float: 0.0"), Apples.compare(null, Float.valueOf(0)));
    }

    @Test
    void nullNotEqualsNil() {
        assertEquals(Result.unequal("null != 0.0"), Apples.compare(null, 0F));
    }

    @Test
    void nilFloatNotEqualsNull() {
        assertEquals(Result.unequal("java.lang.Float: 0.0 != null"), Apples.compare(Float.valueOf(0), null));
    }

    @Test
    void nilNotEqualsNull() {
        assertEquals(Result.unequal("0.0 != null"), Apples.compare(0F, null));
    }

    @Test
    void floatNotEqualsString() {
        assertEquals(Result.unequal("0.0 != \"true\""), Apples.compare(0F, "true"));
    }

    @Test
    void StringNotEqualsFloat() {
        assertEquals(Result.unequal("\"false\" != 0.0"), Apples.compare("false", 0F));
    }
}
