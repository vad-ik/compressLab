import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Stats {
    public static HashMap<Character, Integer> getChareRate(String str) {
        HashMap<Character, Integer> charRate = new HashMap<>();
        for (int i = 0; i < str.length(); i++) {
            if (charRate.containsKey(str.charAt(i))) {
                charRate.put(str.charAt(i), charRate.get(str.charAt(i)) + 1);
            } else {
                charRate.put(str.charAt(i), 1);
            }
        }
        charRate.put((char) 0, charRate.size() + 1);
        return charRate;
    }

    public static HashMap<Character, Double> getChareRateDauble(String str) {
        HashMap<Character, Double> charRate = new HashMap<>();
        for (int i = 0; i < str.length(); i++) {
            if (charRate.containsKey(str.charAt(i))) {
                charRate.put(str.charAt(i), charRate.get(str.charAt(i)) + 1);
            } else {
                charRate.put(str.charAt(i), 1.0);
            }
        }
        for (Character character : charRate.keySet()) {
            charRate.put(character, charRate.get(character) * 1.0 / str.length());
        }
        return charRate;
    }

    public static ArrayList<Character> getAlphabet(String str) {
        ArrayList<Character> alphabet = new ArrayList<>();
        for (int i = 0; i < str.length(); i++) {
            if (!alphabet.contains(str.charAt(i))) {
                alphabet.add(str.charAt(i));
            }
        }
        Collections.sort(alphabet);
        return alphabet;
    }

    public static void entropi(HashMap<Character, Double> charRate, ArrayList<Character> alphabet) {
        double sum = 0;
        System.out.println(charRate);
        for (Character character : alphabet) {
            double p = charRate.get(character);
            sum -= p * Math.log(p) / Math.log(2);
        }
        System.out.println("энтропия " + sum);
    }

    public static void entropiForString(String str) {
        ArrayList<Character> alphabet = getAlphabet(str);
        entropi(getChareRateDauble(str), alphabet);
    }

    public static void rleEffect(String str) {
        int CountOrder = 0;
        int LenOrder = 0;
        boolean Order = false;
        for (int i = 0; i < str.length() - 1; i++) {
            if (str.charAt(i) == str.charAt(i + 1)) {
                if (!Order) {
                    CountOrder++;
                    LenOrder++ ;
                    Order=true;
                }
                LenOrder++ ;
            }else {
                Order = false;
            }
        }
        System.out.println("rleEffect");
        System.out.println(LenOrder);
        System.out.println(CountOrder);
        System.out.println(str.length());
        System.out.println((LenOrder-2*CountOrder)*1.0/str.length());

    }
}
