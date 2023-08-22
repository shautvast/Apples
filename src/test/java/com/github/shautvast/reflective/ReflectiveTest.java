package com.github.shautvast.reflective;

import com.github.shautvast.rusty.Panic;
import com.github.shautvast.rusty.Result;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ReflectiveTest {

    @Test
    void testMethods() {
        Dummy dummy = new Dummy((byte) 42, (short) 43, 44, 45, 46.0F, 47.0, 'D', true, "don't panic!");
        MetaClass metaDummy = Reflective.getMetaClass(dummy.getClass());
        assertEquals("com.github.shautvast.reflective.ReflectiveTest$Dummy", metaDummy.getName());

        Set<MetaMethod> methods = metaDummy.getMethods();
        assertFalse(methods.isEmpty());
        assertEquals(22, methods.size());

        MetaMethod equals = metaDummy.getMethod("equals").orElseGet(Assertions::fail);
        assertEquals(boolean.class, equals.getReturnParameter().getType());
        assertTrue(Modifier.isPublic(equals.getModifiers()));

        MetaMethod hashCode = metaDummy.getMethod("hashCode").orElseGet(Assertions::fail);
        assertEquals(List.of(), hashCode.getParameters());
        assertEquals(int.class, hashCode.getReturnParameter().getType());
        assertTrue(Modifier.isPublic(hashCode.getModifiers()));

        MetaMethod getStringValue = metaDummy.getMethod("getStringValue").orElseGet(Assertions::fail);
        assertEquals(List.of(), getStringValue.getParameters());
        assertEquals(String.class, getStringValue.getReturnParameter().getType());
        assertTrue(Modifier.isPublic(getStringValue.getModifiers()));

        MetaMethod privateMethod = metaDummy.getMethod("privateMethod").orElseGet(Assertions::fail);
        assertEquals(List.of(), privateMethod.getParameters());
        assertEquals(String[].class, privateMethod.getReturnParameter().getType());
        assertTrue(Modifier.isPrivate(privateMethod.getModifiers()));
    }


    @Test
    void testInvokeGetter() {
        Dummy dummy = new Dummy((byte) 42, (short) 43, 44, 45, 46.0F, 47.0, 'D', true, "don't panic!");
        MetaMethod getStringValue = Reflective.getMetaClass(dummy.getClass()).getMethod("getStringValue").orElseGet(Assertions::fail);

        // passing "foo" as the instance is not allowed
        assertThrows(Panic.class, () -> getStringValue.invoke("foo").unwrap());
        // we should pass a valid dummy instance
        assertEquals("don't panic!", getStringValue.invoke(dummy).unwrap());
    }

    @Test
    void testInvokeSetters() {
        Dummy dummy = new Dummy((byte) 42, (short) 43, 44, 45, 46.0F, 47.0, 'D', true, "don't panic!");
        MetaClass metaForClass = Reflective.getMetaClass(dummy.getClass());

        MetaMethod setByte = metaForClass.getMethod("setByteValue").orElseGet(Assertions::fail);
        setByte.invoke(dummy, (byte) -42).unwrap();
        assertEquals((byte) -42, dummy.getByteValue());

        MetaMethod setShort = metaForClass.getMethod("setShortValue").orElseGet(Assertions::fail);
        setShort.invoke(dummy, (short) -43).unwrap();
        assertEquals((short) -43, dummy.getShortValue());

        MetaMethod setInt = metaForClass.getMethod("setIntValue").orElseGet(Assertions::fail);
        setInt.invoke(dummy, -44).unwrap();
        assertEquals(-44, dummy.getIntValue());

        MetaMethod setLongValue = metaForClass.getMethod("setLongValue").orElseGet(Assertions::fail);
        setLongValue.invoke(dummy, -45L).unwrap();
        assertEquals(-45L, dummy.getLongValue());

        MetaMethod setFloat = metaForClass.getMethod("setFloatValue").orElseGet(Assertions::fail);
        setFloat.invoke(dummy, -46.0F).unwrap();
        assertEquals(-46.0F, dummy.getFloatValue());

        MetaMethod setDouble = metaForClass.getMethod("setDoubleValue").orElseGet(Assertions::fail);
        setDouble.invoke(dummy, -47.0).unwrap();
        assertEquals(-47.0, dummy.getDoubleValue());

        MetaMethod setChar = metaForClass.getMethod("setCharValue").orElseGet(Assertions::fail);
        setChar.invoke(dummy, '-').unwrap();
        assertEquals('-', dummy.getCharValue());

        MetaMethod setBoolean = metaForClass.getMethod("setBooleanValue").orElseGet(Assertions::fail);
        setBoolean.invoke(dummy, false).unwrap();
        assertFalse(dummy.isBooleanValue());

        MetaMethod setString = metaForClass.getMethod("setStringValue").orElseGet(Assertions::fail);
        setString.invoke(dummy, "panic!").unwrap();
        assertEquals("panic!", dummy.getStringValue());
    }

    @Test
    void testInvocationExceptionHappened() {
        Dummy dummy = new Dummy((byte) 42, (short) 43, 44, 45, 46.0F, 47.0, 'D', true, "don't panic!");
        MetaClass metaForClass = Reflective.getMetaClass(dummy.getClass());
        MetaMethod throwEx = metaForClass.getMethod("throwEx").orElseGet(Assertions::fail);

        Result<Void> result = throwEx.invoke(dummy, "foo");
        assertFalse(result.isOk());
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Dummy {
        private byte byteValue;
        private short shortValue;
        private int intValue;
        private long longValue;
        private float floatValue;
        private double doubleValue;
        private char charValue;
        private boolean booleanValue;
        private String stringValue;

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Dummy;
        }

        @Override
        public int hashCode() {
            return 6;
        }

        @SuppressWarnings("unused")
        private String[] privateMethod(){
            return new String[1];
        }

        @SuppressWarnings("unused")
        public void throwEx() {
            throw new RuntimeException("ex");
        }

    }
}
