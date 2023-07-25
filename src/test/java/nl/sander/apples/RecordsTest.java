/*
 * uncomment when in jdk16+
 */


//package nl.sander.apples;
//
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//
//class RecordsTest {
//
//    @Test
//    void testRecords() {
//        Result comparison = Apples.compare(new PlumRecord("small", "red", true, 1, 1.0F, Storage.HIGH, (byte) 1, List.of(new Shop("tesco"))),
//                new PlumRecord("large", "green", true, 1, 1.0F, Storage.LOW, (byte) 1, List.of(new Shop("asda"))));
//
//        assertFalse(comparison.areEqual());
//        assertFalse(comparison.getDiffs().isEmpty());
//        assertEquals(4, comparison.getDiffs().size());
//        assertEquals("for core: \"small\" != \"large\"", comparison.getDiffs().get(0));
//        assertEquals("for peel: \"red\" != \"green\"", comparison.getDiffs().get(1));
//        assertEquals("for storage: HIGH != LOW", comparison.getDiffs().get(2));
//        assertEquals("shops[0]:[Shop{name='tesco'}] != [Shop{name='asda'}]", comparison.getDiffs().get(3));
//    }
//
//
//}
//
