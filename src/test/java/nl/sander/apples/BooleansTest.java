package nl.sander.apples;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BooleansTest {

    @Test
    void falseEqualsFalse() {
        assertTrue(Apples.compare(false, false).areEqual());
    }

    @Test
    void falseEqualsBooleanFalse() {
        assertTrue(Apples.compare(false, Boolean.FALSE).areEqual());
    }

    @Test
    void trueEqualsBooleanTrue() {
        assertTrue(Apples.compare(true, Boolean.TRUE).areEqual());
    }

    @Test
    void trueEqualsTrue() {
        assertTrue(Apples.compare(true, true).areEqual());
    }

    @Test
    void falseNotEqualsTrue() {
        assertEquals(Result.unequal("false != true"), Apples.compare(false, true));
    }

    @Test
    void trueNotEqualsFalse() {
        assertEquals(Result.unequal("true != false"), Apples.compare(true, false));
    }


    @Test
    void falseNotEqualsTrueBoolean() {
        assertEquals(Result.unequal("false != java.lang.Boolean: true"), Apples.compare(false, Boolean.valueOf(true)));
    }

    @Test
    void trueNotEqualsFalseBoolean() {
        assertEquals(Result.unequal("true != java.lang.Boolean: false"), Apples.compare(true, Boolean.valueOf(false)));
    }

    @Test
    void falseBooleanNotEqualsTrue() {
        assertEquals(Result.unequal("java.lang.Boolean: false != true"), Apples.compare(Boolean.valueOf(false), true));
    }

    @Test
    void trueBooleanNotEqualsFalse() {
        assertEquals(Result.unequal("java.lang.Boolean: true != false"), Apples.compare(Boolean.valueOf(true), false));
    }

    @Test
    void nullNotEqualsTrue() {
        assertEquals(Result.unequal("null != true"), Apples.compare(null, true));
    }

    @Test
    void nullNotEqualsFalse() {
        assertEquals(Result.unequal("null != false"), Apples.compare(null, false));
    }

    @Test
    void trueNotEqualsNull() {
        assertEquals(Result.unequal("true != null"), Apples.compare(true, null));
    }

    @Test
    void falseNotEqualsNull() {
        assertEquals(Result.unequal("false != null"), Apples.compare(false, null));
    }

    @Test
    void trueNotEqualsString() {
        assertEquals(Result.unequal("true != \"true\""), Apples.compare(true, "true"));
    }

    @Test
    void StringNotEqualsFalse() {
        assertEquals(Result.unequal("\"false\" != false"), Apples.compare("false", false));
    }

    @Test
    void nullNotEqualsTrueBoolean() {
        assertEquals(Result.unequal("null != java.lang.Boolean: true"), Apples.compare(null, Boolean.TRUE));
    }

    @Test
    void nullNotEqualsFalseBoolean() {
        assertEquals(Result.unequal("null != java.lang.Boolean: false"), Apples.compare(null, Boolean.FALSE));
    }

    @Test
    void trueBooleanNotEqualsNull() {
        assertEquals(Result.unequal("java.lang.Boolean: true != null"), Apples.compare(Boolean.TRUE, null));
    }

    @Test
    void falseBooleanNotEqualsNull() {
        assertEquals(Result.unequal("java.lang.Boolean: false != null"), Apples.compare(Boolean.FALSE, null));
    }


}
