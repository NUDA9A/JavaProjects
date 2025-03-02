package game;

public class SequentialPLayer implements Player {

    private final int m, n, number;

    public SequentialPLayer(int m, int n, int number) {
        this.m = m;
        this.n = n;
        this.number = number;
    }

    @Override
    public int getNumber() {
        return number;
    }

    public SequentialPLayer(int m, int n) {
        this(m, n, -1);
    }

    @Override
    public Move move(final Position position, final Cell cell) {
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                final Move move = new Move(r, c, cell);
                if (position.isValid(move)) {
                    return move;
                }
            }
        }
        throw new IllegalStateException();
    }
}
