package com.github.shautvast.reflective;

import com.github.shautvast.rusty.Panic;
import com.github.shautvast.rusty.Result;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ReflectiveTest {

    @Test
    void testMethods() {
        Dummy dummy = new Dummy("bar");
        MetaClass metaDummy = Reflective.getMetaClass(dummy.getClass());
        assertEquals("com.github.shautvast.reflective.ReflectiveTest$Dummy", metaDummy.getName());

        Iterator<MetaField> fields = metaDummy.getFields().iterator();
        assertTrue(fields.hasNext());
        assertEquals("name", fields.next().getName());

        Set<MetaMethod> methods = metaDummy.getMethods();
        assertFalse(methods.isEmpty());
        assertEquals(6, methods.size());

        MetaMethod equals = metaDummy.getMethod("equals").orElseGet(Assertions::fail);
        assertEquals(boolean.class, equals.getReturnParameter().getType());
        assertTrue(Modifier.isPublic(equals.getModifiers()));

        MetaMethod hashCode = metaDummy.getMethod("hashCode").orElseGet(Assertions::fail);
        assertEquals(List.of(), hashCode.getParameters());
        assertEquals(int.class, hashCode.getReturnParameter().getType());
        assertTrue(Modifier.isPublic(hashCode.getModifiers()));

        MetaMethod getName = metaDummy.getMethod("getName").orElseGet(Assertions::fail);
        assertEquals(List.of(), getName.getParameters());
        assertEquals(String.class, getName.getReturnParameter().getType());
        assertTrue(Modifier.isPublic(getName.getModifiers()));

        MetaMethod privateMethod = metaDummy.getMethod("privateMethod").orElseGet(Assertions::fail);
        assertEquals(List.of(), privateMethod.getParameters());
        assertEquals(String[].class, privateMethod.getReturnParameter().getType());
        assertTrue(Modifier.isPrivate(privateMethod.getModifiers()));
    }


    @Test
    void testInvokeGetter() {
        Dummy dummy = new Dummy("bar");
        MetaMethod getName = Reflective.getMetaClass(dummy.getClass()).getMethod("getName").orElseGet(Assertions::fail);

        // passing "foo" as the instance is not allowed
        assertThrows(Panic.class, () -> getName.invoke("foo").unwrap());
        // we should pass a valid dummy instance
        assertEquals("bar", getName.invoke(dummy).unwrap());
    }

    @Test
    void testInvokeSetter() {
        Dummy dummy = new Dummy("bar");
        MetaClass metaForClass = Reflective.getMetaClass(dummy.getClass());
        MetaMethod setName = metaForClass.getMethod("setName").orElseGet(Assertions::fail);

        assertEquals("bar", dummy.getName()); // before invoke
        setName.invoke(dummy, "foo");
        assertEquals("foo", dummy.getName()); // after invoke
    }

    @Test
    void testInvocationExceptionHappened() {
        Dummy dummy = new Dummy("bar");
        MetaClass metaForClass = Reflective.getMetaClass(dummy.getClass());
        MetaMethod throwEx = metaForClass.getMethod("throwEx").orElseGet(Assertions::fail);

        Result<Void> result = throwEx.invoke(dummy, "foo");
        assertFalse(result.isOk());
    }


    public static class Dummy {
        private String name;

        public Dummy(String name) {
            this.name = name;
        }

        public String getName() {
            return privateMethod()[0];
        }

        public void setName(String name) {
            this.name = name;
        }

        public void throwEx() throws Exception {
           throw new Exception("something must have gone wrong");
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Dummy;
        }

        @Override
        public int hashCode() {
            return 6;
        }

        private String[] privateMethod() {
            return new String[]{name, "bar"};
        }
    }
}
