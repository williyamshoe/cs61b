package lab11.graphs;

import java.util.ArrayDeque;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int source;
    private Maze maze;
    private boolean cycleFound = false;

    public MazeCycles(Maze m) {
        super(m);
        maze = m;
        source = maze.xyTo1D(1, 1);
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


    private void cyclefinder() {
        Node current;
        ArrayDeque<Node> fringe = new ArrayDeque<>();
        fringe.addLast(new Node(source, source, 0));
        marked[source] = true;
        while (!cycleFound) {
            current = fringe.removeFirst();
            edgeTo[current.loc] = current.prev;
            marked[current.loc] = true;
            distTo[current.loc] = current.dist;
            for (int i : maze.adj(current.loc)){
                if (marked[i] && current.prev != i) {
                    edgeTo[i] = current.loc;
                    cycleFound = true;
                }else if (current.prev != i) {
                    fringe.addLast(new Node(i, current.loc, current.dist + 1));
                }
            }
            announce();
            if (fringe.isEmpty()){
                return;
            }
        }
    }


    @Override
    public void solve() {
        cyclefinder();
    }
}

