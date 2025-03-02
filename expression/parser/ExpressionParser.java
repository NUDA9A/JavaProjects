package expression.parser;

import expression.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExpressionParser implements TripleParser {

    private final Map<String, LexType> l = Map.of(
            "(", LexType.LB,
            ")", LexType.RB,
            "&", LexType.AND,
            "|", LexType.OR,
            "^", LexType.XOR,
            "*", LexType.MUL,
            "/", LexType.DIV,
            "+", LexType.PLUS,
            "-", LexType.MINUS
    );

    @Override
    public TripleExpression parse(String expression) {
        List<Lexeme> lexemes = lexAnalyze(expression);
        Lex lex = new Lex(lexemes);
        return expr(lex);
    }

    private int parseNum(StringBuilder sb, int i, String s) {
        while (i < s.length() && Character.isDigit(s.charAt(i))) {
            sb.append(s.charAt(i++));
        }
        return i - 1;
    }

    private int parseVar(StringBuilder sb, int i, String s) {
        while (i < s.length() && Character.isLetter(s.charAt(i))) {
            sb.append(s.charAt(i++));
        }
        return i - 1;
    }

    private List<Lexeme> lexAnalyze(String expression) {
        int op = 0;
        boolean isFirst = false;
        List<Lexeme> lexemes = new ArrayList<>();
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (Character.isWhitespace(c)) {
                continue;
            } else if (Character.isDigit(c)) {
                StringBuilder num = new StringBuilder();
                i = parseNum(num, i, expression);
                lexemes.add(new Lexeme(LexType.NUMBER, num.toString()));
                op = 0;
                if (!isFirst) {
                    isFirst = true;
                }
                continue;
            } else if (Character.isLetter(c)) {
                StringBuilder var = new StringBuilder();
                i = parseVar(var, i, expression);
                lexemes.add(new Lexeme(LexType.VAR, var.toString()));
                op = 0;
                if (!isFirst) {
                    isFirst = true;
                }
                continue;
            }

            for (String key : l.keySet()) {
                if (String.valueOf(c).equals(key)) {
                    if (key.equals("-") && Character.isDigit(expression.charAt(i + 1)) && (op > 0 || !isFirst)) {
                        StringBuilder num = new StringBuilder();
                        num.append(key);
                        i = parseNum(num, i + 1, expression);
                        lexemes.add(new Lexeme(LexType.NUMBER, num.toString()));
                        op = 0;
                        if (!isFirst) {
                            isFirst = true;
                        }
                        break;
                    }
                    lexemes.add(new Lexeme(l.get(key), key));
                    op++;
                    if (key.equals("(") || key.equals(")")) {
                        op--;
                    }
                    break;
                }
            }
        }
        lexemes.add(new Lexeme(LexType.EOF, ""));
        return lexemes;
    }

    private AbstractExpression factor(Lex lexemes) {
        Lexeme lexeme = lexemes.getLex();
        return switch (lexeme.type) {
            case NUMBER -> new Const(Integer.parseInt(lexeme.value));
            case MINUS -> new Minus(factor(lexemes));
            case LB -> {
                AbstractExpression result =  expr(lexemes);
                lexemes.getLex();
                yield result;
            }
            case VAR -> new Variable(lexeme.value);
            default -> null;
        };
    }

    private AbstractExpression expr(Lex lexemes) {
        return or(lexemes);
    }

    private AbstractExpression multDiv(Lex lexemes) {
        AbstractExpression value = factor(lexemes);
        while (true) {
            Lexeme lexeme = lexemes.getLex();
            switch (lexeme.type) {
                case MUL:
                    value = new Multiply(value, factor(lexemes));
                    break;
                case DIV:
                    value = new Divide(value, factor(lexemes));
                    break;
                default:
                    lexemes.back();
                    return value;
            }
        }
    }

    private AbstractExpression plusMinus(Lex lexemes) {
        AbstractExpression value = multDiv(lexemes);
        while (true) {
            Lexeme lexeme = lexemes.getLex();
            switch (lexeme.type) {
                case PLUS:
                    value = new Add(value, multDiv(lexemes));
                    break;
                case MINUS:
                    value = new Subtract(value, multDiv(lexemes));
                    break;
                default:
                    lexemes.back();
                    return value;
            }
        }
    }

    private AbstractExpression and(Lex lexemes) {
        AbstractExpression value = plusMinus(lexemes);
        while (true) {
            Lexeme lexeme = lexemes.getLex();
            if (lexeme.type == LexType.AND) {
                value = new And(value, plusMinus(lexemes));
            } else {
                lexemes.back();
                return value;
            }
        }
    }

    private AbstractExpression or(Lex lexemes) {
        AbstractExpression value = xor(lexemes);
        while (true) {
            Lexeme lexeme = lexemes.getLex();
            if (lexeme.type == LexType.OR) {
                value = new Or(value, xor(lexemes));
            } else {
                lexemes.back();
                return value;
            }
        }
    }

    private AbstractExpression xor(Lex lexemes) {
        AbstractExpression value = and(lexemes);
        while (true) {
            Lexeme lexeme = lexemes.getLex();
            if (lexeme.type == LexType.XOR) {
                value = new Xor(value, and(lexemes));
            } else {
                lexemes.back();
                return value;
            }
        }
    }
}
