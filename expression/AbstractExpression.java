package expression;

import java.util.Objects;

public abstract class AbstractExpression implements Expression, TripleExpression {
    protected int cnst;

    protected AbstractExpression a, b;

    protected String var;

    protected AbstractExpression(int cnst) {
        this.cnst = cnst;
    }

    protected AbstractExpression(String var) {
        this.var = var;
    }

    protected AbstractExpression(AbstractExpression a, AbstractExpression b) {
        this.a = a;
        this.b = b;
    }

    protected AbstractExpression(AbstractExpression a) {
        this.a = a;
    }
    @Override
    abstract public int evaluate(int var);

    @Override
    abstract public int evaluate(int x, int y, int z);

    @Override
    public String toString() {
        return "(" + this.a + this.getExp() + this.b + ")";
    }

    abstract protected String getExp();

    @Override
    abstract public boolean equals(Object b);

    @Override
    public int hashCode() {
        return Objects.hash(this.toString());
    }
}
