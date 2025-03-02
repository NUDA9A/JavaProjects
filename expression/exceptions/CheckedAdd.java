package expression.exceptions;


import expression.AbstractExpression;

public class CheckedAdd extends AbstractExpression {

    public CheckedAdd(AbstractExpression a, AbstractExpression b) {
        super(a, b);
    }

    @Override
    public int evaluate(int var) {
        return 0;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int a1 = a.evaluate(x, y, z);
        int b1 = b.evaluate(x, y, z);
        if (a1 > 0 && b1 > 0) {
            if (Integer.MAX_VALUE - b1 < a1) {
                throw new OverflowException();
            }
        } else if (a1 < 0 && b1 < 0) {
            if (Integer.MIN_VALUE - b1 > a1) {
                throw new OverflowException();
            }
        }
        return a1 + b1;
    }

    @Override
    protected String getExp() {
        return " + ";
    }

    @Override
    public boolean equals(Object b) {
        return false;
    }
}
