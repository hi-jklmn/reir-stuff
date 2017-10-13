package s4;

import edu.princeton.cs.algs4.*;
import org.omg.CORBA.MARSHAL;

public class SAP {
    private final Digraph G;
    private int root;
    private final boolean ANCESTOR = true;
    private final boolean LENGTH = false;
    private static final int OFFSET = 1;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        //BreadthFirstDirectedPaths bfs = new BreadthFirstDirectedPaths(G, 0);
        this.G = G;
        DirectedCycle c = new DirectedCycle(G);
        if (new DirectedCycle(G).hasCycle()) {
            throw new IllegalArgumentException("Graph is not acyclic");
        }
        boolean hasRoot = false;
        for (int i = 0; i < G.V(); i++) {
            if (!G.adj(i).iterator().hasNext()) {
                if (hasRoot) {
                    throw new IllegalArgumentException("Graph is not rooted");
                }
                hasRoot = true;
                this.root = i;
            }
        }
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return iterize(LENGTH, v, w);
    }

    // a shortest common common ancestor of v and w; -1 if no such path
    public int ancestor(int v, int w) {
        return iterize(ANCESTOR, v, w);
    }

    // length of shortest ancestral path of vertex subsets A and B; -1 if no such path
    public int length(Iterable<Integer> A, Iterable<Integer> B) {
        return iterbfs(LENGTH, A, B);
    }

    // a shortest common ancestor of vertex subsets A and B; -1 if no such path
    public int ancestor(Iterable<Integer> A, Iterable<Integer> B) {
        return iterbfs(ANCESTOR, A, B);
    }

    private int iterize(boolean ancestor, int v, int w) {
        Bag<Integer> va = new Bag<>();
        Bag<Integer> wa = new Bag<>();
        va.add(v); // This is why Java has no friends
        wa.add(w);
        return iterbfs(ancestor, va, wa);
    }

    private int bfs(int limit, Queue<Integer> queue, int[] mine, int[] other) {
        int candidate = -1;
        if (!queue.isEmpty()) {
            int v = queue.dequeue();
            int depth = mine[v] + 1;
            if (depth > limit) {
                return candidate;
            }
            if (other[v] > 0) {
                candidate = v;
            }
            for (Integer vertex : G.adj(v)) {
                if (mine[vertex] == 0 || mine[vertex] > depth) {
                    mine[vertex] = depth;
                    if (other[vertex] > 0) {
                        // || mine[candidate] + other[candidate] == mine[vertex] + other[vertex]
                        if (candidate == -1 || mine[candidate] + other[candidate] > mine[vertex] + other[vertex]) {
                            candidate = vertex;
                        } else if (mine[candidate] + other[candidate] == mine[vertex] + other[vertex]) {
                            candidate = Math.max(candidate, vertex);
                        }
                    } else {
                        queue.enqueue(vertex);
                    }
                }
            }
        }
        return candidate;
    }

    private int iterbfs(boolean ancestor, Iterable<Integer> A, Iterable<Integer> B) {
        Queue<Integer> queuev = new Queue<>();
        Queue<Integer> queuew = new Queue<>();
        int[] lenv = new int[G.V()];
        int[] lenw = new int[G.V()];
        for (Integer vertex : A) {
            lenv[vertex] = OFFSET;
            queuev.enqueue(vertex);
        }
        for (Integer vertex : B) {
            lenw[vertex] = OFFSET;
            queuew.enqueue(vertex);
        }
        int limit = Integer.MAX_VALUE;
        int candidate = -1;
        while (!queuev.isEmpty() || !queuew.isEmpty()) {
            int dv = bfs(limit, queuev, lenv, lenw);
            int dw = bfs(limit, queuew, lenw, lenv);
            candidate = Math.max(candidate, Math.max(dv, dw));
            if (candidate != -1) {
                if (!ancestor) {
                    return lenw[candidate] + lenv[candidate] - 2 * OFFSET;
                } else {
                    limit = Math.min(limit, lenw[candidate] + lenv[candidate] - OFFSET);
                }
            }
        }
        return candidate;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        StdOut.println(G);

        Out out = new Out("output1.dot");

        //Print a dot representation for visualizer
        out.println("digraph g {\n\tnode[shape = circle]");
        for (int v = 0; v < G.V(); v++) {
            for (int w : G.adj(v)) {
                out.print(String.format("\t%d", v));
                out.print(String.format(" -> %d;\n", w));
            }
        }
        out.println("}");
        SAP sap = new SAP(G);

        out = new Out();
        StdRandom.setSeed(198237);

        for (int i = 0; i < G.V(); i += 1) {
            for (int j = i; j < G.V(); j += 1) {
                out.print("i: ");
                out.print(i);
                out.print(",\t j: ");
                out.print(j);
                out.print(",\t a: ");
                out.print(sap.ancestor(i, j));
                out.print(",\t l: ");
                out.println(sap.length(i, j));
            }
            //StdOut.println(i);
        }
    }
}
