package com.github.shautvast.reflective;

@SuppressWarnings("ALL")
public class DummyInvoker extends AbstractInvoker {

    public Object invoke(Object instance, Object... arguments) {
        ((ReflectiveTest.Dummy) instance).setByteArrayValue((byte[][])arguments[0]);
        return null;
    }
}
