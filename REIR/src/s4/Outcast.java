package s4;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    WordNet net;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        net = wordnet;
    }

    // given an array of WordNet nouns , return an outcast
    public String outcast(String[] nouns) {

        int[][] lengths = new int[nouns.length][nouns.length];

        for(int i = 0; i < nouns.length; i++) {
            for(int j = i+1; j < nouns.length; j++) {
                lengths[i][j] = net.distance(nouns[i], nouns[j]);
                lengths[j][i] = lengths[i][j];
            }
        }

        int candidate = 0;
        int can_len = 0;

        for(int i = 0; i < nouns.length; i++) {
            int len = 0;
            for(int j = 0; j < nouns.length; j++) {
                len += lengths[i][j];
            }
            if(len > can_len) {
                can_len = len;
                candidate = i;
            }
        }

        return nouns[candidate];
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            String[] nouns = In.readStrings(args[t]);
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
