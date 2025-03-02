package expression;

public class Xor extends AbstractExpression {
    public Xor(AbstractExpression a, AbstractExpression b) {
        super(a, b);
    }

    @Override
    public int evaluate(int var) {
        return this.a.evaluate(var) ^ this.b.evaluate(var);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return this.a.evaluate(x, y, z) ^ this.b.evaluate(x, y, z);
    }

    /*@Override
    public String toString() {
        return "(" + this.a + " ^ " + this.b + ")";
    }*/

    @Override
    protected String getExp() {
        return " ^ ";
    }

    @Override
    public boolean equals(Object b) {
        if (this == b) {
            return true;
        }
        if (b == null || this.getClass() != b.getClass()) {
            return false;
        }

        Xor b1 = (Xor) b;

        return this.a.equals(b1.a) && this.b.equals(b1.b);
    }

}
