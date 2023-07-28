package com.github.shautvast.reflective;

public class DummyInvoker extends AbstractInvoker {

    public Object invoke(Object instance, Object... arguments) {
        ((ReflectiveTest.Dummy) instance).setName((String)arguments[0]);
        return null;
    }
}
