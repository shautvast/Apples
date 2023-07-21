package nl.sander.apples;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharsTest {

    @Test
    void nilEqualsNil() {
        assertTrue(Apples.compare((char) 0, (char) 0).areEqual());
    }

    @Test
    void nilNotEqualsSome() {
        assertEquals(Result.unequal("'A' != 'B'"),Apples.compare('A', 'B'));
    }

    @Test
    void SomeNotEqualsNil() {
        assertEquals(Result.unequal("'\\u0001' != '\\u0000'"),Apples.compare((char) 1, (char) 0));
    }

    @Test
    void charEqualsCharacter() {
        assertTrue(Apples.compare(Character.valueOf('X'), 'X').areEqual());
    }

    @Test
    void nilNotEqualsOneCharacter() {
        assertEquals(Result.unequal("'\\u0000' != '\\u0001'"),Apples.compare((char) 0, Character.valueOf((char) 1)));
    }

    @Test
    void nullNotEqualsNilCharacter() {
        assertEquals(Result.unequal("null != '\\u0000'"),Apples.compare(null, Character.valueOf((char) 0)));
    }

    @Test
    void nullNotEqualsChar() {
        assertEquals(Result.unequal("null != '\\u0000'"),Apples.compare(null, (char) 0));
    }

    @Test
    void nilCharacterNotEqualsNull() {
        assertEquals(Result.unequal("'\\u0000' != null"),Apples.compare(Character.valueOf((char) 0), null));
    }

    @Test
    void charNotEqualsNull() {
        assertEquals(Result.unequal("'\\u0000' != null"),Apples.compare((char) 0, null));
    }

    @Test
    void charNotEqualsString() {
        assertEquals(Result.unequal("'\\u0000' != \"true\""),Apples.compare((char)0, "true"));
    }

    @Test
    void StringNotEqualsChar() {
        assertEquals(Result.unequal("\"false\" != '\\u0000'"),Apples.compare("false", (char)0));
    }
}
