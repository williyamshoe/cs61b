package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private TETile[][] saved = null;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {

    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        TETile[][] TILES = new TETile[WIDTH][HEIGHT];
        long seed = 0;
        for (int i = 1; i < input.length(); i += 1) {
            seed += (long) input.charAt(i);
        }
        WorldGen world = new WorldGen(TILES, seed, Tileset.FLOOR, Tileset.WALL);
        world.makeRooms(world, TILES);
        world.makeHallways();
        return TILES;
        /*TETile[][] TILES = new TETile[WIDTH][HEIGHT];

        long seed = 0;
        boolean save = false;

        if (input.charAt(0) == 'n' || input.charAt(0) == 'N') {
            for (int i = 1; i < input.length(); i += 1) {
                if (i == input.length() - 2 && input.charAt(i) == ':' && (input.charAt(i + 1) == 'q' || input.charAt(i + 1) == 'Q')) {
                    save = true;
                } else {
                    seed += (long) input.charAt(i);
                }
            }
            WorldGen world = new WorldGen(TILES, seed, Tileset.FLOOR, Tileset.WALL);
            world.makeRooms(world, TILES);
            world.makeHallways();
            if (save) {
                saved = TILES;
            }
            return TILES;
        } else if ((input.charAt(0) == 'l' || input.charAt(0) == 'L') && saved != null) {
            return saved;
        } else if ((input.charAt(0) == 'l' || input.charAt(0) == 'L') && saved == null) {
            return null;
        } else {
            return playWithInputString("n" + input);
        }*/
    }

    private static void main(String[] args) {
        Game g = new Game();
        TETile[][] board = g.playWithInputString("sd4fsd");
        g.ter.initialize(WIDTH, HEIGHT);
        g.ter.renderFrame(board);

        /*
        TETile[][] board1 = g.playWithInputString("L");
        g.ter.initialize(WIDTH, HEIGHT);
        g.ter.renderFrame(board1);
        */
    }
}
