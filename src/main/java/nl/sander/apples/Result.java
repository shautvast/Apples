package nl.sander.apples;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public record Result(boolean areEqual, List<String> diffs) {
    public static Result SAME = new Result(true, List.of());

    public static Result from(boolean areEqual, String message) {
        if (!areEqual) {
            return new Result(areEqual, List.of(message));
        } else {
            return SAME;
        }
    }

    public static Result from(boolean areEqual, Supplier<String> messageSupplier) {
        if (!areEqual) {
            return new Result(areEqual, List.of(messageSupplier.get()));
        } else {
            return SAME;
        }
    }

    public static Result unequal(String message) {
        return from(false, message);
    }

    public static Result merge(Result... result) {
        boolean areEqual = Arrays.stream(result).allMatch(r -> r.areEqual);
        List<String> diffs = Arrays.stream(result)
                .map(Result::diffs)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        return new Result(areEqual, diffs);
    }
}
