package com.github.shautvast.rusty;

public class Panic extends RuntimeException{
    public Panic() {
    }

    public Panic(String message) {
        super(message);
    }

    public Panic(String message, Throwable cause) {
        super(message, cause);
    }

    public Panic(Throwable cause) {
        super(cause);
    }

    public Panic(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
