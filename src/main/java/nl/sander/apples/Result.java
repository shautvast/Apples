package nl.sander.apples;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SuppressWarnings("unused") // used by generated code
public class Result {
    private final boolean areEqual;
    private final List<String> diffs;

    public Result(boolean areEqual, List<String> diffs) {
        this.areEqual = areEqual;
        this.diffs = diffs;
    }

    public static Result SAME = new Result(true, List.of());

    public static Result from(String property, boolean areEqual, String message) {
        if (!areEqual) {
            if (property.length() > 0) {
                return new Result(areEqual, List.of("for " + property + ": " + message));
            } else {
                return new Result(areEqual, List.of(message));
            }
        } else {
            return SAME;
        }
    }

    public static Result from(String property, boolean areEqual, Supplier<String> messageSupplier) {
        if (!areEqual) {
            if (property.length() > 0) {
                return new Result(areEqual, List.of("for " + property + ": " + messageSupplier.get()));
            } else {
                return new Result(areEqual, List.of(messageSupplier.get()));
            }
        } else {
            return SAME;
        }
    }

    public static Result unequal(String message) {
        return from("", false, message);
    }

    public static Result unequal(String property, String message) {
        return from(property, false, message);
    }

    public List<String> getDiffs() {
        return diffs;
    }

    public boolean areEqual() {
        return areEqual;
    }

    public static Result merge(Result... result) {
        boolean areEqual = Arrays.stream(result).allMatch(r -> r.areEqual);
        List<String> diffs = Arrays.stream(result)
                .map(Result::getDiffs)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        return new Result(areEqual, diffs);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result = (Result) o;
        return areEqual == result.areEqual && Objects.equals(diffs, result.diffs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(areEqual, diffs);
    }

    @Override
    public String toString() {
        return "Result{" +
                "areEqual=" + areEqual +
                ", diffs=" + diffs +
                '}';
    }
}
