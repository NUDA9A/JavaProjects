package expression.exceptions;

public class MissingOperandException extends Exception {
    @Override
    public String getMessage() {
        return "Missing operand";
    }
}
