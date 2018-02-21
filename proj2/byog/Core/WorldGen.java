package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import java.util.Random;


public class WorldGen {
    private static final int WIDTH = 80;
    private static final int HEIGHT = 30;
    private static int SEED = 83249;
    private int numRooms = 30;

    private static theRoom[] rooms;

    public WorldGen(TETile[][] world){
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        rooms = new theRoom[numRooms];
    }
    public int getNumRooms() {
        return numRooms;
    }

    public static void checkAndReplace(int xpos, int ypos, TETile tile, TETile[][] world) {
        if (xpos == 0 || ypos == 0 || xpos == WIDTH - 1 || ypos == HEIGHT - 1) {
            world[xpos][ypos] = Tileset.WALL;
        } else if (xpos >= 0 && ypos >= 0 && xpos < WIDTH && ypos < HEIGHT && world[xpos][ypos] != Tileset.FLOOR) {
            world[xpos][ypos] = tile;
        }
    }

    public static void addRoom(theRoom room, TETile[][] world){
        int xLim = room.xPos + room.roomWidth + 1;
        int yLim = room.yPos + room.roomHeight + 1;
        for (int x = room.xPos; x < xLim; x += 1) {
            for (int y = room.yPos; y < yLim; y += 1) {
                if ((x == room.xPos || x == xLim -1 || y == room.yPos || y == yLim -1) && (world[x][y] != Tileset.FLOOR)){
                    world[x][y] = Tileset.WALL;
                }
                else world[x][y] = Tileset.FLOOR;
            }
        }
    }


    public static void main(String[] args){
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] TILES = new TETile[WIDTH][HEIGHT];
        WorldGen world = new WorldGen(TILES);

        Random ran = new Random(SEED);
        for (int i = 0; i < world.getNumRooms(); i += 1) {
            theRoom room = new theRoom(4, 8, TILES, ran);
            rooms[i] = room;
            world.addRoom(room, TILES);
        }

        theRoom beginning = rooms[0];
        for (int i = 1; i < rooms.length; i += 1) {
            beginning.makeHall(rooms[i].findRandomSpot(WIDTH, HEIGHT), WIDTH, HEIGHT);
            beginning = rooms[i];
        }

        ter.renderFrame(TILES);
    }
}

