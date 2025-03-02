package expression;


import java.util.List;

public class Variable extends AbstractExpression {
    public Variable(String var) {
        super(var);
    }

    public Variable(int pos) {
        super(pos, true);
    }

    public Variable(int pos, String value) {
        super(pos, value);
    }

    @Override
    public int evaluate(int var) {
        return var;
    }

    @Override
    public int evaluate(List<Integer> variables) {
        return variables.get(this.pos);
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
