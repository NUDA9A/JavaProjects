public class SumDouble {
	public static void main(String[] args) {
		double summ = 0;
		
		for (String word : args) {
			for (int i = 0; i < word.length(); i++) {
				if ((word.charAt(i) == '-') || (Character.isDigit(word.charAt(i)))) {
					int j = i;
					i++;
					
					while (i < word.length() && !Character.isWhitespace(word.charAt(i))) {
						i++;
					}
					
					summ += Double.parseDouble(word.substring(j,i));
				}
			}
		}
		System.out.println(summ);
	}
}