package expression.generic;


public class CheckedSubtract<T> extends AbstractExpression<T> {
    protected CheckedSubtract(Expression<T> a, Expression<T> b, GenericUtils<T> eval) {
        super(a, b, eval);
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return eval.subtract(a.evaluate(x, y, z), b.evaluate(x, y, z));
    }

    public String toString() {
        return "(" + this.a.toString() + " - " + this.b.toString() + ")";
    }
}
