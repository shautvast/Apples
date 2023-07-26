package nl.sander.reflective.compare;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DifferentTypesTest {
    @Test
    void testAppleAndOrange() {
        assertTrue(Compare.any(new Apple("orange"), new Orange("orange")).areEqual());
    }

    static class Apple {
        final String color;

        Apple(String color) {
            this.color = color;
        }
    }

    static class Orange {
        final String color;

        Orange(String color) {
            this.color = color;
        }
    }
}
