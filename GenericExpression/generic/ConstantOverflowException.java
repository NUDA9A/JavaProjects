package expression.generic;


public class ConstantOverflowException extends Exception {

    private final int i;
    public ConstantOverflowException(int i) {
        super();
        this.i = i;
    }

    @Override
    public String getMessage() {
        return "Constant overflow " + i;
    }
}
