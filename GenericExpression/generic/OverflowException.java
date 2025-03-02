package expression.generic;


public class OverflowException extends ArithmeticException {
    @Override
    public String getMessage() {
        return "Overflow";
    }
}
