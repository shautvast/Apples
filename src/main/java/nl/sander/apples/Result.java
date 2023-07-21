package nl.sander.apples;

import java.util.List;

public record Result(boolean areEqual, List<String> diffs) {
    public static Result SAME = new Result(true, List.of());

    public static Result from(boolean areEqual, String message) {
        if (!areEqual) {
            return new Result(areEqual, List.of(message));
        } else {
            return SAME;
        }
    }

    public static Result unequal(String message) {
        return from(false, message);
    }

}
