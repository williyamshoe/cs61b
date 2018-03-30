package hw4.puzzle;

import edu.princeton.cs.algs4.StdOut;

public class EightPuzzleSolver {
    /***********************************************************************
     * Test routine for your Solver class. Uncomment and run to test
     * your basic functionality.
    **********************************************************************/
    public static void main(String[] args) {

        int[][] tiles = new int[][] {{8, 3, 6}, {1, 7, 2}, {0, 4, 5}};
        int[][] tiles1 = new int[][] {{8, 3, 6, 1}, {1, 7, 2, 1}, {0, 4, 5, 1}, {1, 1, 1, 1}};
        Board initial = new Board(tiles);
        Board initial1 = new Board(tiles1);
        System.out.println(initial.equals(initial1));

        Solver solver = new Solver(initial);
        StdOut.println("Minimum number of moves = " + solver.moves());
        for (WorldState ws : solver.solution()) {
            StdOut.println(ws);
        }

        StdOut.println(initial);
        for (WorldState n : initial.neighbors()) {
            StdOut.println(n);
        }
    }
}
