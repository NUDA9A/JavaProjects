package expression.generic;


public class CheckedNegate<T> extends AbstractExpression<T> {
    public CheckedNegate(Expression<T> a, GenericUtils<T> eval) {
        super(a, eval);
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return eval.negate(a.evaluate(x, y, z));
    }

    public String toString() {
        return "-" + "(" + this.a.toString() + ")";
    }
}
