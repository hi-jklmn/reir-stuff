package s4;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinearProbingHashST;

import java.io.File;

public class WordNet {

    SAP hypernyms;
    LinearProbingHashST<String, Integer> synsets = new LinearProbingHashST<>();

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        In in = new In(synsets);

        int S = 0;

        while(in.hasNextLine()) {
            S++;
            String[] curr = in.readLine().split(",");
            String[] syns = curr[1].split(" ");

            for(int i = 0; i < syns.length; i++) {
                this.synsets.put(syns[i], Integer.valueOf(curr[0]));
            }
        }

        Digraph G = new Digraph(S);

        in = new In(hypernyms);

        while(in.hasNextLine()) {
            String[] curr = in.readLine().split(",");

            int v = Integer.valueOf(curr[0]);

            for(int i = 1; i < curr.length; i++) {
                G.addEdge(v, Integer.valueOf(curr[i]));
            }
        }

        this.hypernyms = new SAP(G);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return new Bag<String>();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return false;
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        return 1;
    }

    // a synset (second field of synsets.txt) that is a shortest common ancestor
// of nounA and nounB
    public String sap(String nounA, String nounB) {
        return "PHALLUSE";
    }

    // do unit testing of this class
    public static void main(String[] args) {
    }
}
