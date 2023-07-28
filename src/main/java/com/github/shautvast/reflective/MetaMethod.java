package com.github.shautvast.reflective;

import com.github.shautvast.reflective.java.Java;
import com.github.shautvast.rusty.Result;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.github.shautvast.rusty.Result.err;
import static com.github.shautvast.rusty.Result.ok;

public class MetaMethod {
    private final MetaClass metaClass;
    private final String name;
    private final int modifiers;
    private final String descriptor;
    private List<Class<?>> parameters = new LinkedList<>();
    private Class<?> returnParameter;
    private final AbstractInvoker invoker = InvokerFactory.of(this).unwrapOr(() -> null);

    public MetaMethod(MetaClass metaClass, String methodname, int modifiers, String descriptor) {
        this.metaClass = metaClass;
        this.name = methodname;
        this.modifiers = modifiers;
        this.descriptor = descriptor;
        getParameters(descriptor);
    }

    public String getName() {
        return name;
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

    public int getModifiers() {
        return modifiers;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public List<Class<?>> getParameters() {
        return parameters;
    }

    public Class<?> getReturnParameter() {
        return returnParameter;
    }

    @SuppressWarnings("unchecked")
    public <T> Result<T> invoke(Object instance, Object... arguments) {
        if (instance.getClass() != metaClass.getJavaClass()) {
            return Result.err("instance type not of " + metaClass.getJavaClass());
        }

        try {
            T invoke = (T) invoker.invoke(instance, arguments);
            return ok(invoke);
        } catch (Exception e) {
            return err(e);
        }
    }

    private void getParameters(String descriptor) {
        String[] split = descriptor.split("[()]");
        String parms = split[1];
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < parms.length(); i++) {
            String t = Character.toString(parms.charAt(i));
            if (!"L".equals(t) && buf.length() == 0) {
                this.parameters.add(Java.getClassFromDescriptor(t));
            }
            if (";".equals(t)) {
                buf.append(t);
                this.parameters.add(Java.getClassFromDescriptor(buf.toString()));
                buf = new StringBuilder();
            } else {
                buf.append(t);
            }
        }
        this.parameters = Collections.unmodifiableList(this.parameters); //effectively final
        this.returnParameter = Java.getClassFromDescriptor(split[2]);
    }
}
