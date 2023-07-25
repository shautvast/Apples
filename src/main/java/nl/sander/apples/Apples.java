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

    public static Result compare(Object left, Object right) {
        return compare("", left, right);
    }

    public static Result compare(String property, Object left, Object right) {
        if (left == null) {
            return Result.from(property, right == null, "null != " + asString(right));
        }

        if (right == null) {
            return Result.unequal(property, asString(left) + " != null");
        }

        if (left == right) {
            return Result.SAME;
        }

        if (left.getClass() != right.getClass()) {
            return Result.unequal(property, asString(left) + " != " + asString(right));
        }

        if (left instanceof String) {
            return Result.from(property, left.equals(right), () -> asString(left) + " != " + asString(right));
        }

        if (left instanceof Number) {
            return Result.from(property, left.equals(right), () -> left + " != " + right);
        }

        if (left instanceof Collection) {
            return compareCollections(property, (Collection<?>) left, (Collection<?>) right);
        }

        if (left instanceof Map) {
            return compareMaps(property, (Map<?, ?>) left, (Map<?, ?>) right);
        }

        if (left instanceof Comparable<?>) {
            int comparison = ((Comparable) left).compareTo(right);
            if (comparison == 0) {
                return new Result(true, List.of());
            } else {
                return Result.from(property, false, left + " != " + right);
            }
        }
        try {
            ClassReader cr = new ClassReader(left.getClass().getName());
            AppleFactory appleFactory = new AppleFactory();
            cr.accept(appleFactory, ClassReader.SKIP_FRAMES);
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

            appleFactory.classNode.accept(classWriter);
            byte[] byteArray = classWriter.toByteArray();

            generatedClassesLoader.addClass(appleFactory.classNode.name, byteArray);
            BaseApple apple = (BaseApple) generatedClassesLoader.loadClass(appleFactory.classNode.name).getConstructor().newInstance();
            return apple.compare(left, right);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Result compareCollections(String property, Collection<?> left, Collection<?> right) {
        List<String> diffs =
                zipAndEnumerate(left, right)
                        .map(t -> new Tuple4<Integer, Object, Object, Result>(t.e1, left, right, Apples.compare(property, t.e2, t.e3)))
                        .filter(t -> !t.e4.areEqual())
                        .map(t -> property + "[" + t.e1 + "]:" + t.e2 + " != " + t.e3)
                        .collect(Collectors.toList());
        return new Result(!diffs.isEmpty(), diffs);
    }

    private static Result compareMaps(String property, Map<?, ?> left, Map<?, ?> right) {
        List<String> diffs = new ArrayList<>();
        for (Map.Entry<?, ?> leftEntry : left.entrySet()) {
            Object leftValue = leftEntry.getValue();
            Object rightValue = right.get(leftEntry.getKey());
            Result result = Apples.compare(property + "[" + leftEntry.getKey() + "]", leftValue, rightValue);
            if (!result.areEqual()) {
                diffs.addAll(result.getDiffs());
            }
        }
        return new Result(diffs.isEmpty(), diffs);
    }

    private static Stream<Tuple3<Integer, Object, Object>> zipAndEnumerate(Collection<?> left, Collection<?> right) {
        Iterator<?> rightIt = right.iterator();
        LongAdder adder = new LongAdder();
        return left.stream()
                .filter(__ -> rightIt.hasNext())
                .map(o -> {
                    Tuple3<Integer, Object, Object> next = new Tuple3<>(adder.intValue(), o, rightIt.next());
                    adder.increment();
                    return next;
                });
    }

    public static Result compare(long left, long right) {
        return compare("", left, right);
    }

    public static Result compare(String property, long left, long right) {
        return Result.from(property, left == right, left + " != " + right);
    }

    public static Result compare(int left, int right) {
        return compare("", left, right);
    }

    public static Result compare(String property, int left, int right) {
        return Result.from(property, left == right, left + " != " + right);
    }

    public static Result compare(short left, short right) {
        return compare("", left, right);
    }

    public static Result compare(String property, short left, short right) {
        return Result.from(property, left == right, left + " != " + right);
    }

    public static Result compare(byte left, byte right) {
        return compare("", left, right);
    }

    public static Result compare(String property, byte left, byte right) {
        return Result.from(property, left == right, left + " != " + right);
    }

    public static Result compare(float left, float right) {
        return compare("", left, right);
    }

    public static Result compare(String property, float left, float right) {
        return Result.from(property, left == right, left + " != " + right);
    }

    public static Result compare(Object left, long right) {
        return compare("", left, right);
    }

    public static Result compare(String property, Object left, long right) {
        if (left == null) {
            return Result.unequal(property, "null != " + right);
        }

        if (left instanceof Long) {
            return Result.from(property, (Long) left == right, asString(left) + " != " + right);
        } else if (left instanceof Integer) {
            return Result.from(property, (Integer) left == right, asString(left) + " != " + right);
        } else if (left instanceof Short) {
            return Result.from(property, (Short) left == right, asString(left) + " != " + right);
        } else if (left instanceof Byte) {
            return Result.from(property, (Byte) left == right, asString(left) + " != " + right);
        } else {
            return Result.unequal(property, asString(left) + " != " + right);
        }
    }

    public static Result compare(char left, char right) {
        return compare("", left, right);
    }

    public static Result compare(String property, char left, char right) {
        return Result.from(property, left == right, asString(left) + " != " + asString(right));
    }

    public static Result compare(Object left, char right) {
        return compare("", left, right);
    }

    public static Result compare(String property, Object left, char right) {
        return Result.from(property, left instanceof Character && (Character) left == right, asString(left) + " != " + asString(right));
    }

    public static Result compare(long left, Object right) {
        return compare("", left, right);
    }

    public static Result compare(String property, long left, Object right) {
        if (right == null) {
            return Result.unequal(property, left + " != null");
        }
        if (right instanceof Long) {
            return Result.from(property, (Long) right == left, left + " != " + asString(right));
        } else if (right instanceof Integer) {
            return Result.from(property, (Integer) right == left, left + " != " + asString(right));
        } else if (right instanceof Short) {
            return Result.from(property, (Short) right == left, left + " != " + asString(right));
        } else if (right instanceof Byte) {
            return Result.from(property, (Byte) right == left, left + " != " + asString(right));
        } else {
            return Result.unequal(property, left + " != " + asString(right));
        }
    }

    public static Result compare(char left, Object right) {
        return compare("", left, right);
    }

    public static Result compare(String property, char left, Object right) {
        return Result.from(property, right instanceof Character && left == (Character) right, asString(left) + " != " + asString(right));
    }

    public static Result compare(boolean left, boolean right) {
        return compare("", left, right);
    }

    public static Result compare(String property, boolean left, boolean right) {
        return Result.from(property, left == right, left + " != " + right);
    }

    public static Result compare(Object left, boolean right) {
        return compare("", left, right);
    }

    public static Result compare(String property, Object left, boolean right) {
        return Result.from(property, left instanceof Boolean && (Boolean) left == right, asString(left) + " != " + right);
    }

    public static Result compare(boolean left, Object right) {
        return compare("", left, right);
    }

    public static Result compare(String property, boolean left, Object right) {
        return Result.from(property, right instanceof Boolean && left == (Boolean) right, left + " != " + asString(right));
    }

    public static Result compare(double left, double right) {
        return compare("", left, right);
    }

    public static Result compare(String property, double left, double right) {
        return Result.from(property, left == right, left + " != " + right);
    }

    public static Result compare(double left, double right, int precision) {
        return compare("", left, right, precision);
    }

    public static Result compare(String property, double left, double right, int precision) {
        return Result.from(property, (int) (left * Math.pow(10, precision)) == (int) (right * Math.pow(10, precision)), left + " != " + right + ", using precision" + precision);
    }

    public static Result compare(Object left, double right) {
        return compare("", left, right);
    }

    public static Result compare(String property, Object left, double right) {
        return Result.from(property, left instanceof Double && (Double) left == right, asString(left) + " != " + right);
    }

    public static Result compare(double left, Object right) {
        return compare("", left, right);
    }

    public static Result compare(String property, double left, Object right) {
        return Result.from(property, right instanceof Double && left == (Double) right, left + " != " + asString(right));
    }

    public static Result compare(float left, Object right) {
        return compare("", left, right);
    }

    public static Result compare(String property, float left, Object right) {
        return Result.from(property, right instanceof Float && left == (Float) right, left + " != " + asString(right));
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
