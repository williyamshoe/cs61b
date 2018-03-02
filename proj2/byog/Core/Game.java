package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private final String BLOCK = "████████████";

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        TETile[][] board = drawMenu();
        ter.initialize(WIDTH, HEIGHT + 5);

        Integer[] location = findFloor(board);

        board[location[0]][location[1]] = Tileset.PLAYER;

        ter.renderFrame(board);
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(Color.WHITE);
        while(true) {
            location = playerMovement(board, location);
            mouseMenu(board);
            StdDraw.show();
        }
    }

    private void mouseMenu(TETile[][] board) {
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.text(4, HEIGHT + 4, BLOCK);

        StdDraw.setPenColor(Color.WHITE);
        String text = "Nothing";
        int x = (int) (StdDraw.mouseX());
        int y = (int) (StdDraw.mouseY());

        if (x < WIDTH && y < HEIGHT) {
            TETile tile = board[x][y];
            if (tile == Tileset.WALL) {
                text = "Wall";
            } else if (tile == Tileset.FLOOR) {
                text = "Floor";
            } else if (tile == Tileset.PLAYER) {
                text = "Player";
            }
        }
        StdDraw.text(4, HEIGHT + 4, text);
    }

    public TETile[][] drawMenu(){
        StdDraw.clear(Color.BLACK);
        Font gameName = new Font("Monospaced", Font.BOLD, 50);
        StdDraw.setFont(gameName);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(0.5, 0.8, "Escape CS61B");

        StdDraw.setFont(new Font("Monospaced",Font.BOLD, 30));
        StdDraw.text(0.5, 0.5, "New Game (N)");
        StdDraw.text(0.5,0.4, "Load Game (L)");
        StdDraw.text(0.5,0.3, "Quit (Q)");
        StdDraw.show();

        boolean pressed = false;
        String seed = "";
        TETile[][] finalWorldFrame = new TETile[1][1];

        while (!pressed) {
            if (StdDraw.hasNextKeyTyped()) {
                String typed = (StdDraw.nextKeyTyped() + "").toUpperCase();
                switch (typed){
                    case "N":
                        while (true) {
                            if (StdDraw.hasNextKeyTyped()) {
                                typed = StdDraw.nextKeyTyped() + "";
                                if (typed.equalsIgnoreCase("S")) {
                                    break;
                                }
                                seed += typed;
                            }
                        }
                        finalWorldFrame = playWithInputString("N"+seed+"S");
                        pressed = true;
                        break;

                    case "L":
                        finalWorldFrame = playWithInputString("L637628736418L");
                        pressed = true;
                        break;

                    case "Q":
                        saveGameState(finalWorldFrame);
                        System.exit(0);
                        pressed = true;
                        break;
                }
            }
        }

        return finalWorldFrame;
    }

    private Integer[] playerMovement(TETile[][] board, Integer[] location) {
        if (!StdDraw.hasNextKeyTyped()) {
            return location;
        }

        int playerx = location[0];
        int playery = location[1];

        int newplayerx = playerx;
        int newplayery = playery;

        char input = StdDraw.nextKeyTyped();

        if (input == 'w' && safeMovement(board, playerx, playery + 1)) {
            newplayery = playery + 1;
        } else if (input == 'd' && safeMovement(board, playerx + 1, playery )) {
            newplayerx = playerx + 1;
        } else if (input == 's' && safeMovement(board, playerx, playery - 1)) {
            newplayery = playery - 1;
        } else if (input == 'a' && safeMovement(board, playerx - 1, playery )) {
            newplayerx = playerx - 1;
        } else if (input == 'q') {
            saveGameState(board);
            System.exit(0);
        }
        if (newplayerx != playerx || newplayery != playery) {
            board[playerx][playery] = Tileset.FLOOR;
            board[newplayerx][newplayery] = Tileset.PLAYER;
            ter.renderFrame(board);
        }
        return new Integer[] {newplayerx, newplayery};
    }

    private Integer[] findFloor(TETile[][] board) {
        for (int i = 0; i < WIDTH; i += 1) {
            for (int j = 0; j < HEIGHT; j += 1) {
                if (board[i][j] == Tileset.FLOOR) {
                    return new Integer[] {i, j};
                }
            }
        }
        return null;
    }

    private boolean safeMovement(TETile[][] board, int playerx, int playery) {
        if (board[playerx][playery] == Tileset.WALL) {
            return false;
        }
        return true;
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

        input = input.toUpperCase();
        int length = input.length();
        String firstLetter = input.substring(0, 1);

        switch(firstLetter) {
            case "N":
                long seed = Long.parseLong(input.substring(1, input.indexOf("S")));
                WorldGen world = new WorldGen(tiles, seed, Tileset.FLOOR, Tileset.WALL);
                world.makeRooms(tiles);
                world.makeHallways();
                break;
            case "L":
                try {
                    tiles = loadGameState();
                    break;
                } catch (NullPointerException e) {
                    System.out.println("No saved world");
                }
        }
        if (input.substring(length - 2).equals(":Q")) {
            saveGameState(tiles);
        }
        return tiles;
    }

    private void saveGameState(TETile[][] world) {
        File f = new File("./gameState.ser");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(world);
            os.close();
        }  catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    private TETile[][] loadGameState() {
        File f = new File("./gameState.ser");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                TETile[][] loadWorld = (TETile[][]) os.readObject();
                os.close();
                return loadWorld;
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }
        return null;
    }

    public static void main(String[] args) {
        Game g = new Game();
        g.playWithKeyboard();
    }
}
