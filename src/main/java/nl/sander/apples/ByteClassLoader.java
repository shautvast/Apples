package nl.sander.apples;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class ByteClassLoader extends ClassLoader {

    private final ConcurrentMap<String, Class<?>> classes = new ConcurrentHashMap<>();

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> instance = classes.get(name);
        if (instance == null) {
            throw new ClassNotFoundException(name);
        }
        return instance;
    }

    public void addClass(String name, byte[] bytecode) {
        Class<?> classDef = defineClass(name, bytecode, 0, bytecode.length);
        classes.put(name, classDef);
    }
}
