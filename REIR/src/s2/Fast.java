package s2;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Fast {
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

//            int run = 0;
//            double current = p.slopeTo(points[0]);
//            for (int i = 1; i < points.length; i++) {
//                if (current == p.slopeTo(points[i])) {
//                    run++;
//                } else {
//                    current = p.slopeTo(points[i]);
//                    if (run > 0) {
//                        output += p.toString();
//                        for (int j = i-run; j < i; j++) {
//                            output += " -> ";
//                            output += points[j].toString();
//                        }
//                        output += "\n";
//                    }
//                    run = 0;
//                }
//            }

            for (int i = 2; i < points.length; i++) {
                if (p.slopeTo(points[i - 2]) == p.slopeTo(points[i - 1]) &&
                        p.slopeTo(points[i - 1]) == p.slopeTo(points[i])) {
                    output += (p.toString() + " -> ");
                    output += (points[i - 2].toString() + " -> ");
                    output += (points[i - 1].toString() + " -> ");
                    output += (points[i].toString());
                    int j = i + 1;
                    while (j < points.length && p.slopeTo(points[j]) == p.slopeTo(points[i])) {
                        output += (" -> " + points[j].toString());
                        j++;
                    }
                    i = j - 1;
                    output += "\n";
                }
            }
        }
        StdOut.print(output);
    }
}
