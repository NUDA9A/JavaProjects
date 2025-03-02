package expression;


public class Variable extends AbstractExpression {
    public Variable(String var) {
        super(var);
    }

    @Override
    public int evaluate(int var) {
        return var;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        switch (this.var) {
            case "x" -> {
                return x;
            }
            case "y" -> {
                return y;
            }
            case "z" -> {
                return z;
            }
        }
        throw new IllegalStateException();
    }

    @Override
    public String toString() {
        return this.var;
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

        Variable b1 = (Variable) b;

        return this.toString() == b1.toString();
    }
}
