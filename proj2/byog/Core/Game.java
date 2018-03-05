package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdAudio;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Game {
    private TERenderer ter;
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private boolean finished = false;
    private long seed;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        ter = new TERenderer();
        StdAudio.loop("/audio/megalovania.wav");
        ter.initialize(30, 30);
        TETile[][] board = playWithInputString(DisplayMethods.drawMenu(ter));
        if (DisplayMethods.s.equals("")) {
            seed = HelperMethods.seed;
        } else {
            seed = Long.parseLong(DisplayMethods.s);
        }
        ter.initialize(WIDTH, HEIGHT + 5);

        Integer[] location1 = HelperMethods.findOrMakePlayer(board, seed, Tileset.PLAYER1);
        Integer[] location2 = HelperMethods.findOrMakePlayer(board, seed, Tileset.PLAYER2);
        Integer[][] locations = new Integer[][] {location1, location2};
        HelperMethods.updateFlag(board, ter);

        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(Color.WHITE);
        while (!finished) {
            locations = HelperMethods.move(board, locations, ter);
            HelperMethods.updateFlag(board, ter);
            DisplayMethods.mouseMenu(board);
            StdDraw.show();
            if (HelperMethods.flagcount1 >= 10) {
                ter.initialize(30, 30);
                DisplayMethods.win(1);
                finished = true;
            } else if (HelperMethods.flagcount2 >= 10) {
                ter.initialize(30, 30);
                DisplayMethods.win(2);
                finished = true;
            }
        }
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
        // Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        TETile[][] tiles = new TETile[WIDTH][HEIGHT];

        String firstLetter = input.substring(0, 1);

        switch (firstLetter) {
            case "n":
                String s = input.substring(1, input.indexOf("s"));
                HelperMethods.newseed = true;
                this.seed = Long.parseLong(s);
                WorldGen world = new WorldGen(tiles, seed, Tileset.FLOOR, Tileset.WALL);
                world.makeRooms(tiles);
                world.makeHallways();
                input = input.substring(input.indexOf("s") + 1, input.length());
                break;
            case "l":
                try {
                    HelperMethods.newseed = false;
                    File f = new File("./gameState.txt");
                    FileInputStream fs = new FileInputStream(f);
                    tiles = SaveAndLoadStream.loadGameState(fs);
                    input = input.substring(1, input.length());
                } catch (FileNotFoundException e) {
                    input = input.substring(1, input.length());
                    tiles = playWithInputString(DisplayMethods.noSavedState(ter));
                }
                break;
            case "q":
                tiles = null;
            default:
        }
        Integer[] location1 = HelperMethods.findOrMakePlayer(tiles, seed, Tileset.PLAYER1);
        Integer[] location2 = HelperMethods.findOrMakePlayer(tiles, seed, Tileset.PLAYER2);
        while (!input.equals("") && !input.equals(":q") && !input.equals("q")) {
            location1 = HelperMethods.player1MovementWithInput(tiles, location1, input.charAt(0));
            location2 = HelperMethods.player2MovementWithInput(tiles, location2, input.charAt(0));
            HelperMethods.updateFlagnoshow(tiles);
            input = input.substring(1, input.length());
        }

        if (input.length() >= 2 && input.substring(input.length() - 1).equals("q")) {
            SaveAndLoadStream.saveGameState(tiles);
        }

        return tiles;
    }

    private static void main(String[] args) {
        Game g = new Game();
        g.ter = new TERenderer();
        g.ter.initialize(WIDTH, HEIGHT + 5);
        //n98437swwikjlsokwedwdi:q
        //laaaawwllllkikkkssssaaaaew
        //TETile[][] w = g.playWithInputString("n5513998301767302084swwadsa");
        //g.ter.renderFrame(w);
        //n4031671086352609271saasaasdaw
        //n4031671086352609271saasaasdaw
        //g.playWithKeyboard();
        //StdDraw.pause(1000);
        //StdDraw.clear(Color.BLACK);
        //StdDraw.pause(1000);
        TETile[][] x = g.playWithInputString("lwadsw");
        g.ter.renderFrame(x);
    }
}
