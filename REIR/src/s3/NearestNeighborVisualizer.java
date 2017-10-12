package s3;
/******************************************************************************
 *  Compilation:  javac NearestNeighborVisualizer.java
 *  Execution:    java NearestNeighborVisualizer input.txt
 *  Dependencies: PointST.java KdTreeST.java
 *
 *  Read points from a file (specified as a command-line argument) and
 *  draw to standard draw. Highlight the closest point to the mouse.
 *
 *  The nearest neighbor according to the brute-force algorithm is drawn
 *  in red; the nearest neighbor using the kd-tree algorithm is drawn in blue.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.*;

public class NearestNeighborVisualizer {

    public final static int resolution = 2048;

    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);

        StdDraw.show(0);
        StdDraw.setCanvasSize(resolution, resolution);

        // initialize the two data structures with point from standard input
        //PointSET brute = new PointSET();
        KdTree kdtree = new KdTree();
        for (int i = 0; !in.isEmpty(); i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            //brute.insert(p);
        }
        RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
        boolean justpressed = true;


        double granularity = resolution / 1;
        LinearProbingHashST<Point2D, Bag<Point2D>> dict = new LinearProbingHashST<>();
        for (int i = 0; i < granularity; ++i) {
            for (int j = 0; j < granularity; ++j) {
                Point2D p = new Point2D(i / granularity, j / granularity);
                Point2D nearest = kdtree.nearest(p);
                if (!dict.contains(nearest)) {
                    dict.put(nearest, new Bag<>());
                }
                dict.get(nearest).add(p);
            }
        }
//        Bag<Point2D> bag = new Bag<>();
//        for (int i = 0; i < granularity; ++i) {
//            for (int j = 0; j < granularity; ++j) {
//                Point2D p = new Point2D(i / granularity, j / granularity);
//                if (brute.nearest(p) != kdtree.nearest(p)) {
//                    bag.add(p);
//                    StdOut.println(p);
//                }
//            }
//        }
        int randombro = StdRandom.uniform(Integer.MAX_VALUE);
        //StdOut.print("number of inequalities: " + Integer.toString(bag.size()));
        while (true) {

            if (StdDraw.isMousePressed() && justpressed) {
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
                //StdOut.printf("%8.6f %8.6f\n", x, y);
                Point2D p = new Point2D(x, y);
                if (rect.contains(p)) {
                    kdtree.insert(p);
                    //brute.insert(p);
                }
                dict = new LinearProbingHashST<>();
                for (int i = 0; i < granularity; ++i) {
                    for (int j = 0; j < granularity; ++j) {
                        Point2D pnt = new Point2D(i / granularity, j / granularity);
                        Point2D nearest = kdtree.nearest(p);
                        if (!dict.contains(nearest)) {
                            dict.put(nearest, new Bag<>());
                        }
                        dict.get(nearest).add(pnt);
                    }
                }
//                bag = new Bag<>();
//                for (int i = 0; i < granularity; ++i) {
//                    for (int j = 0; j < granularity; ++j) {
//                        Point2D pnt = new Point2D(i / granularity, j / granularity);
//                        if (brute.nearest(p) != kdtree.nearest(p)) {
//                            bag.add(pnt);
//                        }
//                    }
//                }
                justpressed = false;
            } else if (!StdDraw.isMousePressed()) {
                justpressed = true;
            }


            // the location (x, y) of the mouse
            double x = StdDraw.mouseX();
            double y = StdDraw.mouseY();
            Point2D query = new Point2D(x, y);

            // draw all of the points
            StdDraw.clear();

            if (StdDraw.isKeyPressed(java.awt.event.KeyEvent.getExtendedKeyCodeForChar('b'))) {
                StdDraw.setPenColor(StdDraw.MAGENTA);
//                for (Point2D p : bag) {
//                    StdDraw.point(p.x(), p.y());
//                }
            } else {
                // MAKE A PARTY
                StdDraw.setPenRadius(4 / granularity);
                for (Point2D megapoint : dict.keys()) {
                    StdRandom.setSeed(megapoint.hashCode() + randombro);
                    StdDraw.setPenColor(StdRandom.uniform(256), StdRandom.uniform(256), StdRandom.uniform(256));
                    for (Point2D p : dict.get(megapoint)) {
                        StdDraw.point(p.x(), p.y());
                    }
                }
                StdDraw.setPenColor(StdDraw.MAGENTA);
//                for (Point2D p : bag) {
//                    StdDraw.point(p.x(), p.y());
//                }


                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.setPenRadius(.01);
                kdtree.draw();
                // for (Point2D p : brute.points())  p.draw();

                //StdDraw.point(StdDraw.mouseX(), StdDraw.mouseY());

                //Point2D brt = brute.nearest(query);
                Point2D kdt = kdtree.nearest(query);

                // draw in red the nearest neighbor according to the brute-force algorithm
                StdDraw.setPenRadius(.03);
                StdDraw.setPenColor(StdDraw.RED);
                //brt.draw();
                StdDraw.setPenRadius(.02);

                // draw in blue the nearest neighbor according to the kd-tree algorithm
                StdDraw.setPenColor(StdDraw.BLUE);
                kdt.draw();
            }
            StdDraw.show(0);
            StdDraw.show(40);
        }
    }
}
