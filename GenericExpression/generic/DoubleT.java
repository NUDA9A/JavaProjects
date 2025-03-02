package expression.generic;


public class DoubleT implements GenericUtils<Double> {
    @Override
    public Double add(Double a, Double b) {
        return a + b;
    }

    @Override
    public Double parse(String s) {
        return Double.parseDouble(s);
    }

    @Override
    public Double negate(Double a) {
        return -a;
    }

    @Override
    public Double divide(Double a, Double b) {
        return a / b;
    }

    @Override
    public Double multiply(Double a, Double b) {
        return a * b;
    }

    @Override
    public Double subtract(Double a, Double b) {
        return a - b;
    }

    @Override
    public Double getValue(int value) {
        return Double.valueOf(String.valueOf(value));
    }

}
