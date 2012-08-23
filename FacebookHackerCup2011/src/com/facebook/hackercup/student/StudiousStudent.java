package com.facebook.hackercup.student;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * This class solves the Peg Game puzzle for the qualification round
 * of the Facebook Hacker Cup 2011. Puzzle text copied below.
 * 
 * Studious Student
 * You've been given a list of words to study and memorize. Being a
 * diligent student of language and the arts, you've decided to not
 * study them at all and instead make up pointless games based on them.
 * One game you've come up with is to see how you can concatenate the
 * words to generate the lexicographically lowest possible string. 
 * 
 * Input
 * As input for playing this game you will receive a text file
 * containing an integer N, the number of word sets you need to play
 * your game against. This will be followed by N word sets, each
 * starting with an integer M, the number of words in the set,
 * followed by M words. All tokens in the input will be separated by
 * some whitespace and, aside from N and M, will consist entirely of
 * lowercase letters.
 * 
 * Output
 * Your submission should contain the lexicographically shortest strings
 * for each corresponding word set, one per line and in order.
 * 
 * Constraints
 * 1 <= N <= 100
 * 1 <= M <= 9
 * 1 <= all word lengths <= 10
 * 
 * @author Rodrigo Ipince
 *
 */
public class StudiousStudent {
	
	/**
	 * Requires input to be formatted as in the description.
	 * 
	 * @throws IOException if there's an error reading the input
	 * file, including if the input is not formatted appropriately.
	 */
	public static void main(String[] args) throws IOException {
		System.out.println("Loading words...");
		
		Scanner input = new Scanner(new File(args[0]));
		
		// Get number of test cases
		int numTests = 0;
		if (input.hasNext())
			numTests = input.nextInt();
		
		for (int i = 0; i < numTests; i++) {
			int numWords = input.nextInt();
			List<String> words = new ArrayList<String>();
			for (int j = 0; j < numWords; j++) { 
				words.add(input.next());
			}
			Collections.sort(words);
			for (String word : words) {
				System.out.print(word);
			}
			System.out.println();
		}
	}

}
