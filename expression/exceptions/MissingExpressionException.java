package expression.exceptions;

public class MissingExpressionException extends Exception {
    @Override
    public String getMessage() {
        return "Missing expression";
    }
}
