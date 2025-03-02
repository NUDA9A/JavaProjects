package expression.exceptions;


import expression.AbstractExpression;

public class CheckedT0 extends AbstractExpression {
    public CheckedT0(AbstractExpression a) {
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
        String s = Integer.toBinaryString(a1);
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
