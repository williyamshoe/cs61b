package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdAudio;
import edu.princeton.cs.introcs.StdDraw;

import java.util.Random;

public class HelperMethods {
    private static final int WIDTH = 80;
    private static final int HEIGHT = 30;

    private static boolean striggered = false;
    protected static Integer[] flag = null;

    protected static int flagcount1 = 0;
    protected static int flagcount2 = 0;

    protected static int blockedleft1 = 1;
    protected static int blockedleft2 = 1;

    protected static boolean blockedtrig1 = false;
    protected static boolean blockedtrig2 = false;

    protected static boolean stunned1 = false;
    protected static boolean stunned2 = false;

    protected static int stunCountdown1 = 9;
    protected static int stunCountdown2 = 9;

    protected static long seed;
    protected static Random ran = null;

    protected static boolean newseed = false;

    protected static Integer[] findOrMakePlayer(TETile[][] board, long s, TETile tile) {
        Integer[] location = null;
        HelperMethods.seed = s;
        if (newseed) {
            ran = new Random(seed);
        }
        for (int i = 0; i < WIDTH; i += 1) {
            for (int j = 0; j < HEIGHT; j += 1) {
                if (board[i][j].description().equals(tile.description())) {
                    location = new Integer[] {i, j};
                }
            }
        }
        if (location == null) {
            location = findFloor(board);
        }
        board[location[0]][location[1]] = tile;
        return location;
    }

    private static Integer[] findFloor(TETile[][] board) {
        while (true) {
            int w = ran.nextInt(WIDTH);
            int h = ran.nextInt(HEIGHT);
            if (board[w][h].description().equals("floor")) {
                return new Integer[] {w, h};
            }
        }
    }

    protected static void updateFlag(TETile[][] board, TERenderer ter) {
        if (newseed) {
            flag = findOrMakePlayer(board, seed, Tileset.FLAG);
            ter.renderFrame(board);
        } else if (board[flag[0]][flag[1]].description().equals("player1")) {
            StdAudio.play("/audio/claptrimmed.wav");
            flagcount1 += 1;
            blockedleft1 += 1;
            flag = findOrMakePlayer(board, seed, Tileset.FLAG);
            ter.renderFrame(board);
        } else if (board[flag[0]][flag[1]].description().equals("player2")) {
            StdAudio.play("/audio/claptrimmed.wav");
            flagcount2 += 1;
            blockedleft2 += 1;
            flag = findOrMakePlayer(board, seed, Tileset.FLAG);
            ter.renderFrame(board);
        }
    }

    protected static void updateFlagnoshow(TETile[][] board) {
        if (newseed) {
            flag = findOrMakePlayer(board, seed, Tileset.FLAG);
        } else if (board[flag[0]][flag[1]].description().equals("player1")) {
            flagcount1 += 1;
            blockedleft1 += 1;
            flag = findOrMakePlayer(board, seed, Tileset.FLAG);
        } else if (board[flag[0]][flag[1]].description().equals("player2")) {
            flagcount2 += 1;
            blockedleft2 += 1;
            flag = findOrMakePlayer(board, seed, Tileset.FLAG);
        }
    }

    protected static Integer[][] move(TETile[][] b, Integer[][] l, TERenderer t) {
        if (!StdDraw.hasNextKeyTyped()) {
            return l;
        }
        char c = StdDraw.nextKeyTyped();

        int player1x = l[0][0];
        int player1y = l[0][1];
        int player2x = l[1][0];
        int player2y = l[1][1];

        int newplayer1x = player1x;
        int newplayer1y = player1y;
        int newplayer2x = player2x;
        int newplayer2y = player2y;

        if (!stunned1) {
            if (StdDraw.isKeyPressed(87) && safeMovement(b, player1x, player1y + 1)) {
                newplayer1y = player1y + 1;
            } else if (StdDraw.isKeyPressed(68) && safeMovement(b, player1x + 1, player1y)) {
                newplayer1x = player1x + 1;
            } else if (StdDraw.isKeyPressed(65) && safeMovement(b, player1x - 1, player1y)) {
                newplayer1x = player1x - 1;
            } else if (((!striggered && c == 's') || (striggered && StdDraw.isKeyPressed(83)))
                    && safeMovement(b, player1x, player1y - 1)) {
                newplayer1y = player1y - 1;
                striggered = true;
            } else if (StdDraw.isKeyPressed(69) && blockedleft1 > 0) {
                blockedleft1 -= 1;
                blockedtrig1 = true;
            }
            if (newplayer1x != player1x || newplayer1y != player1y) {
                TETile tile = b[player1x][player1y];
                TETile newtile = b[newplayer1x][newplayer1y];
                if (blockedtrig1) {
                    blockedtrig1 = false;
                    b[player1x][player1y] = Tileset.WALLBLOCK1;
                } else {
                    b[player1x][player1y] = Tileset.FLOOR;
                }
                if (newtile.description().equals("stun2")) {
                    stunned1 = true;
                }
                b[newplayer1x][newplayer1y] = tile;
            }
        } else {
            if (stunCountdown1 == 0) {
                stunned1 = false;
                stunCountdown1 = 10;
            }
            stunCountdown1 -= 1;
        }

        if (StdDraw.isKeyPressed(81)) {
            SaveAndLoadStream.saveGameState(b);
            System.exit(0);
        }

        if (!stunned2) {
            if (StdDraw.isKeyPressed(73) && safeMovement(b, player2x, player2y + 1)) {
                newplayer2y = player2y + 1;
            } else if (StdDraw.isKeyPressed(76) && safeMovement(b, player2x + 1, player2y)) {
                newplayer2x = player2x + 1;
            } else if (StdDraw.isKeyPressed(74) && safeMovement(b, player2x - 1, player2y)) {
                newplayer2x = player2x - 1;
            } else if (StdDraw.isKeyPressed(75) && safeMovement(b, player2x, player2y - 1)) {
                newplayer2y = player2y - 1;
            } else if (StdDraw.isKeyPressed(79) && blockedleft2 > 0) {
                blockedleft2 -= 1;
                blockedtrig2 = true;
            }
            if (newplayer2x != player2x || newplayer2y != player2y) {
                TETile tile = b[player2x][player2y];
                TETile newtile = b[newplayer2x][newplayer2y];
                if (blockedtrig2) {
                    blockedtrig2 = false;
                    b[player2x][player2y] = Tileset.WALLBLOCK2;
                } else {
                    b[player2x][player2y] = Tileset.FLOOR;
                }
                if (newtile.description().equals("stun1")) {
                    stunned2 = true;
                }
                b[newplayer2x][newplayer2y] = tile;
            }
        } else {
            if (stunCountdown2 == 0) {
                stunned2 = false;
                stunCountdown2 = 10;
            }
            stunCountdown2 -= 1;
        }

        if (newplayer2x != player2x || newplayer2y != player2y
                || newplayer1x != player1x || newplayer1y != player1y) {
            t.renderFrame(b);
        }
        return new Integer[][]{{newplayer1x, newplayer1y}, {newplayer2x, newplayer2y}};
    }

