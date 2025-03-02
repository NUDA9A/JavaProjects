package expression.exceptions;

public class CorrectBracketSequencesException extends Exception {
    private final String bracketType;

    public CorrectBracketSequencesException(int i) {
        super();
        if (i < 0) {
            bracketType = "opening";
        } else {
            bracketType = "closing";
        }
    }

    @Override
    public String getMessage() {
        return "No " + bracketType + " parenthesis";
    }
}
