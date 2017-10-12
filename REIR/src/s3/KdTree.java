package s3;

import java.util.Arrays;

import edu.princeton.cs.algs4.*;

public class KdTree {
    private static class Node {
        private Point2D p;
        private Node ld;
        private Node ru;

        public Node(Point2D pnt) {
            p = pnt;
            ld = null;
            ru = null;
        }

        public boolean isGreaterThan(Point2D p, boolean vertical) {
            return (vertical && p.y() < this.p.y()) || (!vertical && p.x() < this.p.x());
        }
    }

    private Node root;
    private int size;

    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point p to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (isEmpty()) {
            size++;
            root = new Node(p);
        } else {
            insert(root, p, false);
        }
    }

    private void insert(Node n, Point2D p, boolean vertical) {
        if (p.equals(n.p)) {
            return; //we beat computer science
        }

        if (n.isGreaterThan(p, vertical)) {
            if (n.ld == null) {
                size++;
                n.ld = new Node(p);
            } else {
                insert(n.ld, p, !vertical);
            }
        } else {
            if (n.ru == null) {
                size++;
                n.ru = new Node(p);
            } else {
                insert(n.ru, p, !vertical);
            }
        }
    }

    // does the set contain the point p?
    public boolean contains(Point2D p) {
        Node n = root;
        boolean vertical = false;

        while (n != null) {
            if (p.equals(n.p)) {
                return true;
            }

            n = n.isGreaterThan(p, vertical) ? n.ld : n.ru;
            vertical = !vertical;
        }

        return false;
    }

    // draw all of the points to standard draw
    public void draw() {
        draw(root, new RectHV(0, 0, 1, 1), false);
    }

    private void draw(Node n, RectHV rect, boolean vertical) {
        if (n == null) {
            return;
        }
        if (vertical) {
            draw(n.ld, new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), n.p.y()), !vertical);
            draw(n.ru, new RectHV(rect.xmin(), n.p.y(), rect.xmax(), rect.ymax()), !vertical);
            // draw a horizontal line for the node
            StdDraw.setPenRadius();
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(rect.xmin(), n.p.y(), rect.xmax(), n.p.y());
        } else {
            draw(n.ld, new RectHV(rect.xmin(), rect.ymin(), n.p.x(), rect.ymax()), !vertical);
            draw(n.ru, new RectHV(n.p.x(), rect.ymin(), rect.xmax(), rect.ymax()), !vertical);
            // draw a vertical line for the node
            StdDraw.setPenRadius();
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(n.p.x(), rect.ymin(), n.p.x(), rect.ymax());
        }
        StdDraw.setPenRadius(.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.point(n.p.x(), n.p.y());
    }

    // all points in the set that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        Bag<Point2D> bag = new Bag<Point2D>();
        range(root, rect, bag, false);
        return bag;
    }

    private void range(Node n, RectHV rect, Bag<Point2D> bag, boolean vertical) {
        if (n == null) {
            return;
        }
        if ((vertical && n.p.y() >= rect.ymin()) || (!vertical && n.p.x() >= rect.xmin())) {
            range(n.ld, rect, bag, !vertical);
        }
        if (rect.contains(n.p)) {
            bag.add(n.p);
        }
        if ((vertical && n.p.y() <= rect.ymax()) || (!vertical && n.p.x() <= rect.xmax())) {
            range(n.ru, rect, bag, !vertical);
        }
    }

    // a nearest neighbor in the set to p; null if set is empty
    public Point2D nearest(Point2D p) {
        lcount = 0;
        count = 0;
        return isEmpty() ? null : nearest(root, p, false);
    }

    public int lcount = 0;
    public int count = 0;

    private Point2D nearest(Node n, Point2D p, boolean vertical) {
        lcount++;
        if(n == null) {
            return new Point2D(Double.MAX_VALUE, Double.MAX_VALUE);
        }
        count++;
        boolean ld = n.isGreaterThan(p, vertical);
        Point2D split = new Point2D(vertical ? p.x() : n.p.x(), vertical ? n.p.y() : p.y());
        Point2D best_child = nearest(ld ? n.ld : n.ru, p, !vertical);

        if(p.distanceSquaredTo(split) < p.distanceSquaredTo(best_child)) {
            Point2D temp = nearest(ld ? n.ru : n.ld, p, !vertical);
            best_child = p.distanceSquaredTo(temp) < p.distanceSquaredTo(best_child) ? temp : best_child;
        }

        return p.distanceSquaredTo(best_child) < p.distanceSquaredTo(n.p) ? best_child : n.p;
    }

    /*******************************************************************************
     * Test client
     ******************************************************************************/
    public static void main(String[] args) {

//        for (int i = 0; i <= 24; i++) {
//            KdTree ktree = new KdTree();
//            Stopwatch s = new Stopwatch();
//            for (int j = 0; j < 1<<i; j++) {
//                ktree.insert(new Point2D(StdRandom.uniform(), StdRandom.uniform()));
//            }
//            StdOut.println("$2^{" + Integer.toString(i) + "}$ & " + Double.toString(s.elapsedTime()) + " \\\\");
//        }

        KdTree impl = new KdTree();

        String filename = args[0];
        In in = new In(filename);
        while (!in.isEmpty()) {
            impl.insert(new Point2D(in.readDouble(), in.readDouble()));
        }

        filename = args[1];
        in = new In(filename);
        int iterations = 0;
        Bag<Point2D> bag = new Bag<>();
        while (!in.isEmpty()) {
            bag.add(new Point2D(in.readDouble(), in.readDouble()));
            iterations++;
        }

        StdOut.println("Structure initialized");

        Stopwatch s = new Stopwatch();

        int[] counts = new int[bag.size()];
        int i = 0;
        for (Point2D p : bag) {
            impl.nearest(p);
            counts[i++] = impl.count;
        }
        StdOut.print("The time for " + Integer.toString(iterations) + " iterations was: ");
        StdOut.println(Double.toString(s.elapsedTime()) + " s");
        StdOut.println("The recursive iterations were:");
        StdOut.println("mean:" + Double.toString(StdStats.mean(counts)));

        StdOut.println("max:" + Double.toString(StdStats.max(counts)));
        StdOut.println("min:" + Double.toString(StdStats.min(counts)));
        StdOut.println("stddev:" + Double.toString(StdStats.stddev(counts)));
        StdOut.println("var:" + Double.toString(StdStats.var(counts)));

//        In in = new In();
//        Out out = new Out();
//        int N = in.readInt(), C = in.readInt(), T = 50;
//        Point2D[] queries = new Point2D[C];
//        KdTree tree = new KdTree();
//        out.printf("Inserting %d points into tree\n", N);
//        for (int i = 0; i < N; i++) {
//            tree.insert(new Point2D(in.readDouble(), in.readDouble()));
//        }
//        out.printf("tree.size(): %d\n", tree.size());
//        out.printf("Testing `nearest` method, querying %d points\n", C);
//
//        for (int i = 0; i < C; i++) {
//            queries[i] = new Point2D(in.readDouble(), in.readDouble());
//            out.printf("%s: %s\n", queries[i], tree.nearest(queries[i]));
//        }
//        for (int i = 0; i < T; i++) {
//            for (int j = 0; j < C; j++) {
//                tree.nearest(queries[j]);
//            }
//        }

//        In in = new In();
//        Out out = new Out();
//        int nrOfRecangles = in.readInt();
//        int nrOfPointsCont = in.readInt();
//        int nrOfPointsNear = in.readInt();
//        RectHV[] rectangles = new RectHV[nrOfRecangles];
//        Point2D[] pointsCont = new Point2D[nrOfPointsCont];
//        Point2D[] pointsNear = new Point2D[nrOfPointsNear];
//        for (int i = 0; i < nrOfRecangles; i++) {
//            rectangles[i] = new RectHV(in.readDouble(), in.readDouble(),
//                    in.readDouble(), in.readDouble());
//        }
//        for (int i = 0; i < nrOfPointsCont; i++) {
//            pointsCont[i] = new Point2D(in.readDouble(), in.readDouble());
//        }
//        for (int i = 0; i < nrOfPointsNear; i++) {
//            pointsNear[i] = new Point2D(in.readDouble(), in.readDouble());
//        }
//        KdTree set = new KdTree();
//        for (int i = 0; !in.isEmpty(); i++) {
//            double x = in.readDouble(), y = in.readDouble([0.01,0.025,0.079,0.165,0.479,1.052,2.193,4.665,3.393,7.113]);
//            set.insert(new Point2D(x, y));
//        }
//        for (int i = 0; i < nrOfRecangles; i++) {
//            // Query on rectangle i, sort the result, and print
//            Iterable<Point2D> ptset = set.range(rectangles[i]);
//            int ptcount = 0;
//            for (Point2D p : ptset)
//                ptcount++;
//            Point2D[] ptarr = new Point2D[ptcount];
//            int j = 0;
//            for (Point2D p : ptset) {
//                ptarr[j] = p;
//                j++;
//            }
//            Arrays.sort(ptarr);
//            out.println("Inside rectangle " + (i + 1) + ":");
//            for (j = 0; j < ptcount; j++)
//                out.println(ptarr[j]);
//        }
//        out.println("Contain test:");
//        for (int i = 0; i < nrOfPointsCont; i++) {
//            out.println((i + 1) + ": " + set.contains(pointsCont[i]));
//        }
//
//        out.println("Nearest test:");
//        for (int i = 0; i < nrOfPointsNear; i++) {
//            out.println((i + 1) + ": " + set.nearest(pointsNear[i]));
//        }
//
//        out.println();
    }
}
