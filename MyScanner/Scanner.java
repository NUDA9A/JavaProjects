import java.io.*;
import java.util.NoSuchElementException;
import java.util.Arrays;
public final class Scanner {
    private int currIndx = 0;
    private char[] chr;
    private int currBufferSize;
    private int currSepIndx = 0;
    private final Reader reader;
    private static final int BUFFER_SIZE = 300;
    private final int sepLen = System.lineSeparator().toCharArray().length;
    private final char[] sep = System.lineSeparator().toCharArray();

    private Scanner(InputStream source, int size) {
        reader = new BufferedReader(new InputStreamReader(source));
        chr = new char[size];
        readInput();
    }

    public Scanner(InputStreamReader source) {
        reader = new BufferedReader(source);
        chr = new char[BUFFER_SIZE];
        readInput();
    }

    public Scanner(InputStream in) {
        this(in, BUFFER_SIZE);
    }

    public void close() {
        try {
            reader.close();
        } catch (IOException ioe) {
        }
    }
    private void readInput() {
        try {
            currBufferSize = reader.read(chr);
        } catch (IOException ioe) {
            currBufferSize = -1;
        }
    }

    public boolean hasNextLine() {
        if (currIndx >= currBufferSize) {
            readInput();
            currIndx = 0;
            return currBufferSize != -1;
        }
        return true;
    }

    public boolean hasNext() {
        if (this.currIndx >= this.currBufferSize) {
            readInput();
            this.currIndx = 0;
            if (this.currBufferSize == -1) {
                return false;
            }
        }
        return true;
    }

    public String next() {
        StringBuilder wordBuilder = new StringBuilder();
        for (int i = currIndx; i < currBufferSize; i++, currIndx++) {
            if (Character.getType(chr[i]) == Character.DASH_PUNCTUATION ||
                    Character.isLetter(chr[i]) || chr[i] == '\'') {
                wordBuilder.append(chr[i]);
            } else if (!wordBuilder.isEmpty()) {
                String nextWord = wordBuilder.toString().toLowerCase();
                currIndx++;
                i++;
                return nextWord;
            }
            if (i + 1 == currBufferSize) {
                readInput();
                currIndx = -1;
                i = -1;
                continue;
            }
        }
        throw new NoSuchElementException();
    }
	
	public String[] wordsInLine() {
		String[] words = new String[0];
		int k = 0;
        int startAbcIntIndx = -1;
        boolean fullLine = false;
		StringBuilder wordBuilder = new StringBuilder();
		while (!fullLine) {
			if (chr[currIndx] == sep[currSepIndx]) {
                currIndx++;
                currSepIndx++;
                if (currSepIndx == sepLen) {
					if (!wordBuilder.isEmpty()) {
						if (k == words.length) {
							words = Arrays.copyOf(words, (words.length + 1) * 2);
						}
						words[k++] = wordBuilder.toString().toLowerCase();
					}
                    fullLine = true;
                    currSepIndx = 0;
                }
                if (currIndx == currBufferSize) {
                    readInput();
                    currIndx = 0;
                }
                continue;
            } else {
                currSepIndx = 0;
            }
			if (Character.getType(chr[currIndx]) == Character.DASH_PUNCTUATION || 
			Character.isLetter(chr[currIndx]) || chr[currIndx] == '\'') {
				wordBuilder.append(chr[currIndx]);
			} else if(!wordBuilder.isEmpty()) {
				if (k == words.length) {
					words = Arrays.copyOf(words, (words.length + 1) * 2);
				}
				words[k++] = wordBuilder.toString().toLowerCase();
				wordBuilder = new StringBuilder();
			}
			currIndx++;
			if (currIndx == currBufferSize) {
				readInput();
				currIndx = 0;
			}
		}
		words = Arrays.copyOf(words, k);
		return words;
	}
	
    public int[] intsInLine() {
        int[] ints = new int[0];
        int k = 0;
        int startIntIndx = -1;
        boolean fullLine = false;
        while (!fullLine) {
            if (chr[currIndx] == sep[currSepIndx]) {
                currIndx++;
                currSepIndx++;
                if (currSepIndx == sepLen) {
                    fullLine = true;
                    currSepIndx = 0;
                }
                if (currIndx == currBufferSize) {
                    readInput();
                    currIndx = 0;
                }
                continue;
            } else {
                currSepIndx = 0;
            }
            if (Character.isDigit(chr[currIndx]) || chr[currIndx] == '-') {
                startIntIndx = currIndx;
                String mbInt = "";
                while (!Character.isWhitespace(chr[currIndx])) {
                    currIndx++;
                    if (currIndx == currBufferSize) {
                        mbInt += String.valueOf(chr).substring(startIntIndx, currIndx);
                        readInput();
                        currIndx = 0;
                        startIntIndx = 0;
                    }
                }
                mbInt += String.valueOf(chr).substring(startIntIndx, currIndx);
                if (isInteger(mbInt)) {
                    if (k == ints.length) {
                        ints = Arrays.copyOf(ints, (ints.length + 1) * 2);
                    }
                    ints[k++] = Integer.parseInt(mbInt);
                    startIntIndx = -1;
                }
            } else {
                currIndx++;
                if (currIndx == currBufferSize) {
                    readInput();
                    currIndx = 0;
                }
            }
        }
        ints = Arrays.copyOf(ints, k);
        return ints;
    }

    public String[] abcIntsInLine() {
        String[] abcInts = new String[0];
        int k = 0;
        int startAbcIntIndx = -1;
        boolean fullLine = false;
        while (!fullLine) {
            if (chr[currIndx] == sep[currSepIndx]) {
                currIndx++;
                currSepIndx++;
                if (currSepIndx == sepLen) {
                    fullLine = true;
                    currSepIndx = 0;
                }
                if (currIndx == currBufferSize) {
                    readInput();
                    currIndx = 0;
                }
                continue;
            } else {
                currSepIndx = 0;
            }
            if (((int)chr[currIndx] >= 97 && (int)chr[currIndx] <= 106) || chr[currIndx] == '-') {
                startAbcIntIndx = currIndx;
                String mbInt = "";
                while (!Character.isWhitespace(chr[currIndx])) {
                    if (((int)chr[currIndx] < 97 || (int)chr[currIndx] > 106) && startAbcIntIndx != -1 && currIndx != startAbcIntIndx) {
                        startAbcIntIndx = -1;
                        mbInt = "";
                    }
                    currIndx++;
                    if (currIndx == currBufferSize) {
                        if (startAbcIntIndx != -1) {
                            mbInt += String.valueOf(chr).substring(startAbcIntIndx, currIndx);
                            readInput();
                            currIndx = 0;
                            startAbcIntIndx = 0;
                        } else {
                            readInput();
                            currIndx = 0;
                        }
                    }
                }
                if (startAbcIntIndx == -1) {
                    continue;
                } else {
                    mbInt += String.valueOf(chr).substring(startAbcIntIndx, currIndx);
                    if (k == abcInts.length) {
                        abcInts = Arrays.copyOf(abcInts, (abcInts.length + 1) * 2);
                    }
                    abcInts[k++] = mbInt;
                    startAbcIntIndx = -1;
                }
            } else {
                currIndx++;
                if (currIndx == currBufferSize) {
                    readInput();
                    currIndx = 0;
                }
            }
        }
        abcInts = Arrays.copyOf(abcInts, k);
        return abcInts;
    }

    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}