    protected static Integer[] player1MovementWithInput(TETile[][] board, Integer[] loc, char i) {
        int playerx = loc[0];
        int playery = loc[1];

        int newplayerx = playerx;
        int newplayery = playery;

        if (!stunned1) {
            if (i == 'w' && safeMovement(board, playerx, playery + 1)) {
                newplayery = playery + 1;
            } else if (i == 'd' && safeMovement(board, playerx + 1, playery)) {
                newplayerx = playerx + 1;
            } else if (i == 'a' && safeMovement(board, playerx - 1, playery)) {
                newplayerx = playerx - 1;
            } else if (i == 's' && safeMovement(board, playerx, playery - 1)) {
                newplayery = playery - 1;
                striggered = true;
            } else if (i == 'e' && blockedleft1 > 0) {
                blockedleft1 -= 1;
                blockedtrig1 = true;
            }
        } else {
            if (stunCountdown1 == 0) {
                stunned1 = false;
                stunCountdown1 = 10;
            }
            stunCountdown1 -= 1;
        }

        if (newplayerx != playerx || newplayery != playery) {
            TETile tile = board[playerx][playery];
            TETile newtile = board[newplayerx][newplayery];
            if (blockedtrig1) {
                blockedtrig1 = false;
                board[playerx][playery] = Tileset.WALLBLOCK1;
            } else {
                board[playerx][playery] = Tileset.FLOOR;
            }
            if (newtile.description().equals("stun2")) {
                stunned1 = true;
            }
            board[newplayerx][newplayery] = tile;
        }

        return new Integer[] {newplayerx, newplayery};
    }

    protected static Integer[] player2MovementWithInput(TETile[][] board, Integer[] loc, char i) {
        int playerx = loc[0];
        int playery = loc[1];

        int newplayerx = playerx;
        int newplayery = playery;

        if (!stunned2) {
            if (i == 'i' && safeMovement(board, playerx, playery + 1)) {
                newplayery = playery + 1;
            } else if (i == 'l' && safeMovement(board, playerx + 1, playery)) {
                newplayerx = playerx + 1;
            } else if (i == 'j' && safeMovement(board, playerx - 1, playery)) {
                newplayerx = playerx - 1;
            } else if (i == 'k' && safeMovement(board, playerx, playery - 1)) {
                newplayery = playery - 1;
                striggered = true;
            } else if (i == 'o' && blockedleft2 > 0) {
                blockedleft2 -= 1;
                blockedtrig2 = true;
            }
        } else {
            if (stunCountdown2 == 0) {
                stunned2 = false;
                stunCountdown2 = 10;
            }
            stunCountdown2 -= 1;
        }

        if (newplayerx != playerx || newplayery != playery) {
            TETile tile = board[playerx][playery];
            TETile newtile = board[newplayerx][newplayery];
            if (blockedtrig2) {
                blockedtrig2 = false;
                board[playerx][playery] = Tileset.WALLBLOCK2;
            } else {
                board[playerx][playery] = Tileset.FLOOR;
            }
            if (newtile.description().equals("stun1")) {
                stunned2 = true;
            }
            board[newplayerx][newplayery] = tile;
        }
        return new Integer[] {newplayerx, newplayery};
    }

    private static boolean safeMovement(TETile[][] board, int playerx, int playery) {
        return !board[playerx][playery].description().equals("wall")
                && !board[playerx][playery].description().equals("player1")
                && !board[playerx][playery].description().equals("player2");
    }
}
