package hw4.puzzle;

import java.util.HashSet;
import java.util.Set;

public class Board implements WorldState {

    private int[][] tiles;
    private int size;

    private int[][] newArray(int[][] x) {
        int[][] y = new int[x.length][x.length];
        for (int i = 0; i < size; i += 1) {
            for (int j = 0; j < size; j += 1) {
                y[i][j] = x[i][j];
            }
        }
        return y;
    }

    public Board(int[][] tiles) {
        size = tiles.length;
        this.tiles = newArray(tiles);
    }

    public int tileAt(int i, int j) {
        if (i < 0 || j < 0 || i >= size || j >= size) {
            throw new IndexOutOfBoundsException();
        }
        return tiles[i][j];
    }

    public int size() {
        return size;
    }

    public Iterable<WorldState> neighbors() {
        Set<WorldState> neighbs = new HashSet<>();
        int x0 = -1;
        int y0 = -1;
        for (int i = 0; i < size; i += 1) {
            for (int j = 0; j < size; j += 1) {
                if (tiles[i][j] == 0) {
                    x0 = i;
                    y0 = j;
                }
            }
        }
        for (int i = -1; i < 2; i += 2) {
            try {
                int replacement = tileAt(x0 + i, y0);
                int[][] newtiles = newArray(tiles);
                newtiles[x0][y0] = replacement;
                newtiles[x0 + i][y0] = 0;
                neighbs.add(new Board(newtiles));
            } catch (IndexOutOfBoundsException e) {
                int wat = 0;
            }

            try {
                int replacement = tileAt(x0, y0 + i);
                int[][] newtiles = newArray(tiles);
                newtiles[x0][y0] = replacement;
                newtiles[x0][y0 + i] = 0;
                neighbs.add(new Board(newtiles));
            } catch (IndexOutOfBoundsException e) {
                int wat = 0;
            }
        }
        return neighbs;
    }

    public int hamming() {
        int total = 0;
        for (int i = 0; i < size; i += 1) {
            for (int j = 0; j < size; j += 1) {
                if ((1 + i * size + j) != tiles[i][j] && tiles[i][j] != 0) {
                    total += 1;
                }
            }
        }
        return total;
    }

    public int manhattan() {
        int total = 0;
        for (int i = 0; i < size; i += 1) {
            for (int j = 0; j < size; j += 1) {
                if (tiles[i][j] != 0) {
                    int x = Math.abs((tiles[i][j] - 1) / size - i);
                    int y = Math.abs((tiles[i][j] - 1) % size - j);
                    total += y + x;
                }
            }
        }
        return total;
    }

    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    @Override
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null || getClass() != y.getClass()) {
            return false;
        }

        Board otherBoard = (Board) y;

        if (otherBoard.size() != size) {
            return false;
        }

        for (int i = 0; i < size; i += 1) {
            for (int j = 0; j < size; j += 1) {
                if (otherBoard.tileAt(i, j) != tileAt(i, j)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int tot = 0;
        for (int i = 0; i < size; i += 1) {
            for (int j = 0; j < size; j += 1) {
                tot *= 10;
                tot += tiles[i][j];
            }
        }
        return tot;
    }

    /** Returns the string representation of the board. 
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
