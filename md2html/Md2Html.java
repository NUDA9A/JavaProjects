package md2html;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Md2Html {
	public static int skipNullLines(List<String> lines, int currIndx) {
		while (true) {
			if (currIndx == lines.size()) {
				return -1;
			}
			if (lines.get(currIndx).isEmpty()) {
				currIndx++;
			} else {
				return currIndx;
			}
		}
	}
	
	public static int getParagraph(List<String> lines, StringBuilder p, int currIndx) {
		while (true) {
			p.append(lines.get(currIndx++));
			if (currIndx == lines.size() || lines.get(currIndx).isEmpty()) {
				break;
			} else {
				p.append("\n"); // System.lineSeparator()
			}
		}
		return currIndx;
	}
	
    public static void main(String[] args) throws IOException {
        String fileInputName = args[0];
        String fileOutputName = args[1];
        String paragraphOpen = "<p>";
        String paragraphClose = "</p>";
        String strongOpen = "<strong>";
        String strongClose = "</strong>";
        String codeOpen = "<code>";
        String codeClose = "</code>";
        String emphasisOpen = "<em>";
        String emphasisClose = "</em>";
        String strikeoutOpen = "<s>";
        String strikeoutClose = "</s>";
		String quoteOpen = "<q>";
		String quoteClose = "</q>";
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileInputName), StandardCharsets.UTF_8))) {
            List<String> lines = new ArrayList<>();
            List<String> paragraphs = new ArrayList<>();
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                lines.add(line);
            }
            int currIndx = 0;
            while (true) {
                boolean strong = true;
                boolean emphasis = true;
                boolean code = true;
                boolean strikeout = true;
                boolean quote = true;
                StringBuilder p = new StringBuilder();
                currIndx = skipNullLines(lines, currIndx);
                if (currIndx == -1) {
                    break;
                }
                currIndx = getParagraph(lines, p, currIndx);
                String paragraph = p.toString();

                boolean isHead = isHead(paragraph);
                int currParagraphIndx = 0;

                if (isHead) {
                    int headCount = countHead(paragraph);
                    String head = "<h" + headCount + ">";
                    sb.append(head);
                    currParagraphIndx += headCount + 1;
                } else {
                    sb.append(paragraphOpen);
                }

                int countEmph1 = count(paragraph, '*');
                int countEmph2 = count(paragraph, '_');

                for (int i = currParagraphIndx; i < paragraph.length(); i++) {
                    if (paragraph.charAt(i) == '*' && countEmph1 > 1) {
                        if (i + 1 < paragraph.length() && paragraph.charAt(i + 1) == '*') {
                            if (strong) {
                                sb.append(strongOpen);
                                strong = false;
                            } else {
                                sb.append(strongClose);
                                strong = true;
                            }
                            i++;
                        } else if (i + 1 < paragraph.length() && (!Character.isWhitespace(paragraph.charAt(i + 1)) || !emphasis)) {
                            if (emphasis) {
                                sb.append(emphasisOpen);
                                emphasis = false;
                            } else {
                                sb.append(emphasisClose);
                                emphasis = true;
                            }
                        } else {
                            sb.append(paragraph.charAt(i));
                        }
                        continue;
                    }
                    if (paragraph.charAt(i) == '_' && countEmph2 > 1) {
                        if (i + 1 < paragraph.length() && paragraph.charAt(i + 1) == '_') {
                            if (strong) {
                                sb.append(strongOpen);
                                strong = false;
                            } else {
                                sb.append(strongClose);
                                strong = true;
                            }
                            i++;
                        } else if (i + 1 < paragraph.length() && (!Character.isWhitespace(paragraph.charAt(i + 1)) || !emphasis)) {
                            if (emphasis) {
                                sb.append(emphasisOpen);
                                emphasis = false;
                            } else {
                                sb.append(emphasisClose);
                                emphasis = true;
                            }
                        } else {
                            sb.append(paragraph.charAt(i));
                            continue;
                        }
                        continue;
                    }
                    if (paragraph.charAt(i) == '-') {
                        if (i + 1 < paragraph.length() && paragraph.charAt(i + 1) == '-') {
                            if (strikeout) {
                                sb.append(strikeoutOpen);
                                strikeout = false;
                            } else {
                                sb.append(strikeoutClose);
                                strikeout = true;
                            }
                            i++;
                        } else {
                            sb.append(paragraph.charAt(i));
                        }
                        continue;
                    }
                    if (paragraph.charAt(i) == '`') {
                        if (code) {
                            sb.append(codeOpen);
                            code = false;
                        } else {
                            sb.append(codeClose);
                            code = true;
                        }
                        continue;
                    }
                    if (paragraph.charAt(i) == '\\') {
                        if (i + 1 < paragraph.length()) {
                            sb.append(paragraph.charAt(i + 1));
                            i++;
                        }
                        continue;
                    }
                    if (paragraph.charAt(i) == '&') {
                        sb.append("&amp;");
                        continue;
                    }
                    if (paragraph.charAt(i) == '<') {
                        sb.append("&lt;");
                        continue;
                    }
                    if (paragraph.charAt(i) == '>') {
                        sb.append("&gt;");
                        continue;
                    }
                    if (paragraph.charAt(i) == '\'') {
                        if (i + 1 < paragraph.length() && paragraph.charAt(i + 1) == '\'') {
                            if (quote) {
                                sb.append(quoteOpen);
                                quote = false;
                            } else {
                                sb.append(quoteClose);
                                quote = true;
                            }
                            i++;
                            continue;
                        }
                    }
                    sb.append(paragraph.charAt(i));
                }
                if (isHead) {
                    int headCount = countHead(paragraph);
                    String head = "</h" + headCount + ">";
                    sb.append(head);
                } else {
                    sb.append(paragraphClose);
                }
                String result = sb.toString();
                sb = new StringBuilder();
                paragraphs.add(result);
            }
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileOutputName), StandardCharsets.UTF_8))) {
                for (String pr : paragraphs) {
                    writer.write(pr + "\n");
                }
            } catch (IOException ioe) {
                System.err.println(ioe.getMessage());
            }
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
    }
	
    public static int count(String s, char pattern) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == pattern) {
                count++;
            }
        }
        return count;
    }

    public static int countHead(String line) {
        int count = 0;
        int i = 0;
        while (line.charAt(i++) == '#') {
            count++;
        }
        return count;
    }

    public static boolean isHead(String line) {
        int i = 0;
        if (line.charAt(i) != '#') {
            return false;
        }
        while (i < line.length() && line.charAt(i) == '#') {
            i++;
        }
        if (i == line.length() || i == line.length() - 1) {
            return false;
        } else return Character.isWhitespace(line.charAt(i)) && (!Character.isWhitespace(line.charAt(i + 1)));
    }

}
