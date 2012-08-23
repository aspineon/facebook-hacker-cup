import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class SquishedStatus {

    private static final long MOD = 4207849484L;
    
    private static final Map<String, Long> CACHE = new HashMap<String, Long>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int cases = scanner.nextInt(); scanner.nextLine(); // skip to the end
        StringBuilder out = new StringBuilder();
        long start = System.currentTimeMillis();
        for (int i = 0; i < cases; i++) {
            int m = scanner.nextInt();
            String encoded = scanner.next();
            long result = countPossibleEncodings(encoded, m);
            out.append("Case #" + (i+1) + ": " + result + "\n");
        }
        System.out.println("Took " + (System.currentTimeMillis() - start) + "ms");
        System.out.println(out.toString());
    }

    // 2 <= m <= 255
    private static long countPossibleEncodings(String encoded, int m) {
        int length = 3;
        if (m < 100)
            length = 2;
        if (m < 10)
            length = 1;
        CACHE.clear();
        return countPossibleEncodings(encoded, m, length, "");
    }

    private static long countPossibleEncodings(String encoded, int max, int maxLength, String current) {
        if (current.isEmpty() && CACHE.containsKey(encoded)) {
            return CACHE.get(encoded);
        }
        if (current.isEmpty() && !encoded.isEmpty() && encoded.charAt(0) == '0') { // Invalid input
            return 0;
        }
        // Base case
        if (encoded.isEmpty()) {
            return current.length() > 0 ? 1 : 0;
        } else {
            // Are we forced to make a cut?
            String next = current + encoded.charAt(0);
            int nextInt = Integer.parseInt(next);
            if (nextInt > max) {
                // We can't cut.
                return 0;
            } else {
                // We can cut, but we don't have to.
                String nextStr = encoded.substring(1);
                long encodingsIfCut = countPossibleEncodings(nextStr, max, maxLength, "");
                CACHE.put(nextStr, encodingsIfCut);
                long encodingsIfNotCut = countPossibleEncodings(nextStr, max, maxLength, next);
                return (encodingsIfCut + encodingsIfNotCut) % MOD;
            }
        }
    }
}
