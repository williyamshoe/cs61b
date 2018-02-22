package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import java.util.Random;

public class WorldGen {
    private static final int WIDTH = 80;
    private static final int HEIGHT = 30;
    private Random ran;
    private int numRooms;
    protected TETile floor;
    protected TETile wall;

    private static theRoom[] rooms;

    WorldGen(TETile[][] world, long seed, TETile floor, TETile wall){
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        this.ran = new Random(seed);

        numRooms = ran.nextInt(6) + 14;
        rooms = new theRoom[numRooms];

        this.floor = floor;
        this.wall = wall;
    }
    private int getNumRooms() {
        return numRooms;
    }

    protected void checkAndReplace(int xpos, int ypos, TETile tile, TETile[][] world) {
        if (xpos == 0 || ypos == 0 || xpos == WIDTH - 1 || ypos == HEIGHT - 1) {
            world[xpos][ypos] = wall;
        } else if (xpos >= 0 && ypos >= 0 && xpos < WIDTH && ypos < HEIGHT && world[xpos][ypos] != floor) {
            world[xpos][ypos] = tile;
        }
    }

    private boolean checkOverlap (int xpos, int ypos, int xsize, int ysize, TETile[][] world) {
        return world[xpos][ypos] == floor || world[xpos + xsize][ypos] == floor ||
                world[xpos][ypos + ysize] == floor || world[xpos + xsize][ypos + ysize] == floor;
    }

    private void addRoom(theRoom room, TETile[][] world){
        int xLim = room.xPos + room.roomWidth + 1;
        int yLim = room.yPos + room.roomHeight + 1;
        for (int x = room.xPos; x < xLim; x += 1) {
            for (int y = room.yPos; y < yLim; y += 1) {
                if ((x == room.xPos || x == xLim -1 || y == room.yPos || y == yLim -1) && (world[x][y] != floor)){
                    world[x][y] = wall;
                }
                else world[x][y] = floor;
            }
        }
    }

    protected void makeRooms(WorldGen world, TETile[][] TILES) {
        for (int i = 0; i < world.getNumRooms(); i += 1) {
            int maxRange = 10;
            int minRange = 5;
            Random ran = world.ran;
            int xsize = ran.nextInt(maxRange + 1 - minRange) + minRange;
            int ysize = ran.nextInt(maxRange + 1 - minRange) + minRange;
            int xpos = ran.nextInt(WIDTH - xsize);
            int ypos = ran.nextInt(HEIGHT - ysize);

            while (checkOverlap(xpos, ypos, xsize, ysize, TILES)) {
                xsize = ran.nextInt(maxRange + 1 - minRange) + minRange;
                ysize = ran.nextInt(maxRange + 1 - minRange) + minRange;
                xpos = ran.nextInt(WIDTH - xsize);
                ypos = ran.nextInt(HEIGHT - ysize);
            }

            theRoom room = new theRoom(xsize, ysize, xpos, ypos, TILES, ran, world);

            rooms[i] = room;
            world.addRoom(room, TILES);
        }
    }

    protected void makeHallways() {
        theRoom beginning = rooms[0];
        for (int i = 1; i < rooms.length; i += 1) {
            beginning.makeHall(rooms[i].findRandomSpot(WIDTH, HEIGHT), WIDTH, HEIGHT);
            beginning = rooms[i];
        }
    }

    public static void main(String[] args){
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] TILES = new TETile[WIDTH][HEIGHT];
        int SEED = 91789;
        WorldGen world = new WorldGen(TILES, SEED, Tileset.FLOWER, Tileset.TREE);

        world.makeRooms(world, TILES);

        world.makeHallways();

        ter.renderFrame(TILES);
    }
}
