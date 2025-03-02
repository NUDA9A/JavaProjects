package expression.exceptions;

public class Lexeme {
    LexType type;
    String value;

    public Lexeme(LexType type, String value) {
        this.type = type;
        this.value = value;
    }

    public Lexeme(LexType type, Character value) {
        this.type = type;
        this.value = value.toString();
    }

    @Override
    public String toString() {
        return "type: " + this.type + ", " + "value: " + this.value;
    }

}
