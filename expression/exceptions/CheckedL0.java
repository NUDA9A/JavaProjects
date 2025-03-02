package expression.exceptions;


import expression.AbstractExpression;

public class CheckedL0 extends AbstractExpression {
    public CheckedL0(AbstractExpression a) {
        super(a);
    }

    @Override
    public int evaluate(int var) {
        return 0;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int a1 = a.evaluate(x, y, z);
        if (a1 == 0) {
            return 32;
        }
        int len = Integer.toBinaryString(a1).length();
        return 32 - len;
    }

    @Override
    public String toString() {
        return "l0(" + a + ")";
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
