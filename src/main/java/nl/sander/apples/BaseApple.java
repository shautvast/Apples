package nl.sander.apples;

abstract class BaseApple<T> {

    public abstract Result compare(T left, T right);
}
