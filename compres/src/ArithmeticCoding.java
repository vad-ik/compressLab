import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

public class ArithmeticCoding {
    public StringBuilder compress(StringBuilder str) {


        ArrayList<Character> alphabet = new ArrayList<>();
        HashMap<Character, Integer> charRate = Stats.getChareRate(str.toString());
        Stats.getAlphabet(str.toString(), alphabet);
        alphabet.add('\n');
        charRate.put('\n',1);
        int size = str.length();

        double minChar = size;

//         для проверки на переполнение double          v
        for (Character character : alphabet) {        //|
            if (charRate.get(character) < minChar) {  //|
                minChar = charRate.get(character);    //|
            }                                         //|
        }                                             //|
        minChar /= size;                              //|
//         для проверки на переполнение double          ^

//            double min = compress(str.toString(), alphabet, charRate, size, minChar, false, null);
//            System.out.println(min);

        StringBuilder out = new StringBuilder();
        for (int i = 0; i < size; i += 11) {
            double min = compress(str.substring(i, Math.min(size,i + 10)), alphabet, charRate, minChar, false, null);
            long lng = Double.doubleToLongBits(min);
            for(int j = 0; j < 8; j++) out.append((char)((lng >> ((7 - j) * 8)) & 0xff));
        }


//        for (int i = 0; i <out.length() ; i+=8) {
//
//            byte[] bytes=new byte[8];
//            out.subSequence(i,Math.min(i+8,out.length()));
//            for (int j = 0; j < 8; j++) {
//                bytes[j]= (byte) out.charAt(j);
//            }
//            deCompress( ByteBuffer.wrap(bytes).getDouble(), alphabet, charRate, size);
//        }
        return out;
        // deCompress(min, alphabet, charRate, size);
    }

    public double compress(String str, ArrayList<Character> alphabet, HashMap<Character, Integer> charRate, double minChar, boolean check, ArrayList<Double> result) {

        str+="\n";
        double length = 1;
        double min = 0;
        double max = 1;
        for (int i = 0; i < str.length(); i++) {
            for (Character character : alphabet) {
                if (str.charAt(i) == character) {
                    max = min + (length * charRate.get(character) * 1.0 / charRate.get((char) 0));
                    length = max - min;

                    break;
                } else {
                    min += length * charRate.get(character) * 1.0 / charRate.get((char) 0);
                }
            }
//         для проверки на переполнение double                            v
            if (check) {
                if (i % 10 == 0) {

                    for (Character character : alphabet) {
                        if ('\n' == character) {
                            max = min + (length * charRate.get(character) * 1.0 / charRate.get((char) 0));
                            length = max - min;
                            break;
                        } else {
                            min += length * charRate.get(character) * 1.0 / charRate.get((char) 0);
                        }
                    }
                    System.out.println(min);
                    System.out.println(alphabet);
                    result.add(min);
                    length = 1;
                    min = 0;
                    max = 1;
                }
            } else {
                if (min + length * minChar == min) {                    //|
                    System.out.println("переполнение double i=" + i+" "+minChar);   //|
                    break;                                              //|
                }
            }
//         для проверки на переполнение double                            ^
        }
        return min;
    }

    public void deCompress(double num, ArrayList<Character> alphabet, HashMap<Character, Integer> charRate, int size) {
        double min = 0;
        double max = 1;

        double length = 1;

        StringBuilder str = new StringBuilder();
        int j = 0;
        boolean flag = true;
        while (flag) {

            int i = 0;
            while ((min + length * charRate.get(alphabet.get(i)) * 1.0 / charRate.get((char)0)) <= num) {
                min += length * charRate.get(alphabet.get(i)) * 1.0 / charRate.get((char)0);
                i++;
            }
            max = min + length * charRate.get(alphabet.get(i)) * 1.0 / charRate.get((char)0);
            length = max - min;
            str.append(alphabet.get(i));
            if (alphabet.get(i) == '\n') {
                flag = false;
            }
            j++;

        }
        System.out.print(str);
    }
}
