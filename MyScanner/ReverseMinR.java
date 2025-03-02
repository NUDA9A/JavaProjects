import java.util.Scanner;
import java.util.Arrays;

public class ReverseMinR {
	public static void main(String[] args) {
		Scanner systemScanner = new Scanner(System.in);
		int n = (int) 10e6;
		int[][] ints = new int[n][];
		int i;
		int lines = 0;
		
		while (systemScanner.hasNextLine()) {
			i = 0;
			String s = systemScanner.nextLine();
			
			Scanner lineScanner = new Scanner(s);
			int[] innerInts = new int[s.length()];
			
			while (lineScanner.hasNextInt()) {
				innerInts[i++] = lineScanner.nextInt();
			}
			
			lineScanner.close();
			
			
			ints[lines++] = Arrays.copyOf(innerInts, i);
		}
		
		systemScanner.close();
		
		for (i = 0; i < lines; i++) {
			int mn = Integer.MAX_VALUE;
			for (int j = 0; j < ints[i].length; j++) {
				if (ints[i][j] < mn) {
					mn = ints[i][j];
				}
				System.out.print(mn + " ");
			}
			System.out.println();
		}
	}
}