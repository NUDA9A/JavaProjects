package expression;

import java.util.List;
import java.util.Objects;

public abstract class AbstractExpression implements Expression, TripleExpression, ListExpression {
    protected int cnst;

    protected int pos;

    protected AbstractExpression a, b;

    protected String var;

    protected AbstractExpression(int cnst, boolean list) {
        if (list) {
            this.pos = cnst;
        }
        this.cnst = cnst;
    }

    protected AbstractExpression(int pos, String value) {
        this.pos = pos;
        this.var = value;
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
    abstract public int evaluate(List<Integer> variables);

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
