package expression.parser;

import java.util.List;

public class Lex {
    private int pos;

    private final List<Lexeme> lexemes;

    public Lex(List<Lexeme> lexemes) {
        this.lexemes = lexemes;
    }

    public Lexeme getLex() {
        return lexemes.get(pos++);
    }

    public void back() {
        pos--;
    }
}
