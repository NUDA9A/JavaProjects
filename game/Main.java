package game;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class Main {
    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            int m = scanValue("m", sc);
            System.out.println();
            int n = scanValue("n", sc);
            System.out.println();
            int k = scanValue("k", sc);
            System.out.println();
            int playersAmount = scanValue("Players amount", sc);
            List<Player> players = new ArrayList<>();
            for (int i = 0; i < playersAmount; i++) {
                players.add(new HumanPlayer(i + 1));
            }
            final OlympGame game = new OlympGame(m, n, k, players);
            game.play();
            sc.close();
        } catch (NoSuchElementException nse) {
            System.err.println("You entered wrong symbol combination.");
        }
    }

    public static int scanValue(String s, Scanner sc) {
        int res;
        while (true) {
            System.out.print("Enter " + s + ": ");
            String str = sc.next();
            if (isInteger(str)) {
                res = Integer.parseInt(str);
                if (s.equals("Players amount")) {
                    if (res > 1) {
                        break;
                    } else {
                        System.out.println(s + " must be 2 at least.");
                    }
                } else {
                    if (res > 0) {
                        break;
                    } else {
                        System.out.println(s + " must be 1 at least.");
                    }
                }
            } else {
                System.out.println(str + " is not a valid number");
            }
        }
        return res;
    }

    public static boolean isInteger(String s) {
        int res;
        try {
            res = Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
