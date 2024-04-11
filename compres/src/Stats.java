import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Stats {
    public static HashMap<Character, Integer> getChareRate(String str) {
        HashMap<Character, Integer> charRate = new HashMap<>();
        for (int i = 0; i < str.length(); i++) {
            if (charRate.containsKey(str.charAt(i))) {
                charRate.put(str.charAt(i),charRate.get(str.charAt(i))+1);
            } else {
                charRate.put(str.charAt(i),1);
            }
        }
        charRate.put((char)0,charRate.size()+1);
        return charRate;
    }
    public static HashMap<Character, Double> getChareRateDauble(String str) {
        HashMap<Character, Double> charRate = new HashMap<>();
        for (int i = 0; i < str.length(); i++) {
            if (charRate.containsKey(str.charAt(i))) {
                charRate.put(str.charAt(i),charRate.get(str.charAt(i))+1);
            } else {
                charRate.put(str.charAt(i),1.0);
            }
        }
        for (Character character : charRate.keySet()) {
            charRate.put(character, charRate.get(character)*1.0/str.length());
        }
        return charRate;
    }
    public static ArrayList<Character> getAlphabet(String str) {
        ArrayList<Character>alphabet=new ArrayList<>();
        for (int i = 0; i < str.length(); i++) {
            if (!alphabet.contains(str.charAt(i))) {
                alphabet.add(str.charAt(i));
            }
        }
        Collections.sort(alphabet);
        return alphabet;
    }
    public static void entropi(HashMap<Character, Integer> charRate,ArrayList<Character> alphabet){
        double sum=0;
        for (Character character : alphabet) {
double p= 1.0* charRate.get(character) /charRate.get((char)0);
            sum-=p*Math.log(p)/Math.log(2);
        }
        System.out.println("энтропия "+sum);
    }
    public static void entropiForString(String str){
        ArrayList<Character> alphabet=getAlphabet(str);
        entropi(getChareRate(str),alphabet);
    }
}
