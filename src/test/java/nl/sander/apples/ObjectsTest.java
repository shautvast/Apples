package nl.sander.apples;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObjectsTest {

    @Test
    void nullEqualsNull() {
        assertTrue(Apples.compare(null, null).areEqual());
    }

    @Test
    void nullNotEqualsSome() {
        assertEquals(Result.unequal("null != \"some\""), Apples.compare(null, "some"));
    }

    @Test
    void SomeNotEqualsNull() {
        assertEquals(Result.unequal("\"some\" != null"), Apples.compare("some", null));
    }

    @Test
    void differentClass(){
        assertEquals(Result.unequal("\"1\" != java.lang.Integer: 1"), Apples.compare("1", Integer.valueOf(1)));
    }
}
