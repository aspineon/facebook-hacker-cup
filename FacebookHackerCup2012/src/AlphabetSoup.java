import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class AlphabetSoup {
    
    private static final String KEY = "HACKERCUP";
    private static final int MAX_LETTERS = 1000;
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int cases = scanner.nextInt(); scanner.nextLine(); // skip to the end
        for (int i = 0; i < cases; i++) {
            String sentence = scanner.nextLine();
            System.out.println("Case #" + (i+1) + ": " + countInstancesOf(sentence, KEY));
        }
    }
    
    private static int countInstancesOf(String sentence, String word) {
        // Process word first: figure out how many of each char it requires
        // array of size distinct_letters
        // in each slot, i count how many letters i see in the sentence
        // i divide each result by the number of needed letters and take the floor of that
        // i take the min number i see -> max number of times we can write the word.
        Map<Character, Integer> requirements = new HashMap<Character, Integer>();
        Map<Character, Integer> counts = new HashMap<Character, Integer>();
        
        fillMapWithCounts(requirements, word);
        fillMapWithCounts(counts, sentence);
        
        int minCount = MAX_LETTERS;
        for (Character c : requirements.keySet()) {
            if (counts.containsKey(c)) {
                int numPossibleWords = counts.get(c) / requirements.get(c);
                if (numPossibleWords < minCount) {
                    minCount = numPossibleWords;
                }
            } else {
                return 0;
            }
        }
        return minCount;
    }
    
    private static void fillMapWithCounts(Map<Character, Integer> map, String sentence) {
        for (int i = 0; i < sentence.length(); i++) {
            if (sentence.charAt(i) != ' ') {
                if (map.containsKey(sentence.charAt(i))) {
                    map.put(sentence.charAt(i), map.get(sentence.charAt(i)) + 1);
                } else {
                    map.put(sentence.charAt(i), 1);
                }
            }
        }
    }

}
