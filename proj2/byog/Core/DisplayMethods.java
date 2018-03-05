package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdAudio;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;

public class DisplayMethods {
    private static final int WIDTH = 80;
    private static final int HEIGHT = 30;
    private static final String BLOCK = "████████████████";
    protected static String s = "";
    private static final String NUMBERS = "1234567890s";

    protected static void mouseMenu(TETile[][] board) {
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.text(4, HEIGHT + 4, BLOCK);
        StdDraw.text(WIDTH - 26, HEIGHT + 4, BLOCK + BLOCK + BLOCK + BLOCK);
        StdDraw.setPenColor(Color.WHITE);
        String text = "nothing";
        int x = (int) (StdDraw.mouseX());
        int y = (int) (StdDraw.mouseY());
        if (x < WIDTH && y < HEIGHT) {
            TETile tile = board[x][y];
            text = tile.description();
        }

        StdDraw.text(WIDTH - 50, HEIGHT + 4, "Player 1's flags: ");
        StdDraw.text(WIDTH - 25, HEIGHT + 4, "Player 2's flags: ");

        StdDraw.text(4, HEIGHT + 4, text);
        StdDraw.setPenColor(Color.cyan);
        for (int i = 0; i < HelperMethods.flagcount1; i += 1) {
            StdDraw.text(WIDTH - 45 + i, HEIGHT + 4, "⚑");
        }
        StdDraw.setPenColor(Color.orange);
        for (int i = 0; i < HelperMethods.flagcount2; i += 1) {
            StdDraw.text(WIDTH - 20 + i, HEIGHT + 4, "⚑");
        }
    }

