import java.awt.Color;
import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture pic;

    public SeamCarver(Picture picture){
        pic = picture;
    }

    /*current picture*/
    public Picture picture() {
        return pic;
    }

    /*width of current picture*/
    public int width() {
        return pic.width();
    }

    /*height of current picture*/
    public int height() {
        return pic.height();
    }

    /*energy of pixel at column x and row y*/
    public double energy(int x, int y) {
        Color top = pic.get(x, Math.floorMod(y + 1, pic.height()));
        Color bottom = pic.get(x, Math.floorMod(y - 1, pic.height()));
        Color right = pic.get(Math.floorMod(x + 1, pic.width()), y);
        Color left = pic.get(Math.floorMod(x - 1, pic.width()), y);

        int deltayred = (top.getRed() - bottom.getRed()) * (top.getRed() - bottom.getRed());
        int deltaygreen = (top.getGreen() - bottom.getGreen()) * (top.getGreen() - bottom.getGreen());
        int deltayblue = (top.getBlue() - bottom.getBlue()) * (top.getBlue() - bottom.getBlue());

        int deltaxred = (left.getRed() - right.getRed()) * (left.getRed() - right.getRed());
        int deltaxgreen = (left.getGreen() - right.getGreen()) * (left.getGreen() - right.getGreen());
        int deltaxblue = (left.getBlue() - right.getBlue()) * (left.getBlue() - right.getBlue());

        return deltayred + deltaygreen + deltayblue + deltaxred + deltaxgreen + deltaxblue;
    }

    /*sequence of indices for horizontal seam*/
    public int[] findHorizontalSeam() {
        Picture end = new Picture(pic.height(), pic.width());

        for (int i = 0; i < pic.width(); i += 1) {
            for (int j = 0; j < pic.height(); j += 1) {
                end.set(j, i, pic.get(i, j));
            }
        }

        SeamCarver result = new SeamCarver(end);
        return result.findVerticalSeam();
    }

    /*sequence of indices for vertical seam*/
    public int[] findVerticalSeam() {
        double[][] cost = new double[pic.height()][pic.width()];
        int[][] path = new int[pic.height()][pic.width()];

        double mincost = Double.MAX_VALUE;
        int minindex = -1;

        for (int i = 0; i < pic.width(); i += 1) {
            cost[0][i] = energy(i, 0);
        }

        for (int y = 1; y < cost.length; y += 1) {
            for (int x = 0; x < pic.width(); x += 1) {
                if (x == 0) {
                    if (cost[y - 1][x] <= cost[y - 1][x + 1]) {
                        cost[y][x] = energy(x, y) + cost[y - 1][x];
                        path[y][x] = 0;
                    } else {
                        cost[y][x] = energy(x, y) + cost[y - 1][x + 1];
                        path[y][x] = 1;
                    }
                } else if (x == pic.width() - 1) {
                    if (cost[y - 1][x] <= cost[y - 1][x - 1]) {
                        cost[y][x] = energy(x, y) + cost[y - 1][x];
                        path[y][x] = 0;
                    } else {
                        cost[y][x] = energy(x, y) + cost[y - 1][x - 1];
                        path[y][x] = -1;
                    }
                } else {
                    if (cost[y - 1][x] <= cost[y - 1][x + 1] && cost[y - 1][x] <= cost[y - 1][x - 1]) {
                        cost[y][x] = energy(x, y) + cost[y - 1][x];
                        path[y][x] = 0;
                    } else if (cost[y - 1][x + 1] <= cost[y - 1][x - 1] && cost[y - 1][x + 1] <= cost[y - 1][x]) {
                        cost[y][x] = energy(x, y) + cost[y - 1][x + 1];
                        path[y][x] = 1;
                    } else {
                        cost[y][x] = energy(x, y) + cost[y - 1][x - 1];
                        path[y][x] = -1;
                    }
                }

                if (y == cost.length - 1) {
                    if (cost[y][x] < mincost) {
                        mincost = cost[y][x];
                        minindex = x;
                    }
                }
            }
        }

        int[] result = new int[path.length];
        result[path.length - 1] = minindex;
        for (int r = path.length - 2; r >= 0; r -= 1) {
            result[r] = path[r + 1][minindex] + minindex;
            minindex = result[r];
        }

        return result;
    }

    /*remove horizontal seam from picture*/
    public void removeHorizontalSeam(int[] seam) {
        pic = SeamRemover.removeHorizontalSeam(pic, seam);
    }

    /*remove vertical seam from picture*/
    public void removeVerticalSeam(int[] seam) {
        pic = SeamRemover.removeVerticalSeam(pic, seam);
    }
}