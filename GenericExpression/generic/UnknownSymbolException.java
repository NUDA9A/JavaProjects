package expression.generic;


public class UnknownSymbolException extends Exception {

    private final String message;
    public UnknownSymbolException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return "Unknown symbol: " + message;
    }
}
