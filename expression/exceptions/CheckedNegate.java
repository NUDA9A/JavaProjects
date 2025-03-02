package expression.exceptions;


import expression.AbstractExpression;

public class CheckedNegate extends AbstractExpression {
    public CheckedNegate(AbstractExpression a) {
        super(a);
    }

    @Override
    public int evaluate(int var) {
        return 0;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int a1 = a.evaluate(x, y, z);
        if (a1 == Integer.MIN_VALUE) {
            throw new OverflowException();
        }
        return -a1;
    }

    @Override
    public String toString() {
        return "-(" + a + ")";
    }

    @Override
    protected String getExp() {
        return null;
    }

    @Override
    public boolean equals(Object b) {
        return false;
    }
}
