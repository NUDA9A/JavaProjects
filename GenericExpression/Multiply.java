package expression;


import java.util.List;

public class Multiply extends AbstractExpression {
    public Multiply(AbstractExpression a, AbstractExpression b) {
        super(a, b);
    }

    @Override
    public int evaluate(int var) {
        return a.evaluate(var) * b.evaluate(var);
    }

    @Override
    public int evaluate(List<Integer> variables) {
        return a.evaluate(variables) * b.evaluate(variables);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return a.evaluate(x, y, z) * b.evaluate(x, y, z);
    }

    @Override
    protected String getExp() {
        return " * ";
    }

    @Override
    public boolean equals(Object b) {
        if (b == this) {
            return true;
        }

        if (b == null || b.getClass() != this.getClass()) {
            return false;
        }

        Multiply b1 = (Multiply) b;

        return this.a.equals(b1.a) && this.b.equals(b1.b);
    }
}
