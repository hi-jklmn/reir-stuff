package s2;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Fast2 {
    public static void main(String[] args) {
        // Input handling
        In in = new In();
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt(), y = in.readInt();
            points[i] = new Point(x, y);
        }
        // Process stuff
        String output = "";
        while (points.length > 3) {
            Arrays.sort(points);
            Point p = points[0];
            points = Arrays.copyOfRange(points, 1, points.length);

            Arrays.sort(points, p.SLOPE_ORDER);

            for (int i = 2; i < points.length; i++) {
                if (p.slopeTo(points[i - 2]) == p.slopeTo(points[i - 1]) &&
                        p.slopeTo(points[i - 1]) == p.slopeTo(points[i])) {
                    StdOut.print(p.toString());
                    int j = i - 2;
                    while (j < points.length && p.slopeTo(points[j]) == p.slopeTo(points[i])) {
                        StdOut.print(" -> " + points[j].toString());
                        j++;
                    }
                    i = j - 1;
                    StdOut.println();
                }
            }
        }
    }
}
