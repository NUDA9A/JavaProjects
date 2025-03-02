package expression.generic;


public class MissingOperandException extends Exception {
    @Override
    public String getMessage() {
        return "Missing operand";
    }
}
