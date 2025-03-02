import java.io.*;
import java.util.NoSuchElementException;
import java.util.Arrays;

public class Reverse {
    public static void main(String[] args) {
        Scanner systemScanner = new Scanner(System.in);
        int n = 100;
        int[][] ints = new int[n][];
        int lines = 0;
        while (systemScanner.hasNextLine()) {
            if (lines == ints.length) {
                ints = Arrays.copyOf(ints, ints.length * 2);
            }
            ints[lines++] = systemScanner.intsInLine();
        }
        systemScanner.close();

        for (int i = lines - 1; i >= 0; i--) {
            for (int j = ints[i].length - 1; j >= 0; j--) {
                System.out.print(ints[i][j] + " ");
            }
            System.out.println();
        }
    }
}