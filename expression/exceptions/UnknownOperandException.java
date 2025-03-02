package expression.exceptions;

public class UnknownOperandException extends Exception {

    private final String message;
    public UnknownOperandException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return "Unknown operand: " + message;
    }
}
