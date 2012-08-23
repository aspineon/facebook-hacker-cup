package com.facebook.hackercup.squares;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

/**
 * 
 * This class solves the Peg Game puzzle for the qualification round
 * of the Facebook Hacker Cup 2011. Puzzle text copied below.
 * 
 * Double Squares
 * A double-square number is an integer X which can be expressed as
 * the sum of two perfect squares. For example, 10 is a double-square
 * because 10 = 32 + 12. Your task in this problem is, given X,
 * determine the number of ways in which it can be written as the sum
 * of two squares. For example, 10 can only be written as 32 + 12
 * (we don't count 12 + 32 as being different). On the other hand, 25 
 * can be written as 52 + 02 or as 42 + 32.
 * 
 * Input
 * You should first read an integer N, the number of test cases. The
 * next N lines will contain N values of X.
 * 
 * Constraints
 * 0 <= X <= 2147483647
 * 1 <= N <= 100
 * 
 * Output
 * For each value of X, you should output the number of ways to write
 * X as the sum of two squares.
 * 
 * @author Rodrigo Ipince
 */
public class DoubleSquares {
	
	/**
	 * Requires input to be formatted as in the description.
	 * 
	 * @throws IOException if there's an error reading the input
	 * file, including if the input is not formatted appropriately.
	 */
	public static void main(String[] args) throws IOException {
//		generateTestInput(100); if (true) return;
		
		Scanner input = new Scanner(new File(args[0]));
		int numTests = 0;
		if (input.hasNextInt())
			numTests = input.nextInt();
		
		for (int i = 0; i < numTests; i++) {
			System.out.println(getNumberOfSquarePairs(input.nextInt()));
		}
	}

	private static int getNumberOfSquarePairs(int total) {
		double sqrt = Math.round(Math.sqrt(total / 2.0));
		int numPairs = 0;
		for (int i = 0; i <= sqrt; i++) {
			int sq = i * i;
			int rest = total - sq;
			if (rest < sq) // to ensure no double-counting
				continue;
			int restRoot = (int) Math.sqrt(rest);
			if (restRoot * restRoot == rest)
				numPairs++;
		}
		return numPairs;
	}
	
	private static void generateTestInput(int numTests) {
		Random rand = new Random();
		System.out.println(numTests);
		for (int i = 0; i < numTests; i++) {
			System.out.println(rand.nextInt(2147483647));
		}
	}

}
