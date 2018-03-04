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
    private static Integer[] flag = null;
    protected static int flagcount1 = 0;
    protected static int flagcount2 = 0;

    private static int blockedleft1 = 1;
    private static boolean blockedtrig1 = false;

    private static int blockedleft2 = 1;
    private static boolean blockedtrig2 = false;

    private static boolean stunned1 = false;
    private static boolean stunned2 = false;

    private static int stunCountdown1 = 9;
    private static int stunCountdown2 = 9;

    private static long seed;
    private static Random ran = null;

    protected static Integer[] findOrMakePlayer(TETile[][] board, long seed, TETile tile) {
        Integer[] location = null;
        HelperMethods.seed = seed;
        if (ran == null) {
            ran = new Random(seed);
        }
        for (int i = 0; i < WIDTH; i += 1) {
            for (int j = 0; j < HEIGHT; j += 1) {
                if (board[i][j].description().equals(tile.description())) {
                    location = new Integer[] {i ,j};
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
        if (flag == null) {
            flag = findOrMakePlayer(board, seed, Tileset.FLOWER);
            ter.renderFrame(board);
        } else if (board[flag[0]][flag[1]].description().equals("player1")) {
            StdAudio.play("/byog/Core/claptrimmed.wav");
            flagcount1 += 1;
            blockedleft1 += 1;
            flag = findOrMakePlayer(board, seed, Tileset.FLOWER);
            ter.renderFrame(board);
        } else if (board[flag[0]][flag[1]].description().equals("player2")) {
            StdAudio.play("/byog/Core/claptrimmed.wav");
            flagcount2 += 1;
            blockedleft2 += 1;
            flag = findOrMakePlayer(board, seed, Tileset.FLOWER);
            ter.renderFrame(board);
        }
    }

    protected static void updateFlagnoshow(TETile[][] board) {
        if (flag == null) {
            flag = findOrMakePlayer(board, seed, Tileset.FLOWER);
        } else if (board[flag[0]][flag[1]].description().equals("player1")) {
            flagcount1 += 1;
            blockedleft1 += 1;
            flag = findOrMakePlayer(board, seed, Tileset.FLOWER);
        } else if (board[flag[0]][flag[1]].description().equals("player2")) {
            flagcount2 += 1;
            blockedleft2 += 1;
            flag = findOrMakePlayer(board, seed, Tileset.FLOWER);
        }
    }

    protected static Integer[][] playersMovement(TETile[][] board, Integer[][] locations, TERenderer ter) {
        if (!StdDraw.hasNextKeyTyped()) {
            return locations;
        }
        char c = StdDraw.nextKeyTyped();

        int player1x = locations[0][0];
        int player1y = locations[0][1];
        int player2x = locations[1][0];
        int player2y = locations[1][1];

        int newplayer1x = player1x;
        int newplayer1y = player1y;
        int newplayer2x = player2x;
        int newplayer2y = player2y;

        if (!stunned1) {
            if (StdDraw.isKeyPressed(87) && safeMovement(board, player1x, player1y + 1)) {
                newplayer1y = player1y + 1;
            } else if (StdDraw.isKeyPressed(68) && safeMovement(board, player1x + 1, player1y)) {
                newplayer1x = player1x + 1;
            } else if (StdDraw.isKeyPressed(65) && safeMovement(board, player1x - 1, player1y)) {
                newplayer1x = player1x - 1;
            } else if (((!striggered && c == 's') || (striggered && StdDraw.isKeyPressed(83))) &&
                    safeMovement(board, player1x, player1y - 1)) {
                newplayer1y = player1y - 1;
                striggered = true;
            } else if (StdDraw.isKeyPressed(69) && blockedleft1 > 0) {
                blockedleft1 -= 1;
                blockedtrig1 = true;
            }
            if (newplayer1x != player1x || newplayer1y != player1y) {
                TETile tile = board[player1x][player1y];
                TETile newtile = board[newplayer1x][newplayer1y];
                if (blockedtrig1) {
                    blockedtrig1 = false;
                    board[player1x][player1y] = Tileset.WALLBLOCK1;
                } else {
                    board[player1x][player1y] = Tileset.FLOOR;
                }
                if (newtile.description().equals("stun2")) {
                    stunned1 = true;
                }
                board[newplayer1x][newplayer1y] = tile;
            }
        } else {
            if (stunCountdown1 == 0) {
                stunned1 = false;
                stunCountdown1 = 10;
            }
            stunCountdown1 -= 1;
        }

        if (StdDraw.isKeyPressed(81)) {
            SaveAndLoadStream.saveGameState(board);
            System.exit(0);
        }

        if (!stunned2) {
            if (StdDraw.isKeyPressed(73) && safeMovement(board, player2x, player2y + 1)) {
                newplayer2y = player2y + 1;
            } else if (StdDraw.isKeyPressed(76) && safeMovement(board, player2x + 1, player2y)) {
                newplayer2x = player2x + 1;
            } else if (StdDraw.isKeyPressed(74) && safeMovement(board, player2x - 1, player2y)) {
                newplayer2x = player2x - 1;
            } else if (StdDraw.isKeyPressed(75) && safeMovement(board, player2x, player2y - 1)) {
                newplayer2y = player2y - 1;
            } else if (StdDraw.isKeyPressed(79) && blockedleft2 > 0) {
                blockedleft2 -= 1;
                blockedtrig2 = true;
            }
            if (newplayer2x != player2x || newplayer2y != player2y) {
                TETile tile = board[player2x][player2y];
                TETile newtile = board[newplayer2x][newplayer2y];
                if (blockedtrig2) {
                    blockedtrig2 = false;
                    board[player2x][player2y] = Tileset.WALLBLOCK2;
                } else {
                    board[player2x][player2y] = Tileset.FLOOR;
                }
                if (newtile.description().equals("stun1")) {
                    stunned2 = true;
                }
                board[newplayer2x][newplayer2y] = tile;
            }
        } else {
            if (stunCountdown2 == 0) {
                stunned2 = false;
                stunCountdown2 = 10;
            }
            stunCountdown2 -= 1;
        }

        if (newplayer2x != player2x || newplayer2y != player2y || newplayer1x != player1x || newplayer1y != player1y) {
            ter.renderFrame(board);
        }
        return new Integer[][]{{newplayer1x, newplayer1y}, {newplayer2x, newplayer2y}};
    }

    protected static Integer[] player1MovementWithInput(TETile[][] board, Integer[] location, char input) {
        int playerx = location[0];
        int playery = location[1];

        int newplayerx = playerx;
        int newplayery = playery;

        if (!stunned1) {
            if (input == 'w' && safeMovement(board, playerx, playery + 1)) {
                newplayery = playery + 1;
            } else if (input == 'd' && safeMovement(board, playerx + 1, playery)) {
                newplayerx = playerx + 1;
            } else if (input == 'a' && safeMovement(board, playerx - 1, playery)) {
                newplayerx = playerx - 1;
            } else if (input == 's' && safeMovement(board, playerx, playery - 1)) {
                newplayery = playery - 1;
                striggered = true;
            } else if (input == 'e' && blockedleft1 > 0) {
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

    protected static Integer[] player2MovementWithInput(TETile[][] board, Integer[] location, char input) {
        int playerx = location[0];
        int playery = location[1];

        int newplayerx = playerx;
        int newplayery = playery;

        if (!stunned2) {
            if (input == 'i' && safeMovement(board, playerx, playery + 1)) {
                newplayery = playery + 1;
            } else if (input == 'l' && safeMovement(board, playerx + 1, playery)) {
                newplayerx = playerx + 1;
            } else if (input == 'j' && safeMovement(board, playerx - 1, playery)) {
                newplayerx = playerx - 1;
            } else if (input == 'k' && safeMovement(board, playerx, playery - 1)) {
                newplayery = playery - 1;
                striggered = true;
            } else if (input == 'o' && blockedleft2 > 0) {
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
        return !board[playerx][playery].description().equals("wall") &&
                !board[playerx][playery].description().equals("player1") &&
                !board[playerx][playery].description().equals("player2");
    }
}
