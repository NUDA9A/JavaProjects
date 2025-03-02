package expression.generic;


public class Variable<T> extends AbstractExpression<T> {
    public Variable(String s) {
        super(s);
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return switch (var) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> throw new IllegalStateException();
        };
    }

    public String toString() {
        return this.var;
    }
}
