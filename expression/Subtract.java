package expression;


public class Subtract extends AbstractExpression {
    public Subtract(AbstractExpression a, AbstractExpression b) {
        super(a, b);
    }

    @Override
    public int evaluate(int var) {
        return a.evaluate(var) - b.evaluate(var);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return a.evaluate(x, y, z) - b.evaluate(x, y, z);
    }

    @Override
    protected String getExp() {
        return " - ";
    }

    @Override
    public boolean equals(Object b) {
        if (b == this) {
            return true;
        }

        if (b == null || b.getClass() != this.getClass()) {
            return false;
        }

        Subtract b1 = (Subtract) b;

        return this.a.equals(b1.a) && this.b.equals(b1.b);
    }
}
