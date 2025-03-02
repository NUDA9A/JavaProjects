package expression.generic;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GenericExpressionParser<T> {

    public GenericExpressionParser(GenericUtils<T> type) {
        this.type = type;
    }
    private final GenericUtils<T> type;
    private final Map<String, LexType> l = Map.of(
            "(", LexType.LB,
            ")", LexType.RB,
            "*", LexType.MUL,
            "/", LexType.DIV,
            "+", LexType.PLUS,
            "-", LexType.MINUS
    );

    private int parseNum(StringBuilder sb, int i, String s) {
        while (i < s.length() && (Character.isDigit(s.charAt(i)) || s.charAt(i) == '.')) {
            sb.append(s.charAt(i++));
        }
        return i - 1;
    }

    private int parseVar(StringBuilder sb, int i, String s) {
        while (i < s.length() && !Character.isWhitespace(s.charAt(i))) {
            if (l.containsKey(s.substring(i, i + 1))) {
                break;
            }
            sb.append(s.charAt(i++));
        }
        return i - 1;
    }

    public List<Lexeme> lexAnalyze(String expression) throws Exception {
        int op = 0;
        int bracket = 0;
        boolean isFirst = false;
        LexType lexType = null;
        List<Lexeme> lexemes = new ArrayList<>();
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (Character.isWhitespace(c)) {
                continue;
            } else if (Character.isDigit(c)) {
                StringBuilder num = new StringBuilder();
                if (lexType == LexType.NUMBER) {
                    throw new MissingOperandException();
                }
                i = parseNum(num, i, expression);
                lexemes.add(new Lexeme(LexType.NUMBER, num.toString(), i));
                lexType = LexType.NUMBER;
                op = 0;
                if (!isFirst) {
                    isFirst = true;
                }
                continue;
            } else if (Character.isLetter(c)) {
                StringBuilder symbols = new StringBuilder();
                i = parseVar(symbols, i, expression);
                if (lexType == LexType.VAR) {
                    throw new MissingOperandException();
                }
                if (symbols.toString().equals("x") ||
                        symbols.toString().equals("y") ||
                        symbols.toString().equals("z")) {
                    lexemes.add(new Lexeme(LexType.VAR, symbols.toString(), i));
                    lexType = LexType.VAR;
                    op = 0;
                    if (!isFirst) {
                        isFirst = true;
                    }
                    continue;
                }
                throw new UnknownSymbolException(symbols.toString());
            }

            int len = lexemes.size();

            for (String key : l.keySet()) {
                if (String.valueOf(c).equals(key)) {
                    if (key.equals("-")) {
                        if (i == expression.length() - 1) {
                            throw new MissingExpressionException("Unary '-' at the end");
                        } else if (Character.isDigit(expression.charAt(i + 1)) && (op > 0 || !isFirst)) {
                            StringBuilder num = new StringBuilder();
                            num.append(key);
                            if (lexType == LexType.NUMBER) {
                                throw new MissingOperandException();
                            }
                            i = parseNum(num, i + 1, expression);
                            lexemes.add(new Lexeme(LexType.NUMBER, num.toString(), i));
                            lexType = LexType.NUMBER;
                            op = 0;
                            if (!isFirst) {
                                isFirst = true;
                            }
                            break;
                        }
                    }
                    lexemes.add(new Lexeme(l.get(key), key, i));
                    if (!key.equals("(") && !key.equals(")")) {
                        lexType = l.get(key);
                    }
                    op++;
                    if (key.equals("(")) {
                        bracket++;
                        op--;
                    }
                    if (key.equals(")")) {
                        bracket--;
                        op--;
                    }
                    if (bracket < 0) {
                        throw new CorrectBracketSequencesException(bracket);
                    }
                    break;
                }
            }
            if (len == lexemes.size()) {
                throw new UnknownSymbolException(expression.substring(i, i + 1));
            }
        }
        if (bracket != 0) {
            throw new CorrectBracketSequencesException(bracket);
        }
        lexemes.add(new Lexeme(LexType.EOF, ""));
        return lexemes;
    }

    public Expression<T> expr(Lex lexemes) throws Exception {
        return plusMinus(lexemes);
    }

    private Expression<T> plusMinus(Lex lexemes) throws Exception {
        Expression<T> value = multDiv(lexemes);
        while (true) {
            Lexeme lexeme = lexemes.getLex();
            switch (lexeme.type) {
                case PLUS:
                    value = new CheckedAdd<>(value, factor(lexemes), type);
                    break;
                case MINUS:
                    value = new CheckedSubtract<>(value, factor(lexemes), type);
                    break;
                default:
                    lexemes.back();
                    return value;
            }
        }
    }

    private Expression<T> multDiv(Lex lexemes) throws Exception {
        Expression<T> value = factor(lexemes);
        while (true) {
            Lexeme lexeme = lexemes.getLex();
            switch (lexeme.type) {
                case MUL:
                    value = new CheckedMultiply<>(value, factor(lexemes), type);
                    break;
                case DIV:
                    value = new CheckedDivide<>(value, factor(lexemes), type);
                    break;
                default:
                    lexemes.back();
                    return value;
            }
        }
    }

    private Expression<T> factor(Lex lexemes) throws Exception {
        Lexeme lexeme = lexemes.getLex();
        return switch (lexeme.type) {
            case NUMBER -> new Const<>(type.parse(lexeme.value));
            case MINUS -> new CheckedNegate<>(factor(lexemes), type);
            case LB -> {
                Expression<T> result = expr(lexemes);
                lexemes.getLex();
                yield result;
            }
            case VAR -> new Variable<>(lexeme.value);
            default -> throw new MissingExpressionException("At index: " + lexeme.indx);
        };
    }

}
