import java.util.Scanner;


public class Billboards {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int cases = scanner.nextInt(); scanner.nextLine(); // skip to the end
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < cases; i++) {
            int width = scanner.nextInt();
            int height = scanner.nextInt();
            String sentence = scanner.nextLine().trim();
            out.append("Case #" + (i+1) + ": " + getMaxFontSize(width, height, sentence) + "\n");
        }
        System.out.println(out.toString());
    }

    private static int getMaxFontSize(int width, int height, String sentence) {
        String[] words = sentence.split(" ");
        int[] wordLengths = new int[words.length];
        int i = 0;
        int maxLength = 0;
        for (String word : words) {
            wordLengths[i++] = word.length();
            maxLength = Math.max(maxLength, word.length());
        }
        if (maxLength == 0) {
            // No words?
            return 0;
        }
        // Get simple upper bound.
        int upperBound = width / maxLength;
        if (upperBound < 1) {
            return 0;
        }
        
        // Brute force: starting with upperBound, check sizes until one fits
        for (int size = upperBound; size >= 1; size--) {
            if (textFits(width, height, wordLengths, size)) {
                return size;
            }
        }
        return 0;
    }

    private static boolean textFits(int width, int height, int[] wordLengths, int size) {
        assert wordLengths.length > 0;
        int maxLength = 0;
        for (int length : wordLengths) {
            maxLength = Math.max(maxLength, length);
        }
        // Check global horizontal constraint
        if (size * maxLength > width) {
            return false;
        }
        // Get number of available lines
        int numLines = height / size;
        int remainingLines = numLines;
        int remainingWords = wordLengths.length;
        // Start placing words in lines in order
        int wordIndex = 0;
        for (int line = 0; line < numLines; line++) {
            remainingLines = numLines - line;
            remainingWords = wordLengths.length - wordIndex;
            if (remainingLines >= remainingWords) {
                return true; // We have more lines than words, and all words fit in a line.
            }
            
            // Fill current line with words
            int lineTotal = wordLengths[wordIndex++] * size; // first word always fits
            while (wordIndex < wordLengths.length) {
                if (lineTotal + size /* space */ + wordLengths[wordIndex] * size <= width) {
                    lineTotal += size + wordLengths[wordIndex++] * size; // add word and keep going
                } else {
                    break; // this line is full
                }
            }
            if (wordIndex == wordLengths.length) {
                // Out of words
                return true;
            }
        }
        // Out of lines.
        return false;
    }
}
