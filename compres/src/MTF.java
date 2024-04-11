import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class MTF {
    public StringBuilder compress(String str) {
        ArrayList<Character> alphabet = Stats.getAlphabet(str);
        ArrayList<Integer> strOut = new ArrayList<>();

        for (int i = 0; i < str.length(); i++) {
            int j = 0;
            while ( str.charAt(i)!=alphabet.get(j) ) { j++;}
            strOut.add(j);
            alphabet.remove(j);
            alphabet.add(0,str.charAt(i));
        }
       return toStr(alphabet,strOut);
      // onFile(alphabet,strOut);
    }
    public StringBuilder toStr(ArrayList<Character> alphabet, ArrayList<Integer> strOut){
        StringBuilder out=new StringBuilder();
        out.append(((char)alphabet.size()));
        Collections.sort(alphabet);
        for (Character character : alphabet) {
            out.append(character);

        }
        for (int integer : strOut) {
            out.append((char)integer);
        }
        return out;
    }
    public void onFile(ArrayList<Character> alphabet, ArrayList<Integer> strOut){
    try {
        FileWriter writer = new FileWriter("MTF.txt", true);
        writer.write((char)alphabet.size());
        writer.flush();
        for (Character character : alphabet) {
            writer.write(character);
            writer.flush();
        }
        for (int integer : strOut) {
            writer.write((char)integer);
            writer.flush();
        }


        writer.close();
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}

    public void deCompress(String str) {
        ArrayList<Character> alphabet = new ArrayList<>();

        int alpSize= str.charAt(0);
        for (int i = 1; i <alpSize+1 ; i++) {
            alphabet.add(str.charAt(i));
        }
        StringBuilder strOut=new StringBuilder();
        for (int i = alpSize+1; i < str.length(); i++) {
            int n=str.charAt(i);
            strOut.append(alphabet.get(n));
            alphabet.add(0,alphabet.get(n));
            alphabet.remove(n+1);
        }
        System.out.println(strOut);

    }

}
