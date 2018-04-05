package lab11.graphs;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int target;
    private int source;
    private Maze maze;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        source = maze.xyTo1D(sourceX, sourceY);
        target = maze.xyTo1D(targetX, targetY);
        distTo[source] = 0;
        edgeTo[source] = source;
    }

    private class Node{
        int loc;
        int prev;
        int dist;
        Node(int l, int p, int d) {
            loc = l;
            prev = p;
            dist = d;
        }
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        Node current = new Node(-1, -1, -1);
        ArrayDeque<Node> fringe = new ArrayDeque<>();
        fringe.addLast(new Node(source, source, 0));
        marked[source] = true;
        while (current.loc != target) {
            current = fringe.removeFirst();
            edgeTo[current.loc] = current.prev;
            marked[current.loc] = true;
            distTo[current.loc] = current.dist;
            for (int i : maze.adj(current.loc)){
                if (!marked[i]) {
                    fringe.addLast(new Node(i, current.loc, current.dist + 1));
                }
            }
            announce();
        }
    }


    @Override
    public void solve() {
        bfs();
    }
}

