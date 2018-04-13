package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayDeque;
import java.util.Deque;

public class Solver {

    private MinPQ<SearchNode> pq;
    private Deque<WorldState> path;

    private class SearchNode implements Comparable<SearchNode> {
        WorldState current;
        int moves;
        SearchNode prev;

        SearchNode(WorldState c, int m, SearchNode p) {
            current = c;
            moves = m;
            prev = p;
        }

        @Override
        public int compareTo(SearchNode o) {
            if (current.estimatedDistanceToGoal() + moves
                    > o.current.estimatedDistanceToGoal() + o.moves) {
                return 1;
            } else if (current.estimatedDistanceToGoal() + moves
                    < o.current.estimatedDistanceToGoal() + o.moves) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    /* Constructor which solves the puzzle, computing
    everything necessary for moves() and solution() to
    not have to solve the problem again. Solves the
    puzzle using the A* algorithm. Assumes a solution exists. */
    public Solver(WorldState initial) {
        pq = new MinPQ<>();
        pq.insert(new SearchNode(initial, 0, null));

        path = new ArrayDeque<>();

        while (pq.min().current.estimatedDistanceToGoal() != 0) {
            SearchNode mini = pq.delMin();
            for (WorldState neighbor : mini.current.neighbors()) {
                if (mini.prev == null || !neighbor.equals(mini.prev.current)) {
                    pq.insert(new SearchNode(neighbor, mini.moves + 1, mini));
                }
            }
        }
        SearchNode cur = pq.min();
        while (cur != null) {
            path.addFirst(cur.current);
            cur = cur.prev;
        }
    }

    /* Returns the minimum number of moves to solve the puzzle starting
    at the initial WorldState. */
    public int moves() {
        return path.size() - 1;
    }

    /* Returns a sequence of WorldStates from the initial WorldState
    to the solution. */
    public Iterable<WorldState> solution() {
        return path;
    }
}
