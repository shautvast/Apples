package com.github.shautvast.reflective;

import com.github.shautvast.reflective.MetaClass;
import com.github.shautvast.reflective.Reflective;

public class Main {
    public static void main(String[] args) {
        Dummy dummy = new Dummy();
        dummy.setName("foo");
        MetaClass metaDummy = Reflective.getMetaClass(dummy.getClass());

        metaDummy.getMethod("setName")
                .ifPresent(m -> m.invoke(dummy, "bar"));
        System.out.println(dummy.getName()); // prints "bar"
    }

    public static class Dummy {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}