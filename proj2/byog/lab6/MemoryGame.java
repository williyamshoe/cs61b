package byog.lab6;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(seed, 40, 40);
        game.startGame();
    }

    public MemoryGame(int seed, int width, int height) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.rand = new Random(seed);
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();


        //TODO: Initialize random number generator
    }

    public String generateRandomString(int n) {
        //TODO: Generate random string of letters of length n
        String str = "";
        for (int i = 0; i < n; i += 1) {
            str += CHARACTERS[rand.nextInt(27)];
        }
        return str;
    }

    public void drawFrame(String s) {
        //TODO: Take the string and display it in the center of the screen
        //TODO: If game is not over, display relevant game information at the top of the screen
        StdDraw.clear(Color.BLACK);
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 30));
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(this.width / 2, this.height / 2, s);
        StdDraw.text(5, this.height - 3, "Round " + round);
        if (!playerTurn) {
            StdDraw.text(this.width / 2, this.height - 3, "Watch!");
        } else{
            StdDraw.text(this.width / 2, this.height - 3, "Type!");
        }
        StdDraw.text(this.width - 5, this.height - 3, ENCOURAGEMENT[rand.nextInt(7)]);
        StdDraw.show();
    }

    public void flashSequence(String letters) {
        //TODO: Display each character in letters, making sure to blank the screen between letters
        for (int i = 0; i < (letters.length()); i += 1) {
            drawFrame(letters.substring(i, i + 1));
            StdDraw.pause(1000);
            StdDraw.clear(Color.BLACK);
            StdDraw.show();
            StdDraw.pause(500);
        }
    }

    public String solicitNCharsInput(int n) {
        //TODO: Read n letters of player input
        String s = "";
        int i = 0;
        while (i < n) {
            if (StdDraw.hasNextKeyTyped()) {
                s += StdDraw.nextKeyTyped();
                i += 1;
            }
        }
        return s;
    }

    public void startGame() {
        //TODO: Set any relevant variables before the game starts

        //TODO: Establish Game loop

        round = 1;
        boolean playing = true;
        while (playing) {
            drawFrame("Round " + round);
            StdDraw.show();
            StdDraw.pause(2000);
            String test = generateRandomString(round);
            flashSequence(test);
            String input = solicitNCharsInput(round);
            if (!input.equals(test)) {
                drawFrame("Game Over! You made it to round " + round);
                StdDraw.show();
                StdDraw.pause(3000);
                playing = false;
            }
            round += 1;
        }
    }

}
