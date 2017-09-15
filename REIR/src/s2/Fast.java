package s2;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Fast {
    public static void main(String[] args) {
        // Input handling
        In in = new In();
        Out out = new Out();
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt(), y = in.readInt();
            points[i] = new Point(x, y);
        }
        // Process stuff

        while (points.length > 1) {
            Arrays.sort(points);
            Point p = points[0];
            points = Arrays.copyOfRange(points, 1, points.length);

            Arrays.sort(points, p.SLOPE_ORDER);

            for (int i = 2; i < points.length; i++) {
                if (p.slopeTo(points[i - 2]) == p.slopeTo(points[i - 1]) &&
                        p.slopeTo(points[i - 1]) == p.slopeTo(points[i])) {
                    StdOut.print(p.toString() + " -> ");
                    StdOut.print(points[i - 2].toString() + " -> ");
                    StdOut.print(points[i - 1].toString() + " -> ");
                    StdOut.print(points[i].toString());
                    int j = i + 1;
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
