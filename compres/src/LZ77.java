public class LZ77 {
    int dictionary = 32000;

    int st1=0;
    int ln1=0;
    int st2=0;
    int ln2=0;
    public StringBuilder compress(String str) {

        int size = str.length();
        StringBuilder strCompress = new StringBuilder();
        StringBuilder memory = new StringBuilder();
        int charCoded = 0;
        int type = 0;
        for (int i = 0; i < size; i++) {
            int dictionaryStart = 0;
            int dictionaryLeng = 0;
             for (int j = Math.max(0, i - dictionary); j < i ; j++) {
                if (str.charAt(j) == str.charAt(i)) {//есть вхождение
                    int start = j;
                    int tmp = j + 1;
                    int length = 1;
                    while ((i + length) < size && str.charAt(tmp) == str.charAt(i + length))  {
                        tmp++;
                        length++;
                    }
                    if (dictionaryLeng < length) {
                        dictionaryStart = i-start;
                        dictionaryLeng = length;

                    }
                }
            }
            type = type << 1;
            if (dictionaryLeng > 1) {//запись из словаря
                memory.append((char) dictionaryStart);
                memory.append((char) dictionaryLeng);
               type++;
                i+=dictionaryLeng-1;

                if (dictionaryLeng>127){
                     ln2++;
                }else {
                    ln1++;
                }
                if (dictionaryStart>127){
                    st2++;
                }else {
                    st1++;
                }

            } else {//обычная запись
                memory.append(str.charAt(i));
             }
            charCoded++;

            if (charCoded == 8) {
                strCompress.append((char) type);//запишим типы кодов
                charCoded = 0;
                type = 0;
                strCompress.append(memory);
                memory=new StringBuilder();

            }
        }
        if (charCoded != 0) {
            type = type << 8 - charCoded;//чтобы при дешифрации не было особого случая
            strCompress.append((char) type);//запишим типы кодов

            strCompress.append(memory);

        }
        return strCompress;
    }

    public StringBuilder deCompress(StringBuilder str) {
        StringBuilder strOut = new StringBuilder();
        int size = str.length();
        boolean strEnd = false;
        int j = 0;
        while (!strEnd && j < size) {
            int types = str.charAt(j);
            j++;
            for (int i = 7; i >= 0 && !strEnd; i--, j++) {


                if (j < size) {
                    if ((types & (int) Math.pow(2, i)) == 0) {//обычный символ
                        strOut.append(str.charAt(j));
                    } else {
                        int start = str.charAt(j);
                        j++;

                        int length = str.charAt(j);

                        for (int k = 0; k < length; k++) {
                            strOut.append(strOut.charAt(strOut.length() - start));
                        }
                    }

                } else {
                    strEnd = true;
                }
            }
        }
        return strOut;
    }
}
