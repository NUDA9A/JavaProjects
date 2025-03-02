package expression;


public class Or extends AbstractExpression {

    public Or(AbstractExpression a, AbstractExpression b) {
        super(a, b);
    }

    @Override
    public int evaluate(int var) {
        return this.a.evaluate(var) | this.b.evaluate(var);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return this.a.evaluate(x, y, z) | this.b.evaluate(x, y, z);
    }

    @Override
    protected String getExp() {
        return " | ";
    }

    @Override
    public boolean equals(Object b) {
        if (this == b) {
            return true;
        }

        if (b == null || this.getClass() != b.getClass()) {
            return false;
        }

        Or b1 = (Or) b;

        return this.a.equals(b1.a) && this.b.equals(b1.b);
    }
}
