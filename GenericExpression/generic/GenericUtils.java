package expression.generic;


public interface GenericUtils<T> {
    T add (T a, T b);

    T parse(String s) throws Exception;

    T negate(T a);

    T divide(T a, T b);

    T multiply(T a, T b);

    T subtract(T a, T b);

    T getValue(int value);
}
