package s4;

import edu.princeton.cs.algs4.*;

public class WordNet {

    private SAP hypernyms;
    private String[] synsets_itos;
    private LinearProbingHashST<String, Bag<Integer>> synsets_stoi = new LinearProbingHashST<>();

    private Digraph G;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {

        Bag<String> synset_read = new Bag<>();
        int S = 0;
        In in = new In(synsets);

        while (in.hasNextLine()) {
            S++;
            synset_read.add(in.readLine());
        }

        synsets_itos = new String[S];

        for (String s : synset_read) {
            String[] curr = s.split(",");
            synsets_itos[Integer.valueOf(curr[0])] = curr[1];
            for (String syn : curr[1].split(" ")) {
                if (!synsets_stoi.contains(syn)) {
                    synsets_stoi.put(syn, new Bag<>());
                }
                synsets_stoi.get(syn).add(Integer.valueOf(curr[0]));
            }
        }

//        Digraph G = new Digraph(S);
        this.G = new Digraph(S);
        in = new In(hypernyms);

        while (in.hasNextLine()) {
            String[] curr = in.readLine().split(",");

            int v = Integer.valueOf(curr[0]);

            for (int i = 1; i < curr.length; i++) {
                G.addEdge(v, Integer.valueOf(curr[i]));
            }
        }

        this.hypernyms = new SAP(G);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return synsets_stoi.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return synsets_stoi.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (isNoun(nounA) && isNoun(nounB)) {
            return hypernyms.length(synsets_stoi.get(nounA), synsets_stoi.get(nounB));
        } else {
            throw new IllegalArgumentException();
        }
    }

    // a synset (second field of synsets.txt) that is a shortest common ancestor
// of nounA and nounB
    public String sap(String nounA, String nounB) {
        if (isNoun(nounA) && isNoun(nounB)) {
            return synsets_itos[hypernyms.ancestor(synsets_stoi.get(nounA), synsets_stoi.get(nounB))];
        } else {
            throw new IllegalArgumentException();
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
//        if (args.length < 2) {
//            StdOut.print("you need to input two files");
//            return;
//        }
//        WordNet wn = new WordNet(args[0], args[1]);
        In in = new In();
        WordNet wn = new WordNet(in.readLine(), in.readLine());
        Out out = new Out("wordnet.dot");
//        Out out = new Out();
        if (false) {
            out.println("digraph wordnet {\n\tnode[shape=box]");
            for (int i = 0; i < wn.synsets_itos.length; i++) {
                out.println("\t" + Integer.toString(i) + "[label = \"" + wn.synsets_itos[i] + "\"]");
            }
            for (int v = 0; v < wn.G.V(); v++) {
                for (int w : wn.G.adj(v)) {
                    out.print(String.format("\t%d", v));
                    out.print(String.format(" -> %d;\n", w));
                }
            }
            out.println("}");
        }
        int queries = Integer.valueOf(in.readLine());
        Queue<String> queue = new Queue<>();
        for (int i = 0; i < queries; i++) {
            queue.enqueue(in.readLine());
        }
        out = new Out();
        //out.println(bag);
        //out = new Out("results.txt");
        for (String a : queue) {
            for (String b : queue) {
//        String a = "science";
//        String b = "Christmas";
                try {
                    String ancestor = wn.sap(a, b);
                    int dist = wn.distance(a, b);
                    out.print("sap(");
                    out.print(a);
                    out.print(", ");
                    out.print(b);
                    out.print(")='");
                    out.print(ancestor);
                    out.print("' len=");
                    out.println(dist);
                } catch (IllegalArgumentException e) {
                }
            }
        }
//        for (int i = 0; i < wn.synsets_itos.length; i += 1) {
//            for (int j = 0; j < wn.synsets_itos.length; j += 1) {
//                out.print("sap(");
//                out.print(wn.synsets_itos[i]);
//                out.print(", ");
//                out.print(wn.synsets_itos[j]);
//                out.print(")='");
//                out.print(wn.synsets_itos[wn.hypernyms.ancestor(i, j)]);
//                out.print("' len=");
//                out.println(wn.hypernyms.length(i, j));
//            }
//            //StdOut.println(i);
//        }
    }
}
