package expression.exceptions;


import expression.AbstractExpression;

import java.util.List;

public class CheckedDivide extends AbstractExpression {
    public CheckedDivide(AbstractExpression a, AbstractExpression b) {
        super(a, b);
    }

    @Override
    public int evaluate(int var) {
        int a1 = a.evaluate(var);
        int b1 = b.evaluate(var);
        checkOverflow(a1, b1);
        return a1 / b1;
    }

    @Override
    public int evaluate(List<Integer> variables) {
        int a1 = a.evaluate(variables);
        int b1 = b.evaluate(variables);
        checkOverflow(a1, b1);
        return a1 / b1;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int a1 = a.evaluate(x, y, z);
        int b1 = b.evaluate(x, y, z);
        checkOverflow(a1, b1);
        return a1 / b1;
    }

    private void checkOverflow(int a, int b) {
        if (b == 0) {
            throw new DivisionByZeroException();
        } else if (a == Integer.MIN_VALUE && b == -1) {
            throw new OverflowException();
        }
    }

    @Override
    protected String getExp() {
        return " / ";
    }

    @Override
    public boolean equals(Object b) {
        return false;
    }
}
