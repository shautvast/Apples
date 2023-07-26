package nl.sander.reflective.compare;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BooleansTest {

    @Test
    void falseEqualsFalse() {
        assertTrue(Compare.compare(false, false).areEqual());
    }

    @Test
    void falseEqualsBooleanFalse() {
        assertTrue(Compare.compare(false, Boolean.FALSE).areEqual());
    }

    @Test
    void trueEqualsBooleanTrue() {
        assertTrue(Compare.compare(true, Boolean.TRUE).areEqual());
    }

    @Test
    void trueEqualsTrue() {
        assertTrue(Compare.compare(true, true).areEqual());
    }

    @Test
    void falseNotEqualsTrue() {
        assertEquals(Result.unequal("false != true"), Compare.compare(false, true));
    }

    @Test
    void trueNotEqualsFalse() {
        assertEquals(Result.unequal("true != false"), Compare.compare(true, false));
    }


    @Test
    void falseNotEqualsTrueBoolean() {
        assertEquals(Result.unequal("false != java.lang.Boolean: true"), Compare.compare(false, Boolean.valueOf(true)));
    }

    @Test
    void trueNotEqualsFalseBoolean() {
        assertEquals(Result.unequal("true != java.lang.Boolean: false"), Compare.compare(true, Boolean.valueOf(false)));
    }

    @Test
    void falseBooleanNotEqualsTrue() {
        assertEquals(Result.unequal("java.lang.Boolean: false != true"), Compare.compare(Boolean.valueOf(false), true));
    }

    @Test
    void trueBooleanNotEqualsFalse() {
        assertEquals(Result.unequal("java.lang.Boolean: true != false"), Compare.compare(Boolean.valueOf(true), false));
    }

    @Test
    void nullNotEqualsTrue() {
        assertEquals(Result.unequal("null != true"), Compare.compare(null, true));
    }

    @Test
    void nullNotEqualsFalse() {
        assertEquals(Result.unequal("null != false"), Compare.compare(null, false));
    }

    @Test
    void trueNotEqualsNull() {
        assertEquals(Result.unequal("true != null"), Compare.compare(true, null));
    }

    @Test
    void falseNotEqualsNull() {
        assertEquals(Result.unequal("false != null"), Compare.compare(false, null));
    }

    @Test
    void trueNotEqualsString() {
        assertEquals(Result.unequal("true != \"true\""), Compare.compare(true, "true"));
    }

    @Test
    void StringNotEqualsFalse() {
        assertEquals(Result.unequal("\"false\" != false"), Compare.compare("false", false));
    }

    @Test
    void nullNotEqualsTrueBoolean() {
        assertEquals(Result.unequal("null != java.lang.Boolean: true"), Compare.compare(null, Boolean.TRUE));
    }

    @Test
    void nullNotEqualsFalseBoolean() {
        assertEquals(Result.unequal("null != java.lang.Boolean: false"), Compare.compare(null, Boolean.FALSE));
    }

    @Test
    void trueBooleanNotEqualsNull() {
        assertEquals(Result.unequal("java.lang.Boolean: true != null"), Compare.compare(Boolean.TRUE, null));
    }

    @Test
    void falseBooleanNotEqualsNull() {
        assertEquals(Result.unequal("java.lang.Boolean: false != null"), Compare.compare(Boolean.FALSE, null));
    }


}
