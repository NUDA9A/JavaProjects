package expression.exceptions;

public class MissingExpressionException extends Exception {
    private final String message;

    public MissingExpressionException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return "Missing expression: " + message;
    }
}
