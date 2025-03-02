package expression.exceptions;


import expression.AbstractExpression;

import java.util.List;

public class CheckedMultiply extends AbstractExpression {
    public CheckedMultiply(AbstractExpression a, AbstractExpression b) {
        super(a, b);
    }

    @Override
    public int evaluate(int var) {
        int a1 = a.evaluate(var);
        int b1 = b.evaluate(var);
        if (a1 == 0 || b1 == 0) {
            return 0;
        }
        checkOverflow(a1, b1);
        return a1 * b1;
    }

    @Override
    public int evaluate(List<Integer> variables) {
        int a1 = a.evaluate(variables);
        int b1 = b.evaluate(variables);
        if (a1 == 0 || b1 == 0) {
            return 0;
        }
        checkOverflow(a1, b1);
        return a1 * b1;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int a1 = a.evaluate(x, y, z);
        int b1 = b.evaluate(x, y, z);
        if (a1 == 0 || b1 == 0) {
            return 0;
        }
        checkOverflow(a1, b1);
        return a1 * b1;
    }

    private void checkOverflow(int a, int b) {
        if (a > 0 && b > 0) {
            if (Integer.MAX_VALUE / b < a) {
                throw new OverflowException();
            }
        } else if (a < 0 && b < 0) {
            if (Integer.MAX_VALUE / b > a) {
                throw new OverflowException();
            }
        } else if (a > 0) {
            if (Integer.MIN_VALUE / a > b) {
                throw new OverflowException();
            }
        } else {
            if (Integer.MIN_VALUE / b > a) {
                throw new OverflowException();
            }
        }
    }

    @Override
    protected String getExp() {
        return " * ";
    }

    @Override
    public boolean equals(Object b) {
        return false;
    }
}
