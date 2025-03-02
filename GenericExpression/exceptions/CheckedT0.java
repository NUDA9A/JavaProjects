package expression.exceptions;


import expression.AbstractExpression;

import java.util.List;

public class CheckedT0 extends AbstractExpression {
    public CheckedT0(AbstractExpression a) {
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
        String s = Integer.toBinaryString(a);
        int count = 0;
        for (int i = s.length() - 1; i > -1; i--) {
            if (s.charAt(i) == '1') {
                break;
            }
            count++;
        }
        return count;
    }

    @Override
    public String toString() {
        return "t0(" + a + ")";
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
