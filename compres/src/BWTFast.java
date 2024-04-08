import java.util.Arrays;

public class BWTFast {
    StringBuilder getBWT(String s) {
        s += "$";
        StringBuilder bwt = new StringBuilder();
        int[] suffixArray = compress(s);
        for (int i : suffixArray) {
            int j = i - 1;
            if (j < 0) {
                j += suffixArray.length;
            }
            bwt.append(s.charAt(j));
        }
        return bwt;
    }


    int[] compress(String s){
        int N = s.length();
        int steps = Integer.bitCount(Integer.highestOneBit(N) - 1);
        int[][] rank = new int[steps + 1][N];
        for (int i = 0; i < N; i++) {
            rank[0][i] = s.charAt(i) - 'a';
        }
        Tuple[] tuples = new Tuple[N];
        for (int step = 1, cnt = 1; step <= steps; step++, cnt <<= 1) {
            for (int i = 0; i < N; i++) {
                Tuple tuple = new Tuple();
                tuple.firstHalf = rank[step - 1][i];
                tuple.secondHalf = i + cnt < N ? rank[step - 1][i + cnt] : -1;
                tuple.originalIndex = i;

                tuples[i] = tuple;
            }
            Arrays.sort(tuples);

            rank[step][tuples[0].originalIndex] = 0;

            for (int i = 1, currRank = 0; i < N; i++) {
                if(!tuples[i - 1].firstHalf.equals(tuples[i].firstHalf)
                        || tuples[i - 1].secondHalf.equals(tuples[i].secondHalf)) {
                    ++currRank;
                }
                rank[step][tuples[i].originalIndex] = currRank;
            }


        }

        int[] suffixArray = new int[N];

        for (int i = 0; i < N; i++) {
            suffixArray[i] = tuples[i].originalIndex;
        }
        return suffixArray;
    }

}
