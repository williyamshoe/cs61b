package byog.Core;

import byog.TileEngine.TETile;
import java.util.Random;

public class TheRoom {
    protected int roomHeight;
    protected int roomWidth;
    protected int xPos;
    protected int yPos;
    private Random ran;
    protected TETile[][] world;
    private WorldGen w;

    TheRoom(int xsize, int ysize, int xpos, int ypos, TETile[][] world, Random ran, WorldGen w) {
        this.roomHeight = ysize;
        this.roomWidth = xsize;

        this.xPos = xpos;
        this.yPos = ypos;

        this.ran = ran;
        this.world = world;

        this.w = w;
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
        int[] startpt = findRandomSpot(width, height);
        int[] difference = new int[] {destination[0] - startpt[0], destination[1] - startpt[1]};
        int distance = Math.abs(difference[0]) + Math.abs(difference[1]);
        while (distance != 0) {
            makeHallSection(startpt);
            if (ran.nextInt(distance) < Math.abs(difference[0])) {
                startpt[0] += difference[0] / Math.abs(difference[0]);
            } else {
                startpt[1] += difference[1] / Math.abs(difference[1]);
            }
            difference = new int[] {destination[0] - startpt[0], destination[1] - startpt[1]};
            distance = Math.abs(difference[0]) + Math.abs(difference[1]);
        }
        makeHallSection(destination);
    }

    private void makeHallSection(int[] locationOfPath) {
        w.checkAndReplace(locationOfPath[0], locationOfPath[1], w.floor);
        w.checkAndReplace(locationOfPath[0] + 1, locationOfPath[1], w.wall);
        w.checkAndReplace(locationOfPath[0] - 1, locationOfPath[1], w.wall);
        w.checkAndReplace(locationOfPath[0], locationOfPath[1] + 1, w.wall);
        w.checkAndReplace(locationOfPath[0], locationOfPath[1] - 1, w.wall);
        w.checkAndReplace(locationOfPath[0] + 1, locationOfPath[1] + 1, w.wall);
        w.checkAndReplace(locationOfPath[0] + 1, locationOfPath[1] - 1, w.wall);
        w.checkAndReplace(locationOfPath[0] - 1, locationOfPath[1] + 1, w.wall);
        w.checkAndReplace(locationOfPath[0] - 1, locationOfPath[1] - 1, w.wall);
    }
}
