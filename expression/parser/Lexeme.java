package expression.parser;

public class Lexeme {
    LexType type;
    String value;

    public Lexeme(LexType type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return "type: " + this.type + ", " + "value: " + this.value;
    }

}
