package hw2;

import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {

    private double[] data;
    private int iterations;

    /* perform T independent experiments on an N-by-N grid */
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (T <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        iterations = T;
        data = new double[T];
        for (int i = 0; i < T; i += 1) {
            Percolation world = pf.make(N);
            while (!world.percolates()) {
                world.open(StdRandom.uniform(N), StdRandom.uniform(N));
            }
            data[i] = world.numberOfOpenSites() / (1.0 * N * N);
        }
    }

    /* sample mean of percolation threshold */
    public double mean() {
        double sum = 0;
        for (int i = 0; i < data.length; i += 1) {
            sum += data[i];
        }
        return sum / iterations;
    }

    /* sample standard deviation of percolation threshold */
    public double stddev() {
        double m = mean();
        double sum = 0;
        for (int i = 0; i < data.length; i += 1) {
            sum += java.lang.Math.pow(data[i] - m, 2);
        }
        return java.lang.Math.pow(sum / (iterations - 1), 0.5);
    }

    /* low endpoint of 95% confidence interval */
    public double confidenceLow() {
        return mean() - 1.96 * stddev() * java.lang.Math.pow(iterations, -0.5);
    }

    /* high endpoint of 95% confidence interval */
    public double confidenceHigh() {
        return mean() + 1.96 * stddev() * java.lang.Math.pow(iterations, -0.5);
    }

    private static void main(String[] args) {
        PercolationStats n = new PercolationStats(20, 50, new PercolationFactory());
        System.out.println("mean: " + n.mean());
        System.out.println("stddev: " + n.stddev());
        System.out.println("lower bound: " + n.confidenceLow());
        System.out.println("upper bound: " + n.confidenceHigh());
    }
}