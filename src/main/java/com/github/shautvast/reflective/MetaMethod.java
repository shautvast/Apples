package com.github.shautvast.reflective;

import com.github.shautvast.reflective.java.Java;
import com.github.shautvast.rusty.Result;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.github.shautvast.rusty.Result.err;
import static com.github.shautvast.rusty.Result.ok;

public class MetaMethod {
    private final MetaClass metaClass;
    private final String name;
    private final int modifiers;
    private final String descriptor;
    private final List<Parameter<?>> parameters;
    private Parameter<?> returnParameter;
    private final AbstractInvoker invoker;

    public MetaMethod(MetaClass metaClass, String methodname, int modifiers, String descriptor) {
        this.metaClass = metaClass;
        this.name = methodname;
        this.modifiers = modifiers;
        this.descriptor = descriptor;
        this.parameters = createParameters(descriptor);
        if (!Modifier.isPrivate(modifiers)) {
            invoker = InvokerFactory.of(this).unwrap();//TODO
        } else {
            invoker = null;
        }
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

    public List<Parameter<?>> getParameters() {
        return parameters;
    }

    public Parameter<?> getReturnParameter() {
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

    private List<Parameter<?>> createParameters(String descriptor) {
        List<Parameter<?>> mutableParams = new ArrayList<>();
        String[] split = descriptor.split("[()]");
        String parms = split[1];
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < parms.length(); i++) {
            String t = Character.toString(parms.charAt(i));
            if (!"L".equals(t) && buf.length() == 0) {
                mutableParams.add(new Parameter<>(Java.getClassFromDescriptor(t), t));
            }
            if (";".equals(t)) {
                buf.append(t);
                String desc = buf.toString();
                mutableParams.add(new Parameter<>(Java.getClassFromDescriptor(desc), correct(desc)));
                buf = new StringBuilder();
            } else {
                buf.append(t);
            }
        }

        String returnDesc = split[2];
        this.returnParameter = new Parameter<>(
                Java.getClassFromDescriptor(returnDesc),
                correct(returnDesc));

        return Collections.unmodifiableList(mutableParams);
    }

    private static String correct(String returnDesc) {
        if (returnDesc.startsWith("L")) {
            return returnDesc.substring(1, returnDesc.length() - 1);
        } else {
            return returnDesc;
        }
    }
}
