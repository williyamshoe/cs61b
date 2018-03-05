package byog.TileEngine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    private static Color c = new Color(216, 128, 128);
    public static final TETile PLAYER1 = new TETile('♚', Color.cyan, Color.black, "player1");
    public static final TETile PLAYER2 = new TETile('♚', Color.orange, Color.black, "player2");
    public static final TETile WALL = new TETile('#', c, Color.darkGray, "wall");
    public static final TETile WALLBLOCK1 = new TETile('⚡', Color.black, Color.cyan, "stun1");
    public static final TETile WALLBLOCK2 = new TETile('⚡', Color.black, Color.orange, "stun2");
    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
            "floor");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.black, "flower");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
    public static final TETile FLAG = new TETile('⚑', Color.magenta, Color.black, "flag");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");
}


