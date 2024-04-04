import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ArithmeticCodingIntegers {
    int pending_bits = 0;
    StringBuilder outBit;

    public void compress(String str) {
        str += "\n";
        outBit = new StringBuilder();
        ArrayList<Character> alphabet = new ArrayList<>();
        HashMap<Character, Integer> charRate = Stats.getChareRate(str);
        Stats.getAlphabet(str, alphabet);
        int size = str.length();


        compress(str, alphabet, charRate);

    }

    public void compress(String str, ArrayList<Character> alphabet, HashMap<Character, Integer> charRate) {
        long low = 0;
        long high = Long.MAX_VALUE;
        long del =charRate.get((char)0);// 0xffffffffl;
        str+="\n";
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            long range = high - low + 1;
            high = low + (range * upper(c, alphabet, charRate)) / del;
            low = low + (range * lower(c, alphabet, charRate)) / del;
            for (; ; ) {
                if (high < 0x4000000000000000L) {
                    output_bit_plus_pending(false);
                    low <<= 1;
                    high <<= 1;
                    high |= 1;
                } else if (low >= 0x4000000000000000L) {
                    output_bit_plus_pending(true);
                    low <<= 1;
                    high <<= 1;
                    high |= 1;
                } else if (low >= 0x2000000000000000L && high < 0x6000000000000000L) {
                    pending_bits++;
                    low <<= 1;
                    low &= 0x4000000000000000L - 1L;
                    high <<= 1;
                    high |= 0x4000000000000000L + 1L;
                } else {
                    break;
                }
            }
        }

        System.out.println(high);
   decoder(codingToStr(),alphabet,charRate);
    }

    long upper(char c, ArrayList<Character> alphabet, HashMap<Character, Integer> charRate) {
        Long ans = 0l;
        for (Character character : alphabet) {
            ans += charRate.get(character);
            if (character == c) {
                break;
            }
        }
        return ans;
    }

    long lower(char c, ArrayList<Character> alphabet, HashMap<Character, Integer> charRate) {
        Long ans = 0l;
        for (Character character : alphabet) {
            if (character == c) {
                break;
            }
            ans += charRate.get(character);
        }
        return ans;
    }

    void output_bit_plus_pending(boolean bit) {
        char b=0;
        char neB=1;
        if (bit){
            b=1;
            neB=0;
        }
        outBit.append(b);
        while (0 < pending_bits--) {
            outBit.append(neB);
        }
    }

    StringBuilder codingToStr() {
        StringBuilder writer = new StringBuilder();
        int i = 0;
        while (i + 8 <= outBit.length()) {
            int myChar = 0;
            for (int j = 0; j < 8; j++, i++) {
                myChar = myChar << 1;
                myChar += outBit.charAt(i) == '1' ? 1 : 0;
            }

            writer.append((char) myChar);
        }
        int myChar = 0;

        for (; i < outBit.length(); i++) {
            myChar = myChar << 1;
            myChar += outBit.charAt(i) == '1' ? 1 : 0;
        }

        writer.append((char) myChar);
        return writer;
    }

    StringBuilder decoder(StringBuilder str, ArrayList<Character> alphabet, HashMap<Character, Integer> charRate) {
StringBuilder out=new StringBuilder();

        long high = Long.MAX_VALUE;
        long low = 0;
        long value = 0;
        int j = 0;
        for (int i = 0; i < 8; i++) {
            value <<= 8;
            value += str.charAt(j);
            j++;
        }
       while (out.length()==0||out.charAt(out.length()-1)!='\n'){
            long range = high - low + 1;

            int i = 0;
            while ((low + range * charRate.get(alphabet.get(i)) * 1.0 / 0xffffffffl) <= value) {
                low += range * charRate.get(alphabet.get(i)) * 1.0 / 0xffffffffl;
                i++;
            }
            high = (long) (low + range * charRate.get(alphabet.get(i)) * 1.0 / 0xffffffffl);
            out.append(alphabet.get(i));
        }
        System.out.println(out);
        return out;
    }

}