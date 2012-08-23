import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class Checkpoint {
    
    private static Map<Long, Integer> minDistances = new HashMap<Long, Integer>();
    private static boolean[] seen = new boolean[10000];
    
    public static void main(String[] args) {
        long startPrecalc = System.currentTimeMillis();
        precalculatePascalAdd(100);
        for (int i = 0; i < seen.length; i++) {
            if (!seen[i]) {
                System.err.println("First unseen: " + i);
                break;
            }
        }
        System.out.println("Precalc Took " + (System.currentTimeMillis() - startPrecalc) + "ms");
        Scanner scanner = new Scanner(System.in);
        int cases = scanner.nextInt(); scanner.nextLine(); // skip to the end
        StringBuilder out = new StringBuilder();
        long start = System.currentTimeMillis();
        for (int i = 0; i < cases; i++) {
            int s = scanner.nextInt();
            long result = getMinPointFor(s);
            out.append("Case #" + (i+1) + ": " + result + "\n");
        }
        System.out.println("Took " + (System.currentTimeMillis() - start) + "ms");
        System.out.println(out.toString());
    }
    
    private static void precalculatePascalAdd(int numLines) {
        List<Long> prevLine = new ArrayList<Long>();
        List<Long> currentLine = new ArrayList<Long>();
        currentLine.add(1L); //currentLine.add(2L); currentLine.add(1L);
        minDistances.put(1L, 1); // I disagree with this, but it seems this is how Facebook interprets the problem.
        seen[1] = true; seen[0] = true;
        long current = 0;
        for (int n = 0; n < numLines; n++) {
            prevLine.clear(); prevLine.addAll(currentLine);
            System.out.println("n = " + n + "; " + prevLine);
            currentLine.clear();
            currentLine.add(1L);
            for (int k = 0; k < prevLine.size()-1; k++) {
                current = prevLine.get(k) + prevLine.get(k+1);
//                System.out.println("k=" + k + "; current -> " + current);
                currentLine.add(current);
                if (current > 0 && current < 10000) {
//                    System.err.println("Marking " + current + " as seen");
                    seen[(int)current] = true;
                }
//                System.out.print(nCk + " (" + n + "), ");
//                nCk = nCk * (n-k) / (k+1);
                if (!minDistances.containsKey(current)) {
                    minDistances.put(current, n+1);
                }
//                if (isPrime(current)) {
//                    System.err.println("Prime! " + current + " on line " + n);
//                }
            }
//            if (n >= 2) {
//                if ((n % 2) == 0) {
//                    System.out.println("Re-adding " + current);
//                    currentLine.add(current);
//                } else {
//                    currentLine.add(prevLine.get(prevLine.size()-1) * 2);
//                }
//            }
            currentLine.add(1L);
//            System.out.println();
        }
    }
    
    private static boolean isPrime(long current) {
        double sqrt = Math.sqrt(current);
        for (long i = 2; i <= sqrt; i++) {
            if ((current % i) == 0)
                return false;
        }
        return true;
    }

    private static void precalculatePascalMult(int numLines) {
        for (int n = 0; n < numLines; n++) {
            long nCk = 1L;
            for (int k = 0; k <= n; k++) {
//                System.out.print(nCk + " ");
                nCk = nCk * (n-k) / (k+1);
                if (!minDistances.containsKey(nCk)) {
                    minDistances.put(nCk, n);
                }
            }
//            System.out.println();
        }
    }

    private static long getMinPointFor(int s) {
        if (s == 1)
            return 2; // special case
        // First, get list of factors
        List<Pair> factors = getFactors(s);
        
        System.out.println(minDistances);
        
        // Sort by difference, starting with least first
        Collections.sort(factors, new Comparator<Pair>() {
            @Override
            public int compare(Pair pa, Pair pb) {
                long diff = Math.abs(pa.a - pa.b) - Math.abs(pb.a - pb.b);
                if (diff > 0) return 1;
                if (diff == 0) return 0;
                else return -1;
            }
        });
        System.out.println(factors);
        
        int min = Integer.MAX_VALUE;
        for (Pair pair : factors) {
            min = Math.min(min, getMinDist(pair.a) + getMinDist(pair.b)); 
        }
        return min;
    }

    private static int getMinDist(int a) {
        if (isPrime(a)) {
            // I haven't proved this; just assuming...
            return a;
        } else {
            if (minDistances.containsKey((long)a)) {
                return minDistances.get((long)a);
            }
        }
        System.err.println("Ahhh didn't find it! " + a);
        return 0;
    }

    private static List<Pair> getFactors(int s) {
        List<Pair> factors = new ArrayList<Pair>();
        int root = (int) Math.floor(Math.sqrt(s));
        for (int i = 1; i <= root; i++) {
            if ((s % i) == 0) {
                Pair pair = new Pair(i, s/i);
                if (!factors.contains(pair)) {
                    factors.add(pair);
                }
            }
        }
        return factors;
    }
    
    private static class Pair {
        public int a;
        public int b;
        public Pair(int a, int b) {
            this.a = a;
            this.b = b;
        }
        @Override
        public boolean equals(Object o) {
            if (o instanceof Pair) {
                Pair other = (Pair) o;
                return ((other.a == a && other.b == b) ||
                        (other.b == a && other.a == b));
            }
            return false;
        }
        @Override
        public String toString() {
            return "("+a+","+b+")";
        }
    }

}
