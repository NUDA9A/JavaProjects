package expression.generic;


import java.util.List;
import java.util.Map;

public class Main {
    private static final Map<String, GenericUtils<?>> types = Map.of(
            "i", new IntegerT(true),
            "bi", new BigIntegerT(),
            "d", new DoubleT(),
            "u", new IntegerT(false),
            "b", new ByteT()
    );
    private static <T> void eval(
            GenericUtils<T> type,
            Expression<T> ex
    ) {
        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 2; y++) {
                for (int z = -2; z <= 2; z++) {
                    try {
                        System.out.println(ex.evaluate(type.getValue(x), type.getValue(y), type.getValue(z)));
                    } catch (Exception exc) {
                        System.out.println("NaN");
                    }
                }
            }
        }
    }

    private static GenericUtils<?> getType(String mode) {
        return types.get(mode);
    }

    private static <T> void gen(String expression, GenericUtils<T> type) throws Exception {
        GenericExpressionParser<T> parser = new GenericExpressionParser<>(type);
        List<Lexeme> lexemes = parser.lexAnalyze(expression);
        Lex lex = new Lex(lexemes);
        Expression<T> ex = parser.expr(lex);
        eval(type, ex);
    }

    public static void main(String[] args) throws Exception {
        String mode = args[0];
        String expression = args[1];

        gen(expression, getType(mode));
    }
}
