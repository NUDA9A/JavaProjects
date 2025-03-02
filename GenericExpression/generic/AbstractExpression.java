package expression.generic;


public abstract class AbstractExpression<T> implements Expression<T> {
    protected Expression<T> a, b;

    protected GenericUtils<T> eval;

    protected String var;

    protected T cnst;

    public AbstractExpression(T cnst) {
        this.cnst = cnst;
    }

    public AbstractExpression(String var) {
        this.var = var;
    }

    public AbstractExpression(Expression<T> a, Expression<T> b, GenericUtils<T> eval) {
        this.a = a;
        this.b = b;
        this.eval = eval;
    }

    public AbstractExpression(Expression<T> a, GenericUtils<T> eval) {
        this.a = a;
        this.eval = eval;
    }
}
