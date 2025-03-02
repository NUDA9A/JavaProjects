package expression.generic;


public class DivisionByZeroException extends ArithmeticException {
    @Override
    public String getMessage() {
        return "Division by zero";
    }
}
