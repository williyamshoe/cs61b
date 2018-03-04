package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;
import java.util.Random;

public class WorldGen implements Serializable {
    private static final int WIDTH = 80;
    private static final int HEIGHT = 30;
    private Random ran;
    private int numRooms;
    private TETile[][] world;
    protected TETile floor;
    protected TETile wall;
    private static final long serialVersionUID = 637628736418L;

    private static TheRoom[] rooms;

    WorldGen(TETile[][] world, long seed, TETile floor, TETile wall) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        this.world = world;

        this.ran = new Random(seed);

        numRooms = ran.nextInt(3) + 12;
        rooms = new TheRoom[numRooms];

        this.floor = floor;
        this.wall = wall;
    }
    private int getNumRooms() {
        return numRooms;
    }

    protected void checkAndReplace(int xpos, int ypos, TETile tile) {
        if (xpos == 0 || ypos == 0 || xpos == WIDTH - 1 || ypos == HEIGHT - 1) {
            world[xpos][ypos] = wall;
        } else if (xpos >= 0 && ypos >= 0 && xpos < WIDTH
                && ypos < HEIGHT && world[xpos][ypos] != floor) {
            world[xpos][ypos] = tile;
        }
    }

    private boolean checkOverlap(int xpos, int ypos, int xsize, int ysize) {
        return world[xpos][ypos] == floor || world[xpos + xsize][ypos] == floor
                || world[xpos][ypos + ysize] == floor || world[xpos + xsize][ypos + ysize] == floor;
    }

    private void addRoom(TheRoom room) {
        int xLim = room.xPos + room.roomWidth + 1;
        int yLim = room.yPos + room.roomHeight + 1;
        for (int x = room.xPos; x < xLim; x += 1) {
            for (int y = room.yPos; y < yLim; y += 1) {
                if ((x == room.xPos || x == xLim - 1 || y == room.yPos
                        || y == yLim - 1) && (world[x][y] != floor)) {
                    world[x][y] = wall;
                } else {
                    world[x][y] = floor;
                }
            }
        }
    }

    protected void makeRooms(TETile[][] tiles) {
        for (int i = 0; i < getNumRooms(); i += 1) {
            int maxRange = 9;
            int minRange = 5;
            int xsize = ran.nextInt(maxRange + 1 - minRange) + minRange;
            int ysize = ran.nextInt(maxRange + 1 - minRange) + minRange;
            int xpos = ran.nextInt(WIDTH - xsize);
            int ypos = ran.nextInt(HEIGHT - ysize);

            while (checkOverlap(xpos, ypos, xsize, ysize)) {
                xsize = ran.nextInt(maxRange + 1 - minRange) + minRange;
                ysize = ran.nextInt(maxRange + 1 - minRange) + minRange;
                xpos = ran.nextInt(WIDTH - xsize);
                ypos = ran.nextInt(HEIGHT - ysize);
            }

            TheRoom room = new TheRoom(xsize, ysize, xpos, ypos, tiles, ran, this);

            rooms[i] = room;
            addRoom(room);
        }
    }

    protected void makeHallways() {
        TheRoom beginning = rooms[0];
        for (int i = 1; i < rooms.length; i += 1) {
            beginning.makeHall(rooms[i].findRandomSpot(WIDTH, HEIGHT), WIDTH, HEIGHT);
            beginning = rooms[i];
        }
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] tiles = new TETile[WIDTH][HEIGHT];
        int seed = 91789;
        WorldGen world = new WorldGen(tiles, seed, Tileset.FLOWER, Tileset.TREE);

        world.makeRooms(tiles);

        world.makeHallways();

        ter.renderFrame(tiles);
    }
}
