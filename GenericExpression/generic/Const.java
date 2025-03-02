package expression.generic;


public class Const<T> extends AbstractExpression<T> {
    public Const(T cnst) {
        super(cnst);
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return cnst;
    }

    public String toString() {
        return String.valueOf(this.cnst);
    }
}
