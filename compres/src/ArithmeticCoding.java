import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ArithmeticCoding {
    int scale = 0;
    char endCHR = (char) 65535;

    char endCHRM = (char) 65534;

    public StringBuilder bigDecCompress(String str) {

        str+=(endCHR);//end
        ArrayList<Character> alphabet = Stats.getAlphabet(str.toString());
        HashMap<Character, Double> charRate = Stats.getChareRateDauble(str.toString());
        BigDecimal length = new BigDecimal(1);
        BigDecimal min = new BigDecimal(0);
        BigDecimal max = new BigDecimal(1);

        for (int i = 0; i < str.length(); i++) {

            for (Character character : alphabet) {
                if (str.charAt(i) == character) {
                    max = min.add(length.multiply(new BigDecimal(charRate.get(character))));
                    length = max.subtract(min);

                    break;
                } else {

                    min = min.add(length.multiply(new BigDecimal(charRate.get(character))));

                }
            }
        }
        String minStr = min.toString();
        String maxStr = max.toString();
        StringBuilder outInt = new StringBuilder();

        outInt.append((char) charRate.size());
        for (Character character : charRate.keySet()) {
            outInt.append(character);
            long lng = Double.doubleToLongBits(charRate.get(character));
            for (int i = 0; i < 4; i++) {
                int chr = (int) lng & (32767);
                lng >>= 15;
                outInt.append((char) chr);
            }
            outInt.append((char) lng);
        }
//        StringBuilder num = new StringBuilder();
//        int i = 0;
//        for (i = 2; i < minStr.length(); i++) {
//            if (minStr.charAt(i) == maxStr.charAt(i)) {
//                num.append(minStr.charAt(i));
//            } else {
//
//                System.out.println(i);
//                break;//все что дальше нам не нужно
//            }
//        }
//
//            for (int j = 0; j < 9109 && i < minStr.length(); j++, i++) {
//                num.append(minStr.charAt(i));
//            }
        int end=minStr.length()-1;
        while (minStr.charAt(end)=='0'){
            end--;
        }
            outInt.append((minStr.substring(2,end)));

      //  outInt.append(toStr(new BigInteger(minStr.substring(2,end))));
            // System.out.println("compressEnd");

       return outInt.append(endCHRM);
    }

    public StringBuilder toStr(BigInteger str) {
        StringBuilder output = new StringBuilder();//из числа в бинарное
        while (str.compareTo(BigInteger.ZERO) > 0) {
            output.append(str.remainder(BigInteger.TWO).toString());
            str = str.divide(BigInteger.TWO);
        }
        output.reverse();
        StringBuilder writer = new StringBuilder();

      //  System.out.println(output);
        int i = 0;
        while (i + 15 <= output.length()) {
            int myChar = 0;
            for (int j = 0; j < 15; j++, i++) {
                myChar = myChar << 1;
                myChar += output.charAt(i) == '1' ? 1 : 0;
            }
            writer.append((char) myChar);
        }
        int myChar = 0;

        for (; i < output.length(); i++) {
            myChar = myChar << 1;
            myChar += output.charAt(i) == '1' ? 1 : 0;
        }
        if (myChar > 0) {
            writer.append((char) myChar);
        }

     //   System.out.println(myChar);
        return writer;
    }

    public BigDecimal toBigInt(String str) {
        StringBuilder binary = new StringBuilder();
        int last = 0;
        for (int i = 0; i < str.length(); i++) {
            int c = str.charAt(i);
            binary.append(String.format("%15s", Integer.toBinaryString(c)).replace(' ', '0'));
            last = c;
        }
        binary.delete(binary.length() - 15, binary.length());
        binary.append(Integer.toBinaryString(last));
      //  System.out.println(Integer.toBinaryString(last));
      //  System.out.println((last));

      //  System.out.println(binary);
        BigInteger num = BigInteger.ZERO;

        BigInteger tmp = BigInteger.ONE;
        System.out.println(binary.length());
        for (int i = 0; i < binary.length(); i++) {
            System.out.println(i);
            if (binary.charAt(i) == '1') {
                tmp = BigInteger.ONE;
                for (int j = 0; j < binary.length() - 1 - i; j++) {
                    tmp = tmp.multiply(BigInteger.TWO);
                }
                num = num.add(tmp);
            }
        }

       //  System.out.println(num);
        return new BigDecimal("0." + num.toString());
    }

    public StringBuilder bigDecDecompress(String str) {

        ArrayList<Character> alphabet = new ArrayList<>();
        HashMap<Character, Double> charRate = new HashMap<>();
        int lenAlp = str.charAt(0);
        for (int i = 0; i < lenAlp; i++) {
            alphabet.add(str.charAt(i * 6 + 1));
            long lng = 0L;
            for (int j = 5; j > 0; j--) {
                lng <<= 15;
                lng += str.charAt(i * 6 + 1 + j);
            }
            charRate.put(str.charAt(i * 6 + 1), Double.longBitsToDouble(lng));
        }
        Collections.sort(alphabet);
      //  System.out.println(charRate);
      //   return bigDecDecompress(toBigInt( str.substring(lenAlp * 6 + 1)), alphabet, charRate);
        return bigDecDecompress(new BigDecimal("0." + str.substring(lenAlp * 6 + 1)), alphabet, charRate);
    }

    public StringBuilder bigDecDecompress(BigDecimal num, ArrayList<Character> alphabet, HashMap<Character, Double> charRate) {

        BigDecimal length = new BigDecimal(1);
        length.setScale(scale);
        BigDecimal min = new BigDecimal(0);
        min.setScale(scale);
        BigDecimal max = new BigDecimal(1);
        max.setScale(scale);

        StringBuilder str = new StringBuilder();

        boolean flag = true;
      // System.out.println(num);
        while (flag) {
            int i = 0;
            while (!((min.add(length.multiply(new BigDecimal(charRate.get(alphabet.get(i)))))).compareTo(num) > 0)) {
                min = min.add(length.multiply(new BigDecimal(charRate.get(alphabet.get(i)))));
               // System.out.println(min);
                i++;
            }
            max = min.add(length.multiply(new BigDecimal(charRate.get(alphabet.get(i)))));
            length = max.subtract(min);
            if (alphabet.get(i) == endCHR) {
                flag = false;
            } else {
                str.append(alphabet.get(i));
            }
        }
        System.out.print(str);
        return str;
    }


    public StringBuilder compress(StringBuilder str) {


        ArrayList<Character> alphabet = Stats.getAlphabet(str.toString());
        HashMap<Character, Integer> charRate = Stats.getChareRate(str.toString());


        alphabet.add('\n');
        charRate.put('\n', 1);
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
            double min = compress(str.substring(i, Math.min(size, i + 10)), alphabet, charRate, minChar, false, null);
            long lng = Double.doubleToLongBits(min);
            for (int j = 0; j < 8; j++) out.append((char) ((lng >> ((7 - j) * 8)) & 0xff));
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

        str += "\n";
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
                    System.out.println("переполнение double i=" + i + " " + minChar);   //|
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
            while ((min + length * charRate.get(alphabet.get(i)) * 1.0 / charRate.get((char) 0)) <= num) {
                min += length * charRate.get(alphabet.get(i)) * 1.0 / charRate.get((char) 0);
                i++;
            }
            max = min + length * charRate.get(alphabet.get(i)) * 1.0 / charRate.get((char) 0);
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
