package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    public static void addHexagon(TETile[][] tiles, int size, int x, int y, TETile tile) {

        int counter  = 0;
        int rowsize1 = 0;

        for (int rowsize = size; rowsize < size + 2 * size; rowsize += 2) {
            plotter(tiles, rowsize, size + 2 * size, x, y + counter, tile);
            counter += 1;
            rowsize1 = rowsize;
        }

        for (int rowsize = rowsize1; rowsize >= size; rowsize -= 2) {
            plotter(tiles, rowsize, size + 2 * size, x, y + counter, tile);
            counter += 1;
        }
    }

    public static void makeTess(TETile[][] tiles, int size) {
        int startx = 27;
        int starty = 45;
        int movementx = size * 2 - 1;
        int movementy = size;
        boolean completed = false;
        boolean odd = false;
        int count = 6;
        addHexagon(tiles, size, startx, starty, RandomWorldDemo.randomTile());
        while (count >= 0) {
            if (! odd) {
                starty -= movementy;
                startx -= movementx;
                addHexagon(tiles, size, startx, starty, RandomWorldDemo.randomTile());
                startx += 2 * movementx;
                addHexagon(tiles, size, startx, starty, RandomWorldDemo.randomTile());
            }
            else {
                startx += movementx;
                starty -= movementy;
                addHexagon(tiles, size, startx, starty, RandomWorldDemo.randomTile());
                startx -= 4 * movementx;
                addHexagon(tiles, size, startx, starty, RandomWorldDemo.randomTile());
                startx += 2 * movementx;
                addHexagon(tiles, size, startx, starty, RandomWorldDemo.randomTile());
            }
            odd = ! odd;
            count -= 1;
        }
        addHexagon(tiles, size, startx - movementx, starty - movementy, RandomWorldDemo.randomTile());
    }

    private static void plotter(TETile[][] tiles, int length, int total, int x, int y, TETile tile) {
        int skip = (total - length) / 2 - 1;
        for (int i = 0; i < length; i += 1) {
            int num = skip + i + x;
            tiles[num][y] = tile;
        }
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(80, 60);

        TETile[][] tessTiles = new TETile[60][80];

        for (int i = 0; i < 60; i += 1) {
            for (int j = 0; j < 80; j += 1) {
                tessTiles[i][j] = Tileset.NOTHING;
            }
        }

        makeTess(tessTiles, 3);

        ter.renderFrame(tessTiles);
    }
}
