package expression.exceptions;


import expression.AbstractExpression;

import java.util.List;

public class CheckedL0 extends AbstractExpression {
    public CheckedL0(AbstractExpression a) {
        super(a);
    }

    @Override
    public int evaluate(int var) {
        return eval(a.evaluate(var));
    }

    @Override
    public int evaluate(List<Integer> variables) {
        return eval(a.evaluate(variables));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return eval(a.evaluate(x, y, z));
    }

    private int eval(int a) {
        if (a == 0) {
            return 32;
        }
        int len = Integer.toBinaryString(a).length();
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