    private static String drawQueue() {
        StdDraw.clear(Color.BLACK);
        String seed = "";
        StdDraw.setFont(new Font("Monospaced", Font.PLAIN, 20));
        StdDraw.text(0.5 * 30, 0.7 * 30, "When you are finished, type (s)");
        StdDraw.setFont(new Font("Monospaced", Font.BOLD, 30));
        StdDraw.text(0.5 * 30, 0.9 * 30, "Please type in a");
        StdDraw.text(0.5 * 30, 0.8 * 30, "numerical seed:");
        StdDraw.show();
        boolean warning  = false;
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                StdDraw.setPenColor(Color.BLACK);
                StdDraw.text(0.5 * 30, 0.4 * 30, BLOCK);
                String typed = StdDraw.nextKeyTyped() + "";
                if (((typed.equalsIgnoreCase("s") && seed.length() == 0)
                        || !NUMBERS.contains(typed)) && !warning) {
                    warning = true;
                    StdDraw.setPenColor(Color.WHITE);
                    StdDraw.setFont(new Font("Monospaced", Font.BOLD, 30));
                    StdDraw.text(0.5 * 30, 0.91 * 30, BLOCK);
                    StdDraw.text(0.5 * 30, 0.85 * 30, BLOCK);
                    StdDraw.text(0.5 * 30, 0.79 * 30, BLOCK);
                    StdDraw.text(0.5 * 30, 0.4 * 30, seed);
                    StdDraw.setPenColor(Color.BLACK);
                    StdDraw.text(0.5 * 30, 0.9 * 30, "Please type in a");
                    StdDraw.text(0.5 * 30, 0.8 * 30, "numerical seed:");
                    StdDraw.show();
                }
                if (typed.equalsIgnoreCase("s") && seed.length() > 0) {
                    break;
                } else if (!typed.equalsIgnoreCase("s") && NUMBERS.contains(typed)) {
                    seed += typed;
                    StdDraw.setPenColor(Color.WHITE);
                    StdDraw.text(0.5 * 30, 0.4 * 30, seed);
                    StdDraw.show();
                }
            }
        }
        return seed;
    }

    protected static void win(int player) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new Font("Monospaced", Font.BOLD, 30));
        StdDraw.text(0.5 * 30, 0.8 * 30, "Player " + player + " wins!!");
        StdDraw.setFont(new Font("Monospaced", Font.ITALIC, 20));
        StdDraw.text(0.5 * 30, 0.7 * 30, "press (b) to go back to menu");
        StdDraw.text(0.5 * 30, 0.6 * 30, "press (q) to quit");
        StdDraw.show();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char input = StdDraw.nextKeyTyped();
                if (input == 'b') {
                    Game g = new Game();
                    StdAudio.close();
                    g.playWithKeyboard();
                } else if (input == 'q') {
                    System.exit(0);
                }
            }
        }
    }

    protected static String noSavedState(TERenderer ter) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new Font("Monospaced", Font.BOLD, 30));
        StdDraw.text(0.5 * 30, 0.8 * 30, "No Saved State Found");
        StdDraw.setFont(new Font("Monospaced", Font.ITALIC, 20));
        StdDraw.text(0.5 * 30, 0.7 * 30, "press (b) to go back");
        StdDraw.show();
        while (true) {
            if (StdDraw.hasNextKeyTyped() && StdDraw.nextKeyTyped() == 'b') {
                return DisplayMethods.drawMenu(ter);
            }
        }
    }

    private static String howToPlay(TERenderer ter) {
        ter.initialize(50, 30);
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new Font("Monospaced", Font.BOLD, 30));
        StdDraw.text(25, 0.925 * 30, "Playing capture the flag:");
        StdDraw.setFont(new Font("Monospaced", Font.PLAIN, 19));
        StdDraw.text(25, 0.75 * 30, "This is a 2 player game, where each player's objective is to");
        StdDraw.text(25, 0.70 * 30, "capture randomly generated flags in a randomly generated");
        StdDraw.text(25, 0.65 * 30, "world. Player 1's movement controls are (w)(a)(s)(d)" +
                ", while player");
        StdDraw.text(25, 0.60 * 30, "2's are (i)(j)(k)(l). First one to capture 10 " +
                "flags is the winner.");
        StdDraw.text(25, 0.50 * 30, "Additionally, each player can also place blocks to stun the");
        StdDraw.text(25, 0.45 * 30, "opponent, with the keys (e) and (o), respectively" +
                ", to drop the");
        StdDraw.text(25, 0.40 * 30, "block at the current location of the player. " +
                "The more flags you");
        StdDraw.text(25, 0.35 * 30, "collect, the more stun blocks you can place!");
        StdDraw.setPenColor(Color.cyan);
        StdDraw.setFont(new Font("Monospaced", Font.PLAIN, 23));
        StdDraw.text(12.5, 0.225 * 30, "Player 1 : ♚");
        StdDraw.setPenColor(Color.orange);
        StdDraw.text(37.5, 0.225 * 30, "Player 2 : ♚");
        StdDraw.setPenColor(Color.white);
        StdDraw.text(25, 0.10 * 30, "press (b) to go back to menu");
        StdDraw.show();
        while (true) {
            if (StdDraw.hasNextKeyTyped() && StdDraw.nextKeyTyped() == 'b') {
                ter.initialize(30, 30);
                return DisplayMethods.drawMenu(ter);
            }
        }
    }

    protected static String drawMenu(TERenderer ter) {
        StdDraw.clear(Color.BLACK);
        Font gameName = new Font("Monospaced", Font.BOLD, 45);
        StdDraw.setFont(gameName);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(0.5 * 30, 0.8 * 30, "Capture the Flag");
        StdDraw.setFont(new Font("Monospaced", Font.BOLD, 30));
        StdDraw.text(0.5 * 30, 0.5 * 30, "New Game (n)");
        StdDraw.text(0.5 * 30, 0.4 * 30, "How to Play (h)");
        StdDraw.text(0.5 * 30, 0.3 * 30, "Load Game (l)");
        StdDraw.text(0.5 * 30, 0.2 * 30, "Quit (q)");
        StdDraw.show();
        boolean pressed = false;
        String seed = "";
        while (!pressed) {
            if (StdDraw.hasNextKeyTyped()) {
                String typed = (StdDraw.nextKeyTyped() + "").toUpperCase();
                switch (typed) {
                    case "N":
                        s = drawQueue();
                        seed = "n" + s + "s";
                        pressed = true;
                        break;
                    case "L":
                        seed = "l";
                        pressed = true;
                        break;
                    case "Q":
                        System.exit(0);
                        pressed = true;
                        break;
                    case "H":
                        return howToPlay(ter);
                    default:
                }
            }
        }
        return seed;
    }
}
