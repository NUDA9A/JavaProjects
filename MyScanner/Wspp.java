import java.io.*;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Wspp {
    public static void main(String[] args) throws IOException {
        String fileInputName = args[0];
        String fileOutputName = args[1];
        StringBuilder sb = new StringBuilder();
        LinkedHashMap<String, Integer> uniqWordsCnt = new LinkedHashMap<>();
        HashMap<String, String> wordPositions = new HashMap<>();
        ArrayList<String> allWords = new ArrayList<>();
        try {
            Scanner reader = new Scanner(new InputStreamReader(new FileInputStream(fileInputName), "utf8"));
            try {
                while (reader.hasNext()) {
					try {
						String word = reader.next();
						allWords.add(word);
						
					} catch (NoSuchElementException noe) {
					}
                }
                int position = 1;
                for (String word : allWords) {
                    uniqWordsCnt.put(word, uniqWordsCnt.getOrDefault(word, 0) + 1);
                    wordPositions.put(word, wordPositions.getOrDefault(word, "") + position + " ");
                    position++;
                }
                Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileOutputName), "utf8"));
                try {
                    for (String key : uniqWordsCnt.keySet()) {
                        writer.write(key + " " + uniqWordsCnt.get(key)+ " " + wordPositions.get(key).strip() + "\n");
                    }
                } catch (IOException ioe) {
                    System.err.println(ioe.getMessage());
                } finally {
                    writer.close();
                }
            } catch (IOException ioe) {
                System.err.println(ioe.getMessage());
            } finally {
                reader.close();
            }
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            System.err.println(e.getMessage());
        }
    }
}
