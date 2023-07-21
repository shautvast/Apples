package nl.sander.apples;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoublesTest {

    @Test
    void floatImprecisionLeft() {
        assertTrue(Apples.compare(2 / 3.0, 0.66, 2).areEqual(), (2 / 3) + " != " + 0.66);
    }

    @Test
    void floatImprecisionRight() {
        assertTrue(Apples.compare(0.66, 2 / 3D, 2).areEqual(), (2 / 3D) + " != " + 0.66);
    }

    @Test
    void nilFEqualsNilF() {
        assertTrue(Apples.compare(0.0, .0).areEqual());
    }

    @Test
    void nilFEqualsNilL() {
        assertTrue(Apples.compare(0.0, 0L).areEqual());
    }

    @Test
    void nilEqualsNilF() {
        assertTrue(Apples.compare(0L, 0.0).areEqual());
    }

    @Test
    void nullNotEqualsSome() {
        assertEquals(Result.unequal("0.0 != 1.0"), Apples.compare(0.0, 1.0F));
    }

    @Test
    void SomeNotEqualsNull() {
        assertEquals(Result.unequal("1.0 != 0.0"), Apples.compare(1.0, .0));
    }

    @Test
    void nilDoubleNotEqualsOne() {
        assertEquals(Result.unequal("java.lang.Double: 0.0 != 1.0"), Apples.compare(Double.valueOf(0), 1F));
    }

    @Test
    void nilNotEqualsOneDouble() {
        assertEquals(Result.unequal("0.0 != java.lang.Double: 1.0"), Apples.compare(0F, Double.valueOf(1)));
    }

    @Test
    void nullNotEqualsNilDouble() {
        assertEquals(Result.unequal("null != java.lang.Double: 0.0"), Apples.compare(null, Double.valueOf(0)));
    }

    @Test
    void nullNotEqualsNil() {
        assertEquals(Result.unequal("null != 0.0"), Apples.compare(null, .0));
    }

    @Test
    void nilDoubleNotEqualsNull() {
        assertEquals(Result.unequal("java.lang.Double: 0.0 != null"), Apples.compare(Double.valueOf(0), null));
    }


    @Test
    void nilNotEqualsNull() {
        assertEquals(Result.unequal("0.0 != null"), Apples.compare(.0, null));
    }

    @Test
    void doubleNotEqualsString() {
        assertEquals(Result.unequal("0.0 != \"0\""), Apples.compare(.0, "0"));
    }

    @Test
    void StringNotEqualsDouble() {
        assertEquals(Result.unequal("\"0\" != 0.0"), Apples.compare("0", .0));
    }
}
