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

    private double linewidth = 0.002;
    private double pointradius = 0.01;

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
        if (n.p == p) {
            return;
        }

        if(n.isGreaterThan(p, vertical)) {
            if(n.ld == null) {
                size++;
                n.ld = new Node(p);
            } else {
                insert(n.ld, p, !vertical);
            }
        } else {
            if(n.ru == null) {
                size++;
                n.ru = new Node(p);
            } else {
                insert(n.ru, p, !vertical);
            }
        }
    }

    // does the set contain the point p?
    public boolean contains(Point2D p) {
        return p == nearest(p);
    }

    // draw all of the points to standard draw
    public void draw() {
        if (!isEmpty()) {
            draw(root, new RectHV(0,0,1,1), false);
        }
    }

    private void draw(Node n, RectHV rect, boolean vertical) {
        if (n == null) {
            return;
        }
        StdDraw.setPenRadius(linewidth);
        if (vertical) {
            // draw a horizontal line for the node
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(rect.xmin(), n.p.y(), rect.xmax(), n.p.y());
        } else {
            // draw a vertical line for the node
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(n.p.x(), rect.ymin(), n.p.x(), rect.ymax());
        }
        StdDraw.setPenRadius(pointradius);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.point(n.p.x(), n.p.y());
        if (vertical) {
            draw(n.ld, new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), n.p.y()), !vertical);
            draw(n.ru, new RectHV(rect.xmin(), n.p.y(), rect.xmax(), rect.ymax()), !vertical);
        } else {
            draw(n.ld, new RectHV(rect.xmin(), rect.ymin(), n.p.x(), rect.ymax()), !vertical);
            draw(n.ru, new RectHV(n.p.x(), rect.ymin(), rect.xmax(), rect.ymax()), !vertical);
        }
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
        if ((vertical && n.p.y() > rect.ymin()) || (!vertical && n.p.x() > rect.xmin())) {
            range(n.ld, rect, bag, !vertical);
        }
        if (rect.contains(n.p)) {
            bag.add(n.p);
        }
        if ((vertical && n.p.y() < rect.ymax()) || (!vertical && n.p.x() < rect.xmax())) {
            range(n.ru, rect, bag, !vertical);
        }
    }

    // a nearest neighbor in the set to p; null if set is empty
    public Point2D nearest(Point2D p) {
        return isEmpty() ? null : nearest(root, p, false);
    }

    private Point2D nearest(Node n, Point2D p, boolean vertical) {

        Node next = n.ru;
        boolean leftdown = false;

        if(n.isGreaterThan(p, vertical)) {
            next = n.ld;
            leftdown = true;
        }

        Point2D out = n.p;

        if(next != null) {
            out = nearest(next, p, !vertical);
        }

        if(p.distanceSquaredTo(n.p) <= p.distanceSquaredTo(out)) {
            next = leftdown ? n.ru : n.ld;
            if(next != null) {
                out = nearest(next, p, !vertical);
                return p.distanceSquaredTo(n.p) < p.distanceSquaredTo(out) ? n.p : out;
            }
        }

        return out;
    }

    /*******************************************************************************
     * Test client
     ******************************************************************************/
    public static void main(String[] args) {
        In in = new In();
        Out out = new Out();
        int nrOfRecangles = in.readInt();
        int nrOfPointsCont = in.readInt();
        int nrOfPointsNear = in.readInt();
        RectHV[] rectangles = new RectHV[nrOfRecangles];
        Point2D[] pointsCont = new Point2D[nrOfPointsCont];
        Point2D[] pointsNear = new Point2D[nrOfPointsNear];
        for (int i = 0; i < nrOfRecangles; i++) {
            rectangles[i] = new RectHV(in.readDouble(), in.readDouble(),
                    in.readDouble(), in.readDouble());
        }
        for (int i = 0; i < nrOfPointsCont; i++) {
            pointsCont[i] = new Point2D(in.readDouble(), in.readDouble());
        }
        for (int i = 0; i < nrOfPointsNear; i++) {
            pointsNear[i] = new Point2D(in.readDouble(), in.readDouble());
        }
        KdTree set = new KdTree();
        for (int i = 0; !in.isEmpty(); i++) {
            double x = in.readDouble(), y = in.readDouble();
            set.insert(new Point2D(x, y));
        }
        for (int i = 0; i < nrOfRecangles; i++) {
            // Query on rectangle i, sort the result, and print
            Iterable<Point2D> ptset = set.range(rectangles[i]);
            int ptcount = 0;
            for (Point2D p : ptset)
                ptcount++;
            Point2D[] ptarr = new Point2D[ptcount];
            int j = 0;
            for (Point2D p : ptset) {
                ptarr[j] = p;
                j++;
            }
            Arrays.sort(ptarr);
            out.println("Inside rectangle " + (i + 1) + ":");
            for (j = 0; j < ptcount; j++)
                out.println(ptarr[j]);
        }
        out.println("Contain test:");
        for (int i = 0; i < nrOfPointsCont; i++) {
            out.println((i + 1) + ": " + set.contains(pointsCont[i]));
        }

        out.println("Nearest test:");
        for (int i = 0; i < nrOfPointsNear; i++) {
            out.println((i + 1) + ": " + set.nearest(pointsNear[i]));
        }

        out.println();
    }
}
