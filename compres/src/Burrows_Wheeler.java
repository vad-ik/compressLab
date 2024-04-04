import java.util.*;

public class Burrows_Wheeler {

    public String compress(String str) {
        ArrayList<String> strArray = new ArrayList<>();

        str += ("$");
        strArray.add(str);
        for (int i = 0; i < str.length() - 1; i++) {//выполняем циклический сдвиг


            StringBuilder newStr = new StringBuilder(strArray.get(i));
            newStr.append(newStr.charAt(0));
            newStr.deleteCharAt(0);
            strArray.add(newStr.toString());
        }
        //  System.out.println(strArray);
        Collections.sort(strArray);//сортировка в лексикографическом порядке
        StringBuilder outStr = new StringBuilder();

        for (String s : strArray) {
            outStr.append(s.charAt(s.length() - 1));
        }
        //  System.out.println("изначальная строка "+str);
        //       efficiency(str);
//
        //    System.out.println("преобразованная строка "+outStr);
        //       efficiency(outStr.toString());
        return outStr.toString();
    }

    public void decompress(String str) {
        ArrayList<StringBuilder> strArray = new ArrayList<StringBuilder>();
        for (int i = 0; i < str.length(); i++) {
            strArray.add(new StringBuilder());
        }
        for (int i = 0; i < str.length(); i++) {
            for (int j = 0; j < str.length(); j++) {
                strArray.get(j).insert(0, str.charAt(j));
            }
            Collections.sort(strArray);

        }
        int i = 0;
        while (i < strArray.size() && strArray.get(i).charAt(strArray.get(i).length() - 1) != '$') {
            i++;
        }
        if (i < strArray.size()) {
            strArray.get(i).deleteCharAt(strArray.get(i).length() - 1);
            System.out.println(strArray.get(i));
        } else {
            System.out.println("ошибка преоброзования");
        }

    }

    public void efficiency(String str) {
        int lenSubsequenceSum = 0;
        int lenSubsequence = 1;
        int numSubsequence = 0;
        for (int i = 0; i < str.length() - 1; i++) {
            if (str.charAt(i) == str.charAt(i + 1)) {
                lenSubsequence++;
            } else {
                lenSubsequenceSum += lenSubsequence == 1 ? 0 : lenSubsequence;
                numSubsequence += lenSubsequence == 1 ? 0 : 1;
                lenSubsequence = 1;
            }
        }
        lenSubsequenceSum += lenSubsequence == 1 ? 0 : lenSubsequence;
        numSubsequence += lenSubsequence == 1 ? 0 : 1;

        System.out.println("средняя длина последовательности повторяющихся символов " + (lenSubsequenceSum * 1.0 / numSubsequence));

        System.out.println("эффективность " + (lenSubsequenceSum - 2 * numSubsequence) * 1.0 / str.length());
    }

    public void decompressEffective(String str) {
        ArrayList<BWTtranspositionObject> map = new ArrayList<>();
        for (int i = 0; i < str.length(); i++) {
            map.add(new BWTtranspositionObject(i, str.charAt(i)));
        }
        Collections.sort(map);
        int n = map.get(0).getNum();
        for (int i = 0; i < map.size() - 1; i++) {
            System.out.print(map.get(n).getCharacter());
            n = map.get(n).getNum();
        }

    }

    public ArrayList<Integer> getSufficsIndex(String str) {
        ArrayList<String> suff = getSuffics(str);
        ArrayList<Integer> suffIndex = new ArrayList<>();
        for (String s : suff) {
            suffIndex.add(str.indexOf(s));
        }
        return suffIndex;
    }

    public ArrayList<String> getSuffics(String str) {
        ArrayList<String> suff = new ArrayList<>();
        for (int i = 0; i < str.length(); i++) {
            suff.add(str.substring(i));
        }
        Collections.sort(suff);
        return suff;
    }

    public StringBuilder suffBWT(ArrayList<Integer> suff, String str) {
        StringBuilder strOut = new StringBuilder();
        for (Integer integer : suff) {
            int i = integer - 1;
            if (i < 0) {
                i = suff.size() - 1;
            }
            strOut.append(str.charAt(i));
        }
        return strOut;
    }
    public StringBuilder getSuffType(String str){
        StringBuilder strOut=new StringBuilder();

        strOut.append('s');
        for (int i =str.length()-1; i >0 ; i--) {
            if (str.charAt(i)>str.charAt(i-1)){
                strOut.append('s');
            }else if (str.charAt(i)<str.charAt(i-1)){
                strOut.append('l');
            }else {
                strOut.append(strOut.charAt(strOut.length()-1));
            }
        }
        return strOut.reverse();

    }
}
