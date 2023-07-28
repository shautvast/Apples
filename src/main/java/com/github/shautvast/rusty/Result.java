package com.github.shautvast.rusty;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * attempted rust-style exception handling
 *
 * @param <T> type of expected result
 */
public class Result<T> {
    private final T value;
    private final Panic error;

    private Result(T value, Throwable error) {
        this.value = value;
        this.error = new Panic(error);
    }

    private Result(T value) {
        this.value = value;
        this.error = null;
    }

    public static <T> Result<T> err(Throwable error) {
        return new Result<>(null, new Panic(error));
    }

    public static <T> Result<T> err(String error) {
        return new Result<>(null, new Panic(error));
    }

    public static <T> Result<T> ok(T value) {
        return new Result<>(value);
    }

    public T unwrap() {
        if (error == null) {
            return value;
        } else {
            throw error;
        }
    }

    public T expect(String failedExpectation) {
        if (error == null) {
            return value;
        } else {
            throw new Panic(failedExpectation);
        }
    }

    public T unwrapOr(Supplier<T> valueSupplier) {
        if (error == null) {
            return value;
        } else {
            return valueSupplier.get();
        }
    }

    public <R> Result<R> map(Function<T, R> mappingFunction) {
        if (error == null) {
            return new Result<>(mappingFunction.apply(value), null);
        } else {
            return new Result<>(null, error);
        }
    }
}
