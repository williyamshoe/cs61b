package hw4.puzzle;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class EightPuzzleSolver {
    /***********************************************************************
     * Test routine for your Solver class. Uncomment and run to test
     * your basic functionality.
    **********************************************************************/
    public static void main(String[] args) {

        int[][] tiles = new int[][] {{1, 2}, {0, 3}};
        Board initial = new Board(tiles);

        Solver solver = new Solver(initial);
        StdOut.println("Minimum number of moves = " + solver.moves());
        for (WorldState ws : solver.solution()) {
            StdOut.println(ws);
        }
        /*
        StdOut.println(initial);
        for (WorldState n : initial.neighbors()) {
            StdOut.println(n);
        }
        */
    }
}
