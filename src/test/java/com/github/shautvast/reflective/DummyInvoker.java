package com.github.shautvast.reflective;

@SuppressWarnings("ALL")
public class DummyInvoker extends AbstractInvoker {

    public Object invoke(Object instance, Object... arguments) {
        ((ReflectiveTest.Dummy) instance).setIntValue((int)arguments[0]);
        return null;
    }
}
