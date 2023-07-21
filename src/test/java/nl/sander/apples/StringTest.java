package nl.sander.apples;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StringTest {

    @Test
    void test() {
        assertTrue(Apples.compare("left", "left").areEqual());
    }
}
