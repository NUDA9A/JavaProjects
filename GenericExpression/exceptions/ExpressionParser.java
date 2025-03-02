package expression.exceptions;


import expression.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExpressionParser implements TripleParser, ListParser {
    private final Map<String, LexType> l = Map.of(
            "(", LexType.LB,
            ")", LexType.RB,
            "*", LexType.MUL,
            "/", LexType.DIV,
            "+", LexType.PLUS,
            "-", LexType.MINUS
    );

    private List<String> vars;
    // :NOTE: isList?
    private boolean list;

    @Override
    public TripleExpression parse(String expression) throws Exception {
        this.list = false;
        List<Lexeme> lexemes = lexAnalyze(expression);
        Lex lex = new Lex(lexemes);
        return expr(lex);
    }

    @Override
    public ListExpression parse(String expression, List<String> variables) throws Exception {
        this.list = true;
        this.vars = variables;
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

    private int listParseVar(StringBuilder sb, int i, String s) {
        while (i < s.length() && !Character.isWhitespace(s.charAt(i))) {
            if (l.containsKey(s.substring(i, i + 1))) {
                break;
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
                lexemes.add(new Lexeme(LexType.NUMBER, num.toString(), i));
                lexType = LexType.NUMBER;
                op = 0;
                if (!isFirst) {
                    isFirst = true;
                }
                continue;
            } else if (Character.isLetter(c) || c == '$') {
                StringBuilder symbols = new StringBuilder();
                i = listParseVar(symbols, i, expression);
                if (symbols.toString().equals("t0")) {
                    lexemes.add(new Lexeme(LexType.T0, "t0", i));
                    continue;
                } else if (symbols.toString().equals("l0")) {
                    lexemes.add(new Lexeme(LexType.L0, "l0", i));
                    continue;
                } else {
                    if (lexType == LexType.VAR) {
                        throw new MissingOperandException();
                    }
                    if (list) {
                        if (vars.contains(symbols.toString())) {
                            lexemes.add(new Lexeme(LexType.VAR, vars.indexOf(symbols.toString())));
                            lexType = LexType.VAR;
                            op = 0;
                            if (!isFirst) {
                                isFirst = true;
                            }
                            continue;
                        } else {
                            throw new UnknownSymbolException(symbols.toString());
                        }
                    } else {
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
                }
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

    private AbstractExpression expr(Lex lexemes) throws Exception {
        return plusMinus(lexemes);
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
            case VAR -> {
                if (list) {
                    yield new Variable(lexeme.indx, vars.get(lexeme.indx));
                }
                yield new Variable(lexeme.value);
            }
            case L0 -> new CheckedL0(factor(lexemes));
            case T0 -> new CheckedT0(factor(lexemes));
            default -> throw new MissingExpressionException("At index: " + lexeme.indx);
        };
    }
}
