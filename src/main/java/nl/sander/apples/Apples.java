package nl.sander.apples;

import java.util.Map;

public class Apples {
    private final static Map<Character, String> CHAR_ESCAPES = Map.of('\t', "\\t", '\b', "\\b", '\n', "\\n", '\r', "\\r", '\f', "\\f", '\\', "\\\\");

    public static Result compare(Object left, Object right) {
        if (left == null) {
            return Result.from(right == null, "null != " + asString(right));
        }

        if (right == null) {
            return Result.unequal(asString(left) + " != null");
        }

        if (left == right) {
            return Result.SAME;
        }

        if (left.getClass() != right.getClass()) {
            return Result.unequal(asString(left) + " != " + asString(right));
        }

        return Result.SAME;
    }

    public static Result compare(long left, long right) {
        return Result.from(left == right, left + " != " + right);
    }


    public static Result compare(Object left, long right) {
        if (left == null) {
            return Result.unequal("null != " + right);
        }
        return switch (left) {
            case Long l -> Result.from(l == right, asString(left) + " != " + right);
            case Integer i -> Result.from(i == right, asString(left) + " != " + right);
            case Short s -> Result.from(s == right, asString(left) + " != " + right);
            case Byte b -> Result.from(b == right, asString(left) + " != " + right);
            default -> Result.unequal(asString(left) + " != " + right);
        };
    }

    public static Result compare(char left, char right) {
        return Result.from(left == right, asString(left) + " != " + asString(right));
    }

    public static Result compare(Object left, char right) {
        return Result.from(left instanceof Character && (Character) left == right, asString(left) + " != " + asString(right));
    }

    public static Result compare(long left, Object right) {
        if (right == null) {
            return Result.unequal(left + " != null");
        }
        return switch (right) {
            case Long l -> Result.from(l == left, left + " != " + asString(right));
            case Integer i -> Result.from(i == left, left + " != " + asString(right));
            case Short s -> Result.from(s == left, left + " != " + asString(right));
            case Byte b -> Result.from(b == left, left + " != " + asString(right));
            default -> Result.unequal(left + " != " + asString(right));
        };
    }

    public static Result compare(char left, Object right) {
        return Result.from(right instanceof Character && left == (Character) right, asString(left) + " != " + asString(right));
    }

    public static Result compare(boolean left, boolean right) {
        return Result.from(left == right, left + " != " + right);
    }

    public static Result compare(Object left, boolean right) {
        return Result.from(left instanceof Boolean && (Boolean) left == right, asString(left) + " != " + right);
    }

    public static Result compare(boolean left, Object right) {
        return Result.from(right instanceof Boolean && left == (Boolean) right, left + " != " + asString(right));
    }

    public static Result compare(double left, double right) {
        return Result.from(left == right, left + " != " + right);
    }

    public static Result compare(double left, double right, int precision) {
        return Result.from((int) (left * Math.pow(10, precision)) == (int) (right * Math.pow(10, precision)), left + " != " + right + ", using precision" + precision);
    }

    public static Result compare(Object left, double right) {
        return Result.from(left instanceof Double && (Double) left == right, asString(left) + " != " + right);
    }

    public static Result compare(double left, Object right) {
        return Result.from(right instanceof Double && left == (Double) right, left + " != " + asString(right));
    }

    public static Result compare(float left, Object right) {
        return Result.from(right instanceof Float && left == (Float) right, left + " != " + asString(right));
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
}
