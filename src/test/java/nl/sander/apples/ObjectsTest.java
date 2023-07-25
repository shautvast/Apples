package nl.sander.apples;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

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
    void differentClass() {
        assertEquals(Result.unequal("\"1\" != java.lang.Integer: 1"), Apples.compare("1", Integer.valueOf(1)));
    }

    @Test
    void sameKeysAndValues() {
        assertTrue(Apples.compare("map", Map.of("a", 1, "b", 2), Map.of("b", 2, "a", 1)).areEqual());
    }

    @Test
    void differentKeysAndValues() {
        Result result = Apples.compare("map", Map.of("a", 2, "b", 1), Map.of("b", 2, "a", 1));
        assertFalse(result.areEqual());
        assertTrue(result.getDiffs().contains("for map[b]: 1 != 2"));
        assertTrue(result.getDiffs().contains("for map[a]: 2 != 1"));
    }

    @Test
    void bigDecimals() {
        Result result = Apples.compare(BigDecimal.valueOf(0), BigDecimal.valueOf(1));
        assertFalse(result.areEqual());
        assertEquals("0 != 1", result.getDiffs().get(0));
    }

    @Test
    void enums() {
        Result result = Apples.compare(Storage.HIGH, Storage.LOW);
        assertFalse(result.areEqual());
        assertEquals("HIGH != LOW", result.getDiffs().get(0));
    }
}
