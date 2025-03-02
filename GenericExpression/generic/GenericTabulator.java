package expression.generic;


import java.util.List;
import java.util.Map;

public class GenericTabulator implements Tabulator {
    private final Map<String, GenericUtils<?>> types = Map.of(
            "i", new IntegerT(true),
            "bi", new BigIntegerT(),
            "d", new DoubleT(),
            "u", new IntegerT(false),
            "b", new ByteT()
            );
    private <T> void eval(
            Object[][][] res,
            int x1, int x2,
            int y1, int y2,
            int z1, int z2,
            Expression<T> ex,
            GenericUtils<T> type
    ) {
        for (int i = 0; i < Math.abs(x2 - x1 + 1); i++) {
            for (int j = 0; j < Math.abs(y2 - y1 + 1); j++) {
                for (int k = 0; k < Math.abs(z2 - z1 + 1); k++) {
                    try {
                        res[i][j][k] = ex.evaluate(type.getValue(x1 + i), type.getValue(y1 + j), type.getValue(z1 + k));
                    } catch (Exception exc) {
                        res[i][j][k] = null;
                    }
                }
            }
        }
    }

    private GenericUtils<?> getType(String mode) {
        return types.get(mode);
    }

    private <T> Object[][][] myTabulate(
            Object[][][] res,
            GenericUtils<T> type,
            String expression,
            int x1, int x2,
            int y1, int y2,
            int z1, int z2
    ) throws Exception {
        GenericExpressionParser<T> parser = new GenericExpressionParser<>(type);
        List<Lexeme> lexemes = parser.lexAnalyze(expression);
        Lex lex = new Lex(lexemes);
        Expression<T> ex = parser.expr(lex);
        eval(res, x1, x2, y1, y2, z1, z2, ex, type);
        return res;
    }

    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        Object[][][] result = new Object[Math.abs(x2 - x1 + 1)][Math.abs(y2 - y1 + 1)][Math.abs(z2 - z1 + 1)];

        return myTabulate(result, getType(mode), expression, x1, x2, y1, y2, z1, z2);
    }
}
