import java.io.*;
import java.util.*;

public class WordStatCountPrefixL {
	public static void main(String[] args) {
		String fileInputName = args[0];
		String fileOutputName = args[1];
		StringBuilder sb = new StringBuilder();
		Map<String, Integer> uniqWordsCnt = new LinkedHashMap<>();
		try {
			Reader reader = new BufferedReader(
				new InputStreamReader(
					new FileInputStream(fileInputName),
					"utf8"
				)
			);
			try {
				int len = 0;
				while (len != -1) {
					char[] ch = new char[1024];
					len = reader.read(ch);
					
					for (int i = 0; i < len; i++) {
						if (Character.getType(ch[i]) == Character.DASH_PUNCTUATION || 
						Character.isLetter(ch[i]) || ch[i] == '\'') {
							sb.append(ch[i]);
						} else if (sb.length() > 0 && sb.length() < 3 ) {
							sb = new StringBuilder();
						} else if (sb.length() >= 3) {
							String word = sb.toString().substring(0, 3).toLowerCase();
							sb = new StringBuilder();
							
							uniqWordsCnt.put(word, uniqWordsCnt.getOrDefault(word, 0) + 1);
						}
					}
				}
				
				if (sb.length() > 2) {
					String word = sb.toString().substring(0,3).toLowerCase();
					sb = new StringBuilder();
					
					uniqWordsCnt.put(word, uniqWordsCnt.getOrDefault(word, 0) + 1);
				}
				ArrayList<Integer> cnts = new ArrayList<Integer>();
				for (int cnt : uniqWordsCnt.values()) {
					if (!cnts.contains(cnt)) {
						cnts.add(cnt);
					}
				}
				Collections.sort(cnts);
				Writer writer = new BufferedWriter(
					new OutputStreamWriter(
						new FileOutputStream(fileOutputName), 
						"utf8"
					)
				);
				try {
					for (int cnt : cnts) {
						for (String key : uniqWordsCnt.keySet()) {
							if (uniqWordsCnt.get(key) == cnt) {
								writer.write(key + " " + cnt + "\n");
							}
						}
					}
				} catch (IOException ex) {
					System.err.println(ex.getMessage());
				} finally {
					writer.close();
				}
			} catch (IOException ex) {
				System.err.println(ex.getMessage());
			} finally {
				reader.close();
			}
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (UnsupportedEncodingException  ex) {
			System.err.println(ex.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
}