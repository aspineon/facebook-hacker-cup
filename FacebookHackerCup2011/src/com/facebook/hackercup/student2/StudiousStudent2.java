package com.facebook.hackercup.student2;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class StudiousStudent2 {
	
	/**
	 * Requires input to be formatted as in the description.
	 * 
	 * @throws IOException if there's an error reading the input
	 * file, including if the input is not formatted appropriately.
	 */
	public static void main(String[] args) throws IOException {
		
		// Pre-compute factorials just in case
//		buildFactorialTable(MAX); // max input
//		
		Scanner input = new Scanner(new File(args[0]));
		int numTests = 0; // should be 20
		if (input.hasNextInt())
			numTests = input.nextInt();
		
		long start = System.currentTimeMillis();
		for (int i = 0; i < numTests; i++) {
			String str = input.next();
			System.out.println(countSequences(str));
		}
//		System.out.println(countSequences("ab"));// System.out.println(countSequences(new Substring("ab", 0, 0, 'a')));
//		System.out.println(countSequences("aba"));
//		System.out.println(countSequences("aabb"));
//		System.out.println(countSequences("ababa"));
//		System.out.println(countSequences("bbbbb"));
//		System.out.println(countSequences("abbaaababaabbaaababa"));
//		System.out.println(countSequences("abbaaababaabbaaababaabbbbbaaba"));
//		System.out.println(countSequences("abbaabaabababbabbababaabababababbbbbbbaaaaaaaaababababbbaaba"));
		System.out.println("Took " + (System.currentTimeMillis() - start) + "ms");
	}
	
	private static final BigInteger MOD = BigInteger.valueOf(1000000007);
	private static final char[] CHARS = new char[] {
		'a', 'b'
	};
	
	private static Map<String, BigInteger> numSequences = new HashMap<String, BigInteger>();
	
	public static BigInteger countSequences(String str) {
		String substring, start, end;
		BigInteger total = BigInteger.ZERO;
		if (str.length() == 1) {
			return BigInteger.ONE;
		} else if (numSequences.containsKey(str)) {
//			System.out.println("Cache hit");
			return numSequences.get(str);
		} else {
			for (int i = 0; i < str.length(); i++) {
				for (int j = i+1; j < str.length(); j++) {
					substring = str.substring(i, j+1); // get substring
					start = str.substring(0, i);
					end = str.substring(j+1);
//					if (str.substring(i, j+1).indexOf('a') >= 0)
//						total = total.add(countSequences(str.substring(0, i) + 'a' + str.substring(j+1)));
//					if (str.substring(i, j+1).indexOf('b') >= 0)
//						total = total.add(countSequences(str.substring(0, i) + 'b' + str.substring(j+1)));
					for (char c : CHARS) {
						total = total.add(substring.indexOf(c) >= 0 ? countSequences(start + c + end) : BigInteger.ZERO);
					}
				}
			}
			
			// use recursion
//			for (int k = 2; k <= str.length(); k++) {
//				// k == length of substring
//				for (int l = 0; l < str.length() - k + 1; l++) {
//					substring = str.substring(l, l + k); // get substring
//					for (char c : CHARS) {
//						total += substring.indexOf(c) >= 0 ? countSequences(str.substring(0, l) + c + str.substring(l+k)) : 0;
//					}
//				}
//			}
		}
		numSequences.put(str, (total.add(BigInteger.ONE).mod(MOD)));
		return total.add(BigInteger.ONE).mod(MOD);
	}
	
//	public static BigInteger countSequences(Substring str) {
//		String substring;
//		BigInteger total = BigInteger.ZERO;
//		if (str.length() == 1) {
//			return BigInteger.ONE;
//		} else if (numSequences.containsKey(str)) {
////			System.out.println("Cache hit");
//			return numSequences.get(str);
//		} else {
//			for (int i = 0; i < str.length(); i++) {
//				for (int j = i+1; j < str.length(); j++) {
////					substring = str.substring(i, j+1); // get substring
//					str.setI(i);
//					str.j = j;
//					if (str.substring(i, j+1).indexOf('a') >= 0) {
//						str.c = 'a';
//						str.init = true;
//						total = total.add(countSequences(str));
//					}
//					if (str.substring(i, j+1).indexOf('b') >= 0) {
//						str.c = 'b';
//						str.init = true;
//						total = total.add(countSequences(str));
//					}
////					for (char c : CHARS) {
////						total = total.add(substring.indexOf(c) >= 0 ? countSequences(str.substring(0, i) + c + str.substring(j+1)) : BigInteger.ZERO);
////					}
//				}
//			}
//		}
//		numSequences.put(str, (total.add(BigInteger.ONE).mod(MOD)));
//		return total.add(BigInteger.ONE).mod(MOD);
//	}
	
//	private static class Substring {
//		String str;
//		int i;
//		int j;
//		char c;
//		boolean init = false;
//		
//		public Substring(String str, int i, int j, char c, boolean init) {
//			this.str = str;
//			this.i = i;
//			this.j = j;
//			this.c = c;
//			this.init = init;
//		}
//		
//		public void setI(int i2) {
//			if (i2 < i)
//				this.i = i2;
//			else
//				this.i = 
//			// TODO Auto-generated method stub
//			
//		}
//
//		public int length() {
//			return init ? str.length() - (j - i + 1) : str.length();
//		}
		
//		public String substring(int start, int end) {
//			if (start < i)
//			return "";
//		}
//	}

}
