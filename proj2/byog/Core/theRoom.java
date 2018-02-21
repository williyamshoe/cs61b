package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import java.util.Random;

public class theRoom {
    protected int roomHeight;
    protected int roomWidth;
    protected int xPos;
    protected int yPos;
    protected Random ran;
    protected TETile[][] world;

    public theRoom(int minRange, int maxRange, TETile[][] world, Random ran) {
        this.roomHeight = ran.nextInt(maxRange + 1 - minRange) + minRange;
        this.roomWidth = ran.nextInt(maxRange + 1 - minRange) + minRange;

        this.xPos = Math.max(0,ran.nextInt(world.length) - roomWidth);
        this.yPos = Math.max(0,ran.nextInt(world[0].length) - roomHeight);

        this.ran = ran;
        this.world = world;
    }

    public int[] findRandomSpot(int width, int height) {
        int x =  ran.nextInt(roomWidth) + xPos;
        int y =  ran.nextInt(roomHeight) + yPos;
        int[] xy = new int[] {x, y};
        if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
            return findRandomSpot(width, height);
        }
        return xy;
    }

    public void makeHall(int[] destination, int width, int height) {
        int[] startingpt = findRandomSpot(width, height);
        int[] difference = new int[] {destination[0] - startingpt[0], destination[1] - startingpt[1]};
        int distance = Math.abs(difference[0]) + Math.abs(difference[1]);
        while (distance != 0) {
            makeHallSection(startingpt);
            if (ran.nextInt(distance) < Math.abs(difference[0])) {
                startingpt[0] += difference[0] / Math.abs(difference[0]);
            } else {
                startingpt[1] += difference[1] / Math.abs(difference[1]);
            }
            difference = new int[] {destination[0] - startingpt[0], destination[1] - startingpt[1]};
            distance = Math.abs(difference[0]) + Math.abs(difference[1]);
        }
        makeHallSection(destination);
    }

    public void makeHallSection(int[] locOfPath) {
        WorldGen.checkAndReplace(locOfPath[0], locOfPath[1], Tileset.FLOOR, world);
        WorldGen.checkAndReplace(locOfPath[0] + 1, locOfPath[1], Tileset.WALL, world);
        WorldGen.checkAndReplace(locOfPath[0] - 1, locOfPath[1], Tileset.WALL, world);
        WorldGen.checkAndReplace(locOfPath[0], locOfPath[1] + 1, Tileset.WALL, world);
        WorldGen.checkAndReplace(locOfPath[0], locOfPath[1] - 1, Tileset.WALL, world);
        WorldGen.checkAndReplace(locOfPath[0] + 1, locOfPath[1] + 1, Tileset.WALL, world);
        WorldGen.checkAndReplace(locOfPath[0] + 1, locOfPath[1] - 1, Tileset.WALL, world);
        WorldGen.checkAndReplace(locOfPath[0] - 1, locOfPath[1] + 1, Tileset.WALL, world);
        WorldGen.checkAndReplace(locOfPath[0] - 1, locOfPath[1] - 1, Tileset.WALL, world);
    }
}