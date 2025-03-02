package game;

import java.util.Random;

public class RandomPlayer implements Player {
    private final Random random;
    private final int m, n, number;

    private RandomPlayer(final Random random, int m, int n, int number) {
        this.random = random;
        this.m = m;
        this.n = n;
        this.number = number;
    }

    public RandomPlayer(int m, int n, int number) {
        this(new Random(), m, n, number);
    }

    public RandomPlayer(int m, int n) {
        this(new Random(), m, n, -1);
    }

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public Move move(final Position position, final Cell cell) {
        int r = random.nextInt(m);
        int c = random.nextInt(n);
        final Move move = new Move(r, c, cell);
        if (position.isValid(move)) {
            return move;
        } else {
            throw new IllegalStateException();
        }
    }
}
