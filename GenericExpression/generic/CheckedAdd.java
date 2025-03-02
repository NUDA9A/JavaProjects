package expression.generic;


public class CheckedAdd<T> extends AbstractExpression<T> {
    public CheckedAdd(Expression<T> a, Expression<T> b, GenericUtils<T> eval) {
        super(a, b, eval);
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return eval.add(a.evaluate(x, y, z), b.evaluate(x ,y, z));
    }

    public String toString() {
        return "(" + this.a.toString() + " + " + this.b.toString() + ")";
    }
}
