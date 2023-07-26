package nl.sander.reflective.tomap;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractToMap {

    public abstract Map<String, Object> toMap(Object object);

    protected void add(HashMap<String, Object> m, String key, byte b) {
        m.put(key, b);
    }

    protected void add(HashMap<String, Object> m, String key, short s) {
        m.put(key, s);
    }

    protected void add(HashMap<String, Object> m, String key, int i) {
        m.put(key, i);
    }

    protected void add(HashMap<String, Object> m, String key, boolean b) {
        m.put(key, b);
    }

    protected void add(HashMap<String, Object> m, String key, float f) {
        m.put(key, f);
    }

    protected void add(HashMap<String, Object> m, String key, double b) {
        m.put(key, b);
    }

    protected void add(HashMap<String, Object> m, String key, Object o) {
        m.put(key, o);
    }
}
