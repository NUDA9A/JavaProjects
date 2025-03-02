package game;

import java.util.Arrays;
import java.util.Map;

public class MNKBoard implements Board, Position {
    private static final Map<Cell, Character> SYMBOLS = Map.of(
            Cell.X, 'X',
            Cell.O, 'O',
            Cell.E, '.'
    );

    private final Cell[][] cells;
    private Cell turn;

    public final int m, n, k;

    public MNKBoard(int m, int n, int k) {
        this.cells = new Cell[m][n];
        this.k = k;
        this.m = m;
        this.n = n;
        for (Cell[] row : cells) {
            Arrays.fill(row, Cell.E);
        }
        turn = Cell.X;
    }

    @Override
    public Position getPosition() {
        return this;
    }

    @Override
    public Cell getCell() {
        return turn;
    }

    @Override
    public Result makeMove(final Move move) {
        if (!isValid(move)) {
            return Result.LOSE;
        }

        cells[move.getRow()][move.getColumn()] = move.getValue();

        if (m >= k) {
            for (int r = 0; r < m; r++) {
                int inRow = 0;
                for (int c = 0; c < n; c++) {
                    if (cells[r][c] == turn) {
                        inRow++;
                        if (inRow == k) {
                            return Result.WIN;
                        }
                    } else {
                        inRow = 0;
                    }
                }
            }
        }
        if (n >= k) {
            for (int c = 0; c < n; c++) {
                int inColumn = 0;
                for (int r = 0; r < m; r++) {
                    if (cells[r][c] == turn) {
                        inColumn++;
                        if (inColumn == k) {
                            return Result.WIN;
                        }
                    } else {
                        inColumn = 0;
                    }
                }
            }
        }
        if (m >= k && n >= k) {
            for (int r = 0; r <= m - k; r++) {
                for (int c = 0; c <= n - k; c++) {
                    int rDiagonal = moveDiagonal(r, c, true);
                    int lDiagonal = moveDiagonal(r, n - c - 1, false);
                    if (rDiagonal == k || lDiagonal == k) {
                        return Result.WIN;
                    }
                }
            }
        }

        int empty = 0;
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                if (cells[r][c] == Cell.E) {
                    empty++;
                }
            }
        }
        if (empty == 0) {
            return Result.DRAW;
        }

        turn = turn == Cell.X ? Cell.O : Cell.X;
        return Result.UNKNOWN;
    }

    @Override
    public boolean isValid(final Move move) {
        return 0 <= move.getRow() && move.getRow() < m
                && 0 <= move.getColumn() && move.getColumn() < n
                && cells[move.getRow()][move.getColumn()] == Cell.E
                && turn == getCell();
    }

    @Override
    public Cell getCell(final int r, final int c) {
        return cells[r][c];
    }

    @Override
    public String toString() {
        StringBuilder colNum = new StringBuilder();
        for (int i = 1; i <= n; i++) {
            colNum.append(i);
            colNum.append(' ');
        }
        final StringBuilder sb = new StringBuilder("  " + colNum);
        for (int r = 0; r < m; r++) {
            sb.append(System.lineSeparator());
            sb.append(r + 1);
            sb.append(' ');
            for (int c = 0; c < n; c++) {
                sb.append(SYMBOLS.get(cells[r][c]));
                if (c < 9) {
                    sb.append(' ');
                } else {
                    sb.append("  ");
                }
            }
        }
        return sb.toString();
    }

    private int moveDiagonal(int r, int c, boolean direction) {
        int inDiagonal = 0;
        if (direction) {
            for (int i = 0; i < k; i++) {
                if (cells[r + i][c + i] == turn) {
                    inDiagonal++;
                }
            }
        } else {
            for (int i = 0; i < k; i++) {
                if (cells[r + i][c - i] == turn) {
                    inDiagonal++;
                }
            }
        }
        return inDiagonal;
    }
}
