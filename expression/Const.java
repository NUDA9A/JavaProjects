package expression;


public class Const extends AbstractExpression {
    public Const(int cnst) {
        super(cnst);
    }

    @Override
    public int evaluate(int var) {
        return this.cnst;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return this.cnst;
    }

    @Override
    public String toString() {
        return String.valueOf(this.cnst);
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

        Const b1 = (Const) b;

        return this.cnst == b1.cnst;
    }
}
