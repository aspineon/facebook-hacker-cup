package hackercup2013;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CardGame {
	
	private static final int MOD = 1000000007;
	private static final BigInteger BIG_MOD = BigInteger.valueOf(MOD);
	
	public static void main(String[] args) {
//		if (true) {
//			System.out.println(binomial(9999, 5999));
//			System.exit(1);
//		}
//		Random rand = new Random();
//		int[] c = new int[10000];
//		for (int i = 0; i < c.length; i++) {
//			c[i] = rand.nextInt(2000000000);
//		}
//		if (true) {
//			for (int i : c) {
//				System.out.print(i + " ");
//			}
//			System.exit(1);
//		}
		
		Scanner scanner = new Scanner(System.in);
        int cases = scanner.nextInt(); scanner.nextLine(); // skip to the end
        StringBuilder out = new StringBuilder();
        long start = System.currentTimeMillis();
        for (int i = 0; i < cases; i++) {
            int n = scanner.nextInt();
            int k = scanner.nextInt();
            System.out.println("n is " + n + ", k is " + k);
            Integer[] cards = new Integer[n];
            for (int j = 0; j < n; j++) {
            	cards[j] = scanner.nextInt() % MOD;
            }
            long result = findMaxSum(n, k, cards);
            out.append("Case #" + (i+1) + ": " + result + "\n");
        }
        System.out.println("Took " + (System.currentTimeMillis() - start) + "ms");
        System.out.println(out.toString());
	}
	
	private static int findMaxSum(int n, int k, Integer[] cards) {
		List<Integer> cardList = Arrays.asList(cards);
		Collections.sort(cardList);
		BigInteger max = BigInteger.ZERO;
		for (int i = 0; i < cardList.size(); i++) {
			// Max not used yet
			int idx = cardList.size() - 1 - i;
			int maxCard = cardList.get(idx);
			BigInteger numHands = dynamicBinomial(n - (i + 1), k - 1);
			max = max.add(numHands.multiply(BigInteger.valueOf(maxCard))).mod(BIG_MOD);
			if (i % 1000 == 0) {
				System.out.println("max card this time is " + maxCard);
			    System.out.println("After iteration " + i + ", max is " + max);
			}
		}
		return max.intValue();
	}
	
	static Map<String, BigInteger> cache = new HashMap<String, BigInteger>();
	
	static BigInteger binomial(int N, int K) {
		if (cache.containsKey(N + "," + K)) {
			return cache.get(N + "," + K);
		}
		if (N < K) {
			return BigInteger.ZERO;
		}
		K = Math.min(K, N-K);
	    BigInteger num = BigInteger.ONE;
	    for (int k = 0; k < K; k++) {
	    	num = num.multiply(BigInteger.valueOf(N-k)).divide(BigInteger.valueOf(k+1));
	    }
	    cache.put(N + "," + K, num.mod(BIG_MOD));
	    return num.mod(BIG_MOD);
	}
	
	static BigInteger[][] test;
	
	static BigInteger dynamicBinomial(int N, int K) {
		if (test == null) {
			int m = 10000;
			test = new BigInteger[m/2+1][m];
			test[0][0] = BigInteger.ONE;
			for (int j = 1; j < m/2+1; j++) {
				test[0][j] = BigInteger.ZERO;
			}
			for (int i = 1; i < test.length; i++) {
				test[i][0] = BigInteger.ONE;
			}
			for (int i = 1; i < m; i++) {
				for (int j = 1; j < m/2+1; j++) {
					test[i][j] = test[i-1][j-1].add(test[i-1][j]);
				}
			}
		}
		K = Math.min(K, N-K);
		return test[N][K];
	}
}
