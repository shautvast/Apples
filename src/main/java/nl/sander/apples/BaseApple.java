package nl.sander.apples;

public abstract class BaseApple<T> {

    public abstract Result compare(T left, T right);
}
