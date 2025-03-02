import java.util.Arrays;

public class ReverseMinRAbc {
    public static void main(String[] args) {
        Scanner systemScanner = new Scanner(System.in);
        int n = 100;
        String[][] abcInts = new String[n][];
        int lines = 0;
        while (systemScanner.hasNextLine()) {
            if (lines == abcInts.length) {
                abcInts = Arrays.copyOf(abcInts, abcInts.length * 2);
            }
            abcInts[lines++] = systemScanner.abcIntsInLine();
        }
        systemScanner.close();

        for (int i = 0; i < lines; i++) {
            int min = Integer.MAX_VALUE;
            if (abcInts[i].length == 0) {
                System.out.println();
                continue;
            }
            String mn = abcInts[i][0];
            for (int j = 0; j < abcInts[i].length; j++) {
                try {
                    int currValue = toNum(abcInts[i][j]);
                    if (currValue < min) {
                        min = currValue;
                        mn = abcInts[i][j];
                    }
                    System.out.print(mn + " ");
                } catch (NumberFormatException nfe) {
                    continue;
                }
            }
            System.out.println();
        }
    }

    public static int toNum(String s) {
        StringBuilder numBuilder = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != '-') {
                numBuilder.append(((int)s.charAt(i) - 97));
            } else {
                numBuilder.append(s.charAt(i));
            }
        }
        try {
            int result = Integer.parseInt(numBuilder.toString());
            return Integer.parseInt(numBuilder.toString());
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException();
        }
    }
}