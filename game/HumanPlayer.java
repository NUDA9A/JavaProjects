package game;

import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class HumanPlayer implements Player {
    private final PrintStream out;
    private final Scanner in;

    private final int number;

    private HumanPlayer(final PrintStream out, final Scanner in, final int number) {
        this.out = out;
        this.in = in;
        this.number = number;
    }

    @Override
    public int getNumber() {
        return this.number;
    }

    public HumanPlayer(int number) {
        this(System.out, new Scanner(System.in), number);
    }

    public HumanPlayer() {
        this(System.out, new Scanner(System.in), -1);
    }

    @Override
    public Move move(final Position position, final Cell cell) {
        while (true) {
            out.println();
            out.println("Position");
            out.println(position);
            out.println(cell + "'s move");
            out.println("Enter row and column");
            String row;
            String column;
            try {
                row = in.next();
                column = in.next();
            } catch (NoSuchElementException nse) {
                out.println("You entered wrong symbol combination. Good Luck!");
                throw new IllegalStateException();
            }
            if (!isInteger(row)) {
                out.println("Your row number is invalid. Please try again.");
                continue;
            }
            if (!isInteger(column)) {
                out.println("Your column number is invalid. Please try again.");
                continue;
            }
            int r = Integer.parseInt(row);
            int c = Integer.parseInt(column);
            final Move move = new Move(r - 1, c - 1, cell);
            if (position.isValid(move)) {
                return move;
            }
            out.println("Move " + move + " is invalid");
        }
    }

    private boolean isInteger(String s) {
        int number;
        try {
            number = Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
