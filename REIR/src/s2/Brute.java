package s2;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
import javafx.scene.paint.Stop;

import java.util.Arrays;

public class Brute {
    //et tu brute
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

        Stopwatch timer = new Stopwatch();
        Arrays.sort(points);
        for (int i = 0; i < n - 3; i++) {
            for (int j = i + 1; j < n - 2; j++) {
                for (int k = j + 1; k < n - 1; k++) {
                    for (int l = k + 1; l < n; l++) {
                        double a = points[i].slopeTo(points[j]);
                        double b = points[j].slopeTo(points[k]);
                        double c = points[k].slopeTo(points[l]);
                        if (a == b && b == c) {
                            StdOut.print(points[i].toString() + " -> ");
                            StdOut.print(points[j].toString() + " -> ");
                            StdOut.print(points[k].toString() + " -> ");
                            StdOut.println(points[l].toString());
                        }
                    }
                }
            }
        }
        StdOut.println(timer.elapsedTime());
    }
}
