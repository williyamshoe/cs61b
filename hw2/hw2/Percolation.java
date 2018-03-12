package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private WeightedQuickUnionUF world;
    private WeightedQuickUnionUF world1;
    private int len;
    private int openSites;
    private int water;
    private int core;
    private boolean[] openWorld;
    private boolean perk;

    /* create N-by-N grid, with all sites initially blocked */
    public Percolation(int N) {
        if (N <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        len = N;
        world = new WeightedQuickUnionUF(N * N + 2);
        world1 = new WeightedQuickUnionUF(N * N + 1);
        water = N * N;
        core = water + 1;
        openWorld = new boolean[N * N];
        openSites = 0;
    }

    /* open the site (row, col) if it is not open already */
    public void open(int row, int col) {
        if (!isOpen(row, col)) {
            int converted = converter(row, col);
            openWorld[converted] = true;
            for (int neighbor : findNeighbors(row, col)) {
                if (neighbor != -1 && openWorld[neighbor]) {
                    world.union(converted, neighbor);
                    world1.union(converted, neighbor);
                }
            }
            if (row == 0) {
                world.union(converted, water);
                world1.union(converted, water);
            }
            if (row == len - 1) {
                world.union(converted, core);
            }
            openSites += 1;
        }
    }

    /* is the site (row, col) open? */
    public boolean isOpen(int row, int col) {
        checker(row, col);
        return openWorld[converter(row, col)];
    }

    /* is the site (row, col) full? */
    public boolean isFull(int row, int col) {
        return isOpen(row, col) && world1.find(converter(row, col)) == world1.find(water);
    }

    /* number of open sites */
    public int numberOfOpenSites() {
        return openSites;
    }

    /* does the system percolate? */
    public boolean percolates() {
        if (!perk) {
            perk = (world.find(water) == world.find(core));
        }
        return perk;
    }

    private int converter(int row, int col) {
        return row * len + col;
    }

    private int[] findNeighbors(int row, int col) {
        int[] neighbors = new int[4];
        int loc = 0;
        for (int i = -1; i < 2; i += 2) {
            if (row + i >= 0 && row + i < len) {
                neighbors[loc] = converter(row + i, col);
            } else {
                neighbors[loc] = -1;
            }

            loc += 1;

            if (col + i >= 0 && col + i < len) {
                neighbors[loc] = converter(row, col + i);
            } else {
                neighbors[loc] = -1;
            }

            loc += 1;
        }
        return neighbors;
    }

    private void checker(int row, int col) {
        if (row < 0 || row >= len || col < 0 || col >= len) {
            throw new java.lang.IndexOutOfBoundsException();
        }
    }

    /* use for unit testing (not required) */
    public static void main(String[] args) {

    }
}
