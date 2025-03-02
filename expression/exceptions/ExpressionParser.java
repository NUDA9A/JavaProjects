package expression.exceptions;


import expression.Variable;
import expression.Const;
import expression.And;
import expression.Xor;
import expression.Or;
import expression.TripleExpression;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import expression.AbstractExpression;

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
    public TripleExpression parse(String expression) throws Exception {
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

    private int parseVar(StringBuilder sb, int i, String s) throws Exception {
        while (i < s.length() && Character.isLetter(s.charAt(i))) {
            if (Character.isUpperCase(s.charAt(i)) || (s.charAt(i) != 'x' && s.charAt(i) != 'y'
            && s.charAt(i) != 'z')) {
                sb.append(s.charAt(i));
                throw new UnknownSymbolException(sb.toString());
            }
            sb.append(s.charAt(i++));
        }
        return i - 1;
    }

    private List<Lexeme> lexAnalyze(String expression) throws Exception {
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
                lexemes.add(new Lexeme(LexType.NUMBER, num.toString()));
                lexType = LexType.NUMBER;
                op = 0;
                if (!isFirst) {
                    isFirst = true;
                }
                continue;
            } else if (Character.isLetter(c)) {
                if (c == 'l' || c == 't') {
                    if (i + 1 < expression.length() && expression.charAt(i + 1) == '0') {
                        if (i + 2 < expression.length() && (Character.isLetter(expression.charAt(i + 2)) ||
                                Character.isDigit(expression.charAt(i + 2)))) {
                            throw new UnknownOperandException(expression.substring(i, i + 3));
                        }
                        i++;
                        if (c == 'l') {
                            lexemes.add(new Lexeme(LexType.L0, "l0"));
                            continue;
                        }
                        lexemes.add(new Lexeme(LexType.T0, "t0"));
                        continue;
                    }
                }
                StringBuilder var = new StringBuilder();
                if (lexType == LexType.VAR) {
                    throw new MissingOperandException();
                }
                i = parseVar(var, i, expression);
                lexemes.add(new Lexeme(LexType.VAR, var.toString()));
                lexType = LexType.VAR;
                op = 0;
                if (!isFirst) {
                    isFirst = true;
                }
                continue;
            }

            int len = lexemes.size();

            for (String key : l.keySet()) {
                if (String.valueOf(c).equals(key)) {
                    if (key.equals("-")) {
                        if (i == expression.length() - 1) {
                            throw new MissingExpressionException();
                        } else if (Character.isDigit(expression.charAt(i + 1)) && (op > 0 || !isFirst)) {
                            StringBuilder num = new StringBuilder();
                            num.append(key);
                            if (lexType == LexType.NUMBER) {
                                throw new MissingOperandException();
                            }
                            i = parseNum(num, i + 1, expression);
                            lexemes.add(new Lexeme(LexType.NUMBER, num.toString()));
                            lexType = LexType.NUMBER;
                            op = 0;
                            if (!isFirst) {
                                isFirst = true;
                            }
                            break;
                        }
                    }
                    lexemes.add(new Lexeme(l.get(key), key));
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

    private AbstractExpression factor(Lex lexemes) throws Exception {
        Lexeme lexeme = lexemes.getLex();
        return switch (lexeme.type) {
            case NUMBER -> {
                int num;
                try {
                    num = Integer.parseInt(lexeme.value);
                } catch (NumberFormatException nfe) {
                    if (lexeme.value.charAt(0) == '-') {
                        throw new ConstantOverflowException(1);
                    }
                    throw new ConstantOverflowException(2);
                }
                yield new Const(num);
            }
            case MINUS -> new CheckedNegate(factor(lexemes));
            case LB -> {
                AbstractExpression result = expr(lexemes);
                lexemes.getLex();
                yield result;
            }
            case VAR -> new Variable(lexeme.value);
            case L0 -> new CheckedL0(factor(lexemes));
            case T0 -> new CheckedT0(factor(lexemes));
            default -> throw new MissingExpressionException();
        };
    }

    private AbstractExpression expr(Lex lexemes) throws Exception {
        return or(lexemes);
    }

    private AbstractExpression multDiv(Lex lexemes) throws Exception {
        AbstractExpression value = factor(lexemes);
        while (true) {
            Lexeme lexeme = lexemes.getLex();
            switch (lexeme.type) {
                case MUL:
                    value = new CheckedMultiply(value, factor(lexemes));
                    break;
                case DIV:
                    value = new CheckedDivide(value, factor(lexemes));
                    break;
                default:
                    lexemes.back();
                    return value;
            }
        }
    }

    private AbstractExpression plusMinus(Lex lexemes) throws Exception {
        AbstractExpression value = multDiv(lexemes);
        while (true) {
            Lexeme lexeme = lexemes.getLex();
            switch (lexeme.type) {
                case PLUS:
                    value = new CheckedAdd(value, multDiv(lexemes));
                    break;
                case MINUS:
                    value = new CheckedSubtract(value, multDiv(lexemes));
                    break;
                default:
                    lexemes.back();
                    return value;
            }
        }
    }

    private AbstractExpression and(Lex lexemes) throws Exception {
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

    private AbstractExpression or(Lex lexemes) throws Exception {
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

    private AbstractExpression xor(Lex lexemes) throws Exception {
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
