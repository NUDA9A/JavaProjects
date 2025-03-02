package expression.generic;


public class Lexeme {
    LexType type;
    String value;

    int indx;

    public Lexeme(LexType type, String value, int indx) {
        this.type = type;
        this.value = value;
        this.indx = indx;
    }

    public Lexeme(LexType type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return "type: " + this.type + ", " + "value: " + this.value;
    }

}
