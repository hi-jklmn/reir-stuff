package s4;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinearProbingHashST;

import java.io.File;

public class WordNet {

    SAP hypernyms;
    String[] synsets_itos;
    LinearProbingHashST<String, Integer> synsets_stoi = new LinearProbingHashST<>();

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {

        Bag<String> synset_read = new Bag<>();
        int S = 0;
        In in = new In(synsets);

        while(in.hasNextLine()) {
            S++;
            synset_read.add(in.readLine());
        }

        synsets_itos = new String[S];

        for(String s : synset_read) {
            String[] curr = s.split(",");
            synsets_itos[Integer.valueOf(curr[0])] = curr[1];
            for(String syn : curr[1].split(" ")) {
                synsets_stoi.put(syn, Integer.valueOf(curr[0]));
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
        return synsets_stoi.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return synsets_stoi.contains(word);
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
