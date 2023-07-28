package com.github.shautvast.reflective;

public class DummyInvoker extends AbstractInvoker {

    public Object invoke(Object instance, Object... arguments) {
        return ((ReflectiveTest.Dummy) instance).getName();
    }
}
