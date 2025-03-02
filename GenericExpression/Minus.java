package expression;


import java.util.List;

public class Minus extends AbstractExpression {
    public Minus(AbstractExpression a) {
        super(a);
    }

    @Override
    public int evaluate(int var) {
        return -this.a.evaluate(var);
    }

    @Override
    public int evaluate(List<Integer> variables) {
        return -this.evaluate(variables);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return -this.a.evaluate(x, y, z);
    }

    @Override
    public String toString() {
        return "-" + "(" + a.toString() + ")";
    }

    @Override
    protected String getExp() {
        return null;
    }

    @Override
    public boolean equals(Object b) {
        if (b == this) {
            return true;
        }
        if (b == null || this.getClass() != b.getClass()) {
            return false;
        }
        Minus b1 = (Minus) b;

        return this.toString() == b.toString();
    }
}
