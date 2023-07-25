package nl.sander.apples;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.util.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings({"unchecked", "rawtypes"})
public class Apples {
    private final static Map<Character, String> CHAR_ESCAPES = Map.of('\t', "\\t", '\b', "\\b", '\n', "\\n", '\r', "\\r", '\f', "\\f", '\\', "\\\\");

    private static final ByteClassLoader generatedClassesLoader = new ByteClassLoader();

    public static Result compare(Object apple, Object orange) {
        return compare("", apple, orange);
    }

    public static Result compare(String property, Object apple, Object orange) {
        if (apple == null) {
            return Result.from(property, orange == null, "null != " + asString(orange));
        }

        if (orange == null) {
            return Result.unequal(property, asString(apple) + " != null");
        }

        if (apple == orange) {
            return Result.SAME;
        }

        if (apple.getClass() != orange.getClass()) {
            return Result.unequal(property, asString(apple) + " != " + asString(orange));
        }

        if (apple instanceof String) {
            return Result.from(property, apple.equals(orange), () -> asString(apple) + " != " + asString(orange));
        }

        if (apple instanceof Number) {
            return Result.from(property, apple.equals(orange), () -> apple + " != " + orange);
        }

        if (apple instanceof Collection) {
            return compareCollections(property, (Collection<?>) apple, (Collection<?>) orange);
        }

        if (apple instanceof Map) {
            return compareMaps(property, (Map<?, ?>) apple, (Map<?, ?>) orange);
        }

        if (apple instanceof Comparable<?>) {
            int comparison = ((Comparable) apple).compareTo(orange);
            if (comparison == 0) {
                return new Result(true, List.of());
            } else {
                return Result.from(property, false, apple + " != " + orange);
            }
        }
        try {
            ClassReader cr = new ClassReader(apple.getClass().getName());
            AppleFactory appleFactory = new AppleFactory();
            cr.accept(appleFactory, ClassReader.SKIP_FRAMES);
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

            appleFactory.classNode.accept(classWriter);
            byte[] byteArray = classWriter.toByteArray();

            generatedClassesLoader.addClass(appleFactory.classNode.name, byteArray);
            BaseApple baseApple = (BaseApple) generatedClassesLoader.loadClass(appleFactory.classNode.name).getConstructor().newInstance();
            return baseApple.compare(apple, orange);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Result compareCollections(String property, Collection<?> apple, Collection<?> orange) {
        List<String> diffs =
                zipAndEnumerate(apple, orange)
                        .map(t -> new Tuple4<Integer, Object, Object, Result>(t.e1, apple, orange, Apples.compare(property, t.e2, t.e3)))
                        .filter(t -> !t.e4.areEqual())
                        .map(t -> property + "[" + t.e1 + "]:" + t.e2 + " != " + t.e3)
                        .collect(Collectors.toList());
        return new Result(!diffs.isEmpty(), diffs);
    }

    private static Result compareMaps(String property, Map<?, ?> apple, Map<?, ?> orange) {
        List<String> diffs = new ArrayList<>();
        for (Map.Entry<?, ?> appleEntry : apple.entrySet()) {
            Object appleValue = appleEntry.getValue();
            Object orangeValue = orange.get(appleEntry.getKey());
            Result result = Apples.compare(property + "[" + appleEntry.getKey() + "]", appleValue, orangeValue);
            if (!result.areEqual()) {
                diffs.addAll(result.getDiffs());
            }
        }
        return new Result(diffs.isEmpty(), diffs);
    }

    private static Stream<Tuple3<Integer, Object, Object>> zipAndEnumerate(Collection<?> apple, Collection<?> orange) {
        Iterator<?> orangeListIterator = orange.iterator();
        LongAdder adder = new LongAdder();
        return apple.stream()
                .filter(__ -> orangeListIterator.hasNext())
                .map(o -> {
                    Tuple3<Integer, Object, Object> next = new Tuple3<>(adder.intValue(), o, orangeListIterator.next());
                    adder.increment();
                    return next;
                });
    }

    public static Result compare(long apple, long orange) {
        return compare("", apple, orange);
    }

    public static Result compare(String property, long apple, long orange) {
        return Result.from(property, apple == orange, apple + " != " + orange);
    }

    public static Result compare(int apple, int orange) {
        return compare("", apple, orange);
    }

    public static Result compare(String property, int apple, int orange) {
        return Result.from(property, apple == orange, apple + " != " + orange);
    }

    public static Result compare(short apple, short orange) {
        return compare("", apple, orange);
    }

    public static Result compare(String property, short apple, short orange) {
        return Result.from(property, apple == orange, apple + " != " + orange);
    }

    public static Result compare(byte apple, byte orange) {
        return compare("", apple, orange);
    }

    public static Result compare(String property, byte apple, byte orange) {
        return Result.from(property, apple == orange, apple + " != " + orange);
    }

    public static Result compare(float apple, float orange) {
        return compare("", apple, orange);
    }

    public static Result compare(String property, float apple, float orange) {
        return Result.from(property, apple == orange, apple + " != " + orange);
    }

    public static Result compare(Object apple, long orange) {
        return compare("", apple, orange);
    }

    public static Result compare(String property, Object apple, long orange) {
        if (apple == null) {
            return Result.unequal(property, "null != " + orange);
        }

        if (apple instanceof Long) {
            return Result.from(property, (Long) apple == orange, asString(apple) + " != " + orange);
        } else if (apple instanceof Integer) {
            return Result.from(property, (Integer) apple == orange, asString(apple) + " != " + orange);
        } else if (apple instanceof Short) {
            return Result.from(property, (Short) apple == orange, asString(apple) + " != " + orange);
        } else if (apple instanceof Byte) {
            return Result.from(property, (Byte) apple == orange, asString(apple) + " != " + orange);
        } else {
            return Result.unequal(property, asString(apple) + " != " + orange);
        }
    }

    public static Result compare(char apple, char orange) {
        return compare("", apple, orange);
    }

    public static Result compare(String property, char apple, char orange) {
        return Result.from(property, apple == orange, asString(apple) + " != " + asString(orange));
    }

    public static Result compare(Object apple, char orange) {
        return compare("", apple, orange);
    }

    public static Result compare(String property, Object apple, char orange) {
        return Result.from(property, apple instanceof Character && (Character) apple == orange, asString(apple) + " != " + asString(orange));
    }

    public static Result compare(long apple, Object orange) {
        return compare("", apple, orange);
    }

    public static Result compare(String property, long apple, Object orange) {
        if (orange == null) {
            return Result.unequal(property, apple + " != null");
        }
        if (orange instanceof Long) {
            return Result.from(property, (Long) orange == apple, apple + " != " + asString(orange));
        } else if (orange instanceof Integer) {
            return Result.from(property, (Integer) orange == apple, apple + " != " + asString(orange));
        } else if (orange instanceof Short) {
            return Result.from(property, (Short) orange == apple, apple + " != " + asString(orange));
        } else if (orange instanceof Byte) {
            return Result.from(property, (Byte) orange == apple, apple + " != " + asString(orange));
        } else {
            return Result.unequal(property, apple + " != " + asString(orange));
        }
    }

    public static Result compare(char apple, Object orange) {
        return compare("", apple, orange);
    }

    public static Result compare(String property, char apple, Object orange) {
        return Result.from(property, orange instanceof Character && apple == (Character) orange, asString(apple) + " != " + asString(orange));
    }

    public static Result compare(boolean apple, boolean orange) {
        return compare("", apple, orange);
    }

    public static Result compare(String property, boolean apple, boolean orange) {
        return Result.from(property, apple == orange, apple + " != " + orange);
    }

    public static Result compare(Object apple, boolean orange) {
        return compare("", apple, orange);
    }

    public static Result compare(String property, Object apple, boolean orange) {
        return Result.from(property, apple instanceof Boolean && (Boolean) apple == orange, asString(apple) + " != " + orange);
    }

    public static Result compare(boolean apple, Object orange) {
        return compare("", apple, orange);
    }

    public static Result compare(String property, boolean apple, Object orange) {
        return Result.from(property, orange instanceof Boolean && apple == (Boolean) orange, apple + " != " + asString(orange));
    }

    public static Result compare(double apple, double orange) {
        return compare("", apple, orange);
    }

    public static Result compare(String property, double apple, double orange) {
        return Result.from(property, apple == orange, apple + " != " + orange);
    }

    public static Result compare(double apple, double orange, int precision) {
        return compare("", apple, orange, precision);
    }

    public static Result compare(String property, double apple, double orange, int precision) {
        return Result.from(property, (int) (apple * Math.pow(10, precision)) == (int) (orange * Math.pow(10, precision)), apple + " != " + orange + ", using precision" + precision);
    }

    public static Result compare(Object apple, double orange) {
        return compare("", apple, orange);
    }

    public static Result compare(String property, Object apple, double orange) {
        return Result.from(property, apple instanceof Double && (Double) apple == orange, asString(apple) + " != " + orange);
    }

    public static Result compare(double apple, Object orange) {
        return compare("", apple, orange);
    }

    public static Result compare(String property, double apple, Object orange) {
        return Result.from(property, orange instanceof Double && apple == (Double) orange, apple + " != " + asString(orange));
    }

    public static Result compare(float apple, Object orange) {
        return compare("", apple, orange);
    }

    public static Result compare(String property, float apple, Object orange) {
        return Result.from(property, orange instanceof Float && apple == (Float) orange, apple + " != " + asString(orange));
    }

    private static String asString(char value) {
        if (CHAR_ESCAPES.containsKey(value)) {
            return "'" + CHAR_ESCAPES.get(value) + "'";
        } else if (value < 32) {
            return String.format("'\\u%04d'", (int) value);
        } else {
            return "'" + value + "'";
        }
    }


    private static String asString(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof String) {
            return "\"" + value + "\"";
        }
        if (value instanceof Character) {
            return asString((char) value);
        } else {
            return value.getClass().getName() + ": " + value;
        }
    }

    static class Tuple3<E1, E2, E3> {
        final E1 e1;
        final E2 e2;
        final E3 e3;

        Tuple3(E1 e1, E2 e2, E3 e3) {
            this.e1 = e1;
            this.e2 = e2;
            this.e3 = e3;
        }
    }

    static class Tuple4<E1, E2, E3, E4> {
        final E1 e1;
        final E2 e2;
        final E3 e3;
        final E4 e4;

        Tuple4(E1 e1, E2 e2, E3 e3, E4 e4) {
            this.e1 = e1;
            this.e2 = e2;
            this.e3 = e3;
            this.e4 = e4;
        }
    }
}
