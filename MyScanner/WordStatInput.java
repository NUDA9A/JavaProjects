import java.io.*;
import java.util.*;

public class WordStatInput {
	public static void main(String[] args) {
		String fileInputName = args[0];
		String fileOutputName = args[1];
		StringBuilder sb = new StringBuilder();
		LinkedHashMap<String, Integer> uniqWordsCnt = new LinkedHashMap<>();
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
					char[] ch = new char[4000];
					len = reader.read(ch);
					
					for (int i = 0; i < len; i++) {
						if (Character.getType(ch[i]) == Character.DASH_PUNCTUATION || 
						Character.isLetter(ch[i]) || ch[i] == '\'') {
							sb.append(ch[i]);
						} else if (sb.length() > 0) {
							String word = sb.toString().toLowerCase();
							sb = new StringBuilder();
							
							if (uniqWordsCnt.get(word) == null) {
								uniqWordsCnt.put(word, 1);
							} else {
								uniqWordsCnt.put(word, uniqWordsCnt.get(word) + 1);
							}
						}
					}
				}
				
				if (sb.length() > 0) {
					String word = sb.toString().toLowerCase();
					sb = new StringBuilder();
					
					if (uniqWordsCnt.get(word) == null) {
						uniqWordsCnt.put(word, 1);
					} else {
						uniqWordsCnt.put(word, uniqWordsCnt.get(word) + 1);
					}
				}
				Writer writer = new BufferedWriter(
					new OutputStreamWriter(
						new FileOutputStream(fileOutputName), 
						"utf8"
					)
				);
				try {
					for (String key : uniqWordsCnt.keySet()) {
						writer.write(key + " " + uniqWordsCnt.get(key) + "\n");
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
		} catch (UnsupportedEncodingException ex) {
			System.err.println(ex.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
}