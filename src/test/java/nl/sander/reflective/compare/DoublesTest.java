package nl.sander.reflective.compare;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoublesTest {

    @Test
    void floatImprecisionLeft() {
        assertTrue(Compare.compare(2 / 3.0, 0.66, 2).areEqual(), (2 / 3) + " != " + 0.66);
    }

    @Test
    void floatImprecisionRight() {
        assertTrue(Compare.compare(0.66, 2 / 3D, 2).areEqual(), (2 / 3D) + " != " + 0.66);
    }

    @Test
    void nilFEqualsNilF() {
        assertTrue(Compare.compare(0.0, .0).areEqual());
    }

    @Test
    void nilFEqualsNilL() {
        assertTrue(Compare.compare(0.0, 0L).areEqual());
    }

    @Test
    void nilEqualsNilF() {
        assertTrue(Compare.compare(0L, 0.0).areEqual());
    }

    @Test
    void nullNotEqualsSome() {
        assertEquals(Result.unequal("0.0 != 1.0"), Compare.compare(0.0, 1.0F));
    }

    @Test
    void SomeNotEqualsNull() {
        assertEquals(Result.unequal("1.0 != 0.0"), Compare.compare(1.0, .0));
    }

    @Test
    void nilDoubleNotEqualsOne() {
        assertEquals(Result.unequal("java.lang.Double: 0.0 != 1.0"), Compare.compare(Double.valueOf(0), 1F));
    }

    @Test
    void nilNotEqualsOneDouble() {
        assertEquals(Result.unequal("0.0 != java.lang.Double: 1.0"), Compare.compare(0F, Double.valueOf(1)));
    }

    @Test
    void nullNotEqualsNilDouble() {
        assertEquals(Result.unequal("null != java.lang.Double: 0.0"), Compare.compare(null, Double.valueOf(0)));
    }

    @Test
    void nullNotEqualsNil() {
        assertEquals(Result.unequal("null != 0.0"), Compare.compare(null, .0));
    }

    @Test
    void nilDoubleNotEqualsNull() {
        assertEquals(Result.unequal("java.lang.Double: 0.0 != null"), Compare.compare(Double.valueOf(0), null));
    }


    @Test
    void nilNotEqualsNull() {
        assertEquals(Result.unequal("0.0 != null"), Compare.compare(.0, null));
    }

    @Test
    void doubleNotEqualsString() {
        assertEquals(Result.unequal("0.0 != \"0\""), Compare.compare(.0, "0"));
    }

    @Test
    void StringNotEqualsDouble() {
        assertEquals(Result.unequal("\"0\" != 0.0"), Compare.compare("0", .0));
    }
}
