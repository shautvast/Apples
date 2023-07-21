package nl.sander.apples;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class RecordsTest {

    @Test
    void testExample() {
        Result comparison = new PlumApple().compare(new Plum("small", "red",true, 1, 1.0F,Storage.HIGH),
                new Plum("large", "green",true, 1, 1.0F,Storage.HIGH));

        assertFalse(comparison.areEqual());
        assertFalse(comparison.diffs().isEmpty());
        assertEquals(2, comparison.diffs().size());
        assertEquals("\"small\" != \"large\"", comparison.diffs().get(0));
        assertEquals("\"red\" != \"green\"", comparison.diffs().get(1));
    }

    @Test
    void testRecords() {
        Result comparison = Apples.compare(new Plum("small", "red",true, 1, 1.0F,Storage.HIGH),
                new Plum("large", "green",true, 1, 1.0F,Storage.HIGH));

        assertFalse(comparison.areEqual());
        assertFalse(comparison.diffs().isEmpty());
        assertEquals(2, comparison.diffs().size());
        assertEquals("\"small\" != \"large\"", comparison.diffs().get(0));
        assertEquals("\"red\" != \"green\"", comparison.diffs().get(1));
    }
}
