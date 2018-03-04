package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdAudio;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;

public class Game {
    private TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private boolean finished = false;
    private long seed;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        //StdAudio.loop("/byog/Core/megalovania.wav");
        ter.initialize(30, 30);
        TETile[][] board = playWithInputString(DisplayMethods.drawMenu(ter));
        StdAudio.close();
        seed = Long.parseLong(DisplayMethods.s);
        ter.initialize(WIDTH, HEIGHT + 5);

        Integer[] location1 = HelperMethods.findOrMakePlayer(board, seed, Tileset.PLAYER1);
        Integer[] location2 = HelperMethods.findOrMakePlayer(board, seed, Tileset.PLAYER2);
        Integer[][] locations = new Integer[][] {location1, location2};
        HelperMethods.updateFlag(board, ter);

        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(Color.WHITE);
        while(!finished) {
            locations = HelperMethods.playersMovement(board, locations, ter);
            HelperMethods.updateFlag(board, ter);
            DisplayMethods.mouseMenu(board);
            StdDraw.show();
            if (HelperMethods.flagcount1 >= 10){
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

        switch(firstLetter) {
            case "n":
                ter.initialize(WIDTH, HEIGHT + 5);
                long seed = Long.parseLong(input.substring(1, input.indexOf("s")));
                WorldGen world = new WorldGen(tiles, seed, Tileset.FLOOR, Tileset.WALL);
                world.makeRooms(tiles);
                world.makeHallways();
                input = input.substring(input.indexOf("s") + 1, input.length());
                break;
            case "l":
                try {
                    File f = new File("./gameState.ser");
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
        }
        Integer[] location1 = HelperMethods.findOrMakePlayer(tiles, seed, Tileset.PLAYER1);
        Integer[] location2 = HelperMethods.findOrMakePlayer(tiles, seed, Tileset.PLAYER2);
        while (!input.equals("") && !input.equals(":q")) {
            location1 = HelperMethods.player1MovementWithInput(tiles, location1, input.charAt(0));
            location2 = HelperMethods.player2MovementWithInput(tiles, location2, input.charAt(0));
            HelperMethods.updateFlagnoshow(tiles);
            input = input.substring(1, input.length());
        }

        if (input.length() >= 2 && input.substring(input.length() - 2).equals(":q")) {
            SaveAndLoadStream.saveGameState(tiles);
        }

        return tiles;
    }

    public static void main(String[] args) {
        Game g = new Game();
        //n98437swwikjlsokwedwd
        //TETile[][] w = g.playWithInputString("n98437swwikjlsokwedwd");
        //g.ter.renderFrame(w);
        g.playWithKeyboard();
    }
}
