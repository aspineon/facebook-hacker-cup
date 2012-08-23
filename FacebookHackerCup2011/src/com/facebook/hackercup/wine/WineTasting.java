package com.facebook.hackercup.wine;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class WineTasting {
	
	private static final BigInteger MOD = BigInteger.valueOf(1051962371); // less than MAX_INT
	private static final int MAX = 100;
	private static BigInteger[] FACTORIALS;
	
	/**
	 * Requires input to be formatted as in the description.
	 * 
	 * @throws IOException if there's an error reading the input
	 * file, including if the input is not formatted appropriately.
	 */
	public static void main(String[] args) throws IOException {
//		generateTestInput(); if (true) return;
		
		// Pre-compute factorials just in case
		buildFactorialTable(MAX); // max input
		
		Scanner input = new Scanner(new File(args[0]));
		int numTests = 0; // should be 20
		if (input.hasNextInt())
			numTests = input.nextInt();
		
		for (int i = 0; i < numTests; i++) {
			int numGlasses = input.nextInt();
			int numGuesses = input.nextInt();
			BigInteger result = findNumberOfWinOutcomes(numGlasses, numGuesses);
			result = result.mod(MOD);
			System.out.println(result.toString());
		}
	}

	private static void buildFactorialTable(int max) {
		FACTORIALS = new BigInteger[max + 1];
		System.out.println("Generating table");
		for (int i = 0; i <= max; i++) {
			factorial(i);
		}
		List<BigInteger> list = Arrays.asList(FACTORIALS);
		System.out.println(list);
	}

	private static BigInteger factorial(int i) {
		if (i < 0 || i > MAX) {
			return BigInteger.ZERO;
		} else {
			if (FACTORIALS[i] != null) {
				return FACTORIALS[i];
			} else {
				BigInteger bigI = BigInteger.valueOf(i);
				BigInteger result = (i == 0 ?
						BigInteger.ONE :
						factorial(i - 1).multiply(bigI));
				FACTORIALS[i] = result;
				return result;
			}
		}
	}
	
	private static BigInteger findNumberOfWinOutcomes(int glasses, int target) {
		// Let f(n, k) = number of permutations of n items with
		// exactly k fixed positions.
		// We're looking for the sum of f(n,k) for k = target, target+1, ... , glasses
		
		BigInteger result = BigInteger.ZERO;
		for (int i = target; i <= glasses; i++) {
			result = result.add(fixedPointPermutations(glasses, i));
		}
		return result;
	}

	private static BigInteger fixedPointPermutations(int n, int k) {
		// f(n,k) = (n choose k) * f(n, 0)
		if (k == 0) {
			// n! - sum_{i=1}^n((-1)^(i+1)*(n choose i)*(n-i)!)
			BigInteger sum = BigInteger.ZERO;
			BigInteger tmp;
			for (int i = 1; i <= n; i++) {
				tmp = combination(n, i).multiply(factorial(n-i));
				if ((i+1) % 2 == 0) {
					sum = sum.add(tmp);
				} else {
					sum = sum.subtract(tmp);
				}
			}
			return factorial(n).subtract(sum);
		} else {
			return combination(n, k).multiply(fixedPointPermutations(n-k, 0));
		}
	}

	private static BigInteger combination(int n, int k) {
		BigInteger result = factorial(n).divide(factorial(k).multiply(factorial(n-k)));
		return result;
	}
	

	private static void generateTestInput() {
		int testCases = 50;
		System.out.println(testCases);
		Random rand = new Random();
		for (int i = 0; i < testCases; i++) {
			int g = 1 + rand.nextInt(MAX);
			System.out.print(g + " ");
			System.out.println(1 + rand.nextInt(g));
		}
	}

}
