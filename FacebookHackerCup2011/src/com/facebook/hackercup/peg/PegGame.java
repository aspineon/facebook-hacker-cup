package com.facebook.hackercup.peg;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * This class solves the Peg Game puzzle for the qualification round
 * of the Facebook Hacker Cup 2011. Puzzle text copied below.
 * 
 * Peg Game
 * 
 * At the arcade, you can play a simple game where a ball is dropped
 * into the top of the game, from a position of your choosing. There
 * are a number of pegs that the ball will bounce off of as it drops
 * through the game. Whenever the ball hits a peg, it will bounce to
 * the left with probability 0.5 and to the right with probability 0.5.
 * The one exception to this is when it hits a peg on the far left or
 * right side, in which case it always bounces towards the middle.
 * 
 * When the game was first made, the pegs where arranged in a regular
 * grid. However, it's an old game, and now some of the pegs are
 * missing. Your goal in the game is to get the ball to fall out of
 * the bottom of the game in a specific location. Your task is, given
 * the arrangement of the game, to determine the optimal place to
 * drop the ball, such that the probability of getting it to this
 * specific location is maximized.
 * 
 * The image below shows an example of a game with five rows of five
 * columns. Notice that the top row has five pegs, the next row has
 * four pegs, the next five, and so on. With five columns, there are
 * four choices to drop the ball into (indexed from 0). Note that in
 * this example, there are three pegs missing. The top row is row 0,
 * and the leftmost peg is column 0, so the coordinates of the missing
 * pegs are (1,1), (2,1) and (3,2). In this example, the best place to
 * drop the ball is on the far left, in column 0, which gives a 50%
 * chance that it will end in the goal.
 * 
 * x.x.x.x.x
 *  x...x.x
 * x...x.x.x
 *  x.x...x
 * x.x.x.x.x
 *  G
 *  
 * 'x' indicates a peg, '.' indicates empty space.
 * 
 * Input
 * You should first read an integer N, the number of test cases. Each
 * of the next N lines will then contain a single test case. Each test
 * case will start with integers R and C, the number of rows and
 * columns (R will be odd). Next, an integer K will specify the target
 * column. Finally, an integer M will be followed by M pairs of
 * integer ri and ci, giving the locations of the missing pegs.
 * 
 * Constraints
 * 1 <= N <= 100
 * 3 <= R,C <= 100
 * The top and bottom rows will not have any missing pegs.
 * Other parameters will all be valid, given R and C
 * 
 * Output
 * For each test case, you should output an integer, the location to
 * drop the ball into, followed by the probability that the ball will
 * end in columns K, formatted with exactly six digits after the decimal
 * point (round the last digit, don't truncate).
 * 
 * Notes
 * The input will be designed such that minor rounding errors will not
 * impact the output (i.e. there will be no ties or near -- up to 1E-9
 * -- ties, and the direction of rounding for the output will not be
 * impacted by small errors).
 * 
 * @author Rodrigo Ipince
 */
public class PegGame {
	
	/**
	 * Represents the board. We use a rectangular representation of the
	 * board where non-empty spaces are considered to be "pegs", whether
	 * they're actual pegs, or part of the sides.
	 * 
	 * Thus, the example board in the problem description would be 
	 * represented as follows:
	 * 
	 * Board:
	 * x.x.x.x.x
	 *  x...x.x
	 * x...x.x.x
 	 *  x.x...x
	 * x.x.x.x.x
	 * 
	 * Representation:
	 * x x x x x
	 * xx   x xx
	 * x   x x x
	 * xx x   xx
	 * x x x x x
	 */
	private final boolean[][] pegs;
	private final int spaceColumns;
	private final int width;
	
	private final double[][][] probabilities;
	private final boolean[][] done;
//	                         <Integer[], Double[]> probabilities = new HashMap<Integer[], Double[]>();
	
	/**
	 * Requires input to be formatted as in the description.
	 * 
	 * @throws IOException if there's an error reading the input
	 * file, including if the input is not formatted appropriately.
	 */
	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();

		// Load peg locations
		System.out.println("Loading games...");
		
		Scanner pegInput = new Scanner(new File(args[0]));
		
		// Get number of test cases
		int numTests = 0;
		if (pegInput.hasNext())
			numTests = pegInput.nextInt();
		
		StringBuilder out = new StringBuilder(); // output
		for (int i = 0; i < numTests; i++) {
			System.out.println("Game number " + i);
			
			int rows = pegInput.nextInt();
			assert (rows % 2) == 1; // rows must be odd
			int columns = pegInput.nextInt();
			int target = pegInput.nextInt();
			int numMissingPegs = pegInput.nextInt();
			int[][] coords = new int[numMissingPegs][2];
			for (int j = 0; j < numMissingPegs; j++) {
				int x = pegInput.nextInt(); // row coord
				int y = pegInput.nextInt(); // col coord
				coords[j] = new int[] {x, y};
			}
			
			System.out.println("Game number " + i);
			System.out.println("Peg coords: ");
			for (int[] c : coords) {
				System.out.print(Arrays.toString(c) + ", ");
			}
			System.out.println();
			PegGame game = new PegGame(rows, columns, coords);
			
			System.out.println(game);
			double result[] = game.getBestColumn(target);
			
			// Print output in desired format (nasty formatting)
			out.append((int) result[0]).append(" ");
			double prob = result[1];
			if (prob == 1)
				out.append("1.000000");
			else {
				out.append("0.").append((int) Math.round(prob * 1000000));
			}
			out.append("\n");
			System.out.println("Best col for target " + target + " is column " + result[0] + " with prob " + result[1]);
		}
		
		System.out.println(out.toString());
		System.out.println("Took " + (System.currentTimeMillis() - start) + "ms");
	}

	public PegGame(int rows, int columns, int[][] missingPegs) {
		spaceColumns = columns;
		width = 2 * columns + 1;
		pegs = new boolean[rows][width];
		
		// First, color the entire board with pegs
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < width; j++) {
				if (j == 0 || j == width - 1) // first or last col => peg
					pegs[i][j] = true;
				if (i % 2 == j % 2) // same parity => peg
					pegs[i][j] = true;
			}
		}
		
		// Then, remove missing pegs
		for (int k = 0; k < missingPegs.length; k++) {
			pegs[missingPegs[k][0]][getRepColumnFromGameColumn(missingPegs[k][0], missingPegs[k][1])] = false;
		}
		
		probabilities = new double[rows][width][spaceColumns];
		done = new boolean[rows][width];
	}
	
	private double[] getProbabilities(int x, int y) {
		// If memoized, return right away
//		Integer[] key = new Integer[]{x ,y};
//		if (probabilities.containsKey(key))
//			return probabilities.get(key);
		if (done[x][y])
			return probabilities[x][y];
		
		double[] probs = new double[spaceColumns];
//		for (int i = 0; i < probs.length; i++)
//			probs[i] = 0.0;
		
		if (pegs[x][y] || y == 0 || y == width - 1) {
//			probabilities.put(key, probs);
			probabilities[x][y] = probs;
			done[x][y] = true;
			return probs; // peg on this board => 0 prob
		}
		
		if (x == 0) { // Starting row
			probs[getGameColumnFromRepColumn(x, y)] = 1.0; // rest are 0
		} else {
			// Initialize probs
			double factor;

			// Get left probs iff there's a peg to the left (note: no
			// boundary conditions since all sides are covered in pegs)
			if (pegs[x][y - 1]) {
				// Left peg -> factor is 1 instead of 0.5
				factor = (y - 1 == 1 && x % 2 == 1) ? 1 : 0.5;
				addProbabilities(probs, getProbabilities(x - 1, y - 1), factor);
			}
			
			// Get above probs iff there's no peg above (note: no
			// boundary conditions since we already established
			// that x != 0)
			if (!pegs[x - 1][y])
				addProbabilities(probs, getProbabilities(x - 1, y), 1.0);
			
			// Get right probs iff there's a peg to the right
			if (pegs[x][y + 1]) {
				// Right peg -> factor is 1 instead of 0.5
				factor = (y + 1 == width - 2 && x % 2 == 1) ? 1 : 0.5;
				addProbabilities(probs, getProbabilities(x - 1, y + 1), factor);
			}
		}
//		probabilities.put(key, probs);
		probabilities[x][y] = probs;
		done[x][y] = true;
		return probs;
	}
	
	private double[] getBestColumn(int target) {
		int x = pegs.length - 1;
		int y = getRepColumnFromGameColumn(x, target) + 1;
		
		double[] probs = getProbabilities(x, y);
		
		double maxProb = 0;
		int maxCol = -1;
		for (int i = 0; i < probs.length; i++) {
			if (probs[i] > maxProb) {
				maxProb = probs[i];
				maxCol = i;
			}
		}
		
		return new double[]{maxCol, maxProb};
	}
	
	private int getRepColumnFromGameColumn(int x, int y) {
		return 2 * y + (x % 2);
	}
	
	// only works if y is guaranteed to be a representation
	// of a real game column
	private int getGameColumnFromRepColumn(int x, int y) {
		return (y - (x % 2)) / 2;
	}
	
	private double[] addProbabilities(double[] first, double[] second, double factor) {
		for (int i = 0; i < first.length; i++) {
			first[i] += second[i] * factor;
		}
		return first;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < pegs.length; i++) {
			for (int j = 0; j < pegs[0].length; j++) {
				sb.append(pegs[i][j] ? "x" : " ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

}