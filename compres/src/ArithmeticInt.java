import java.util.ArrayList;
import java.util.Collections;

public class ArithmeticInt {
    int _low_edge = 0;
    int _high_edge = 65535;
    char EOF_symbol = '-';
    char symbol_break = '\0';


    int first_qtr = _high_edge / 4 + 1;

    int half = 2 * first_qtr;

    int third_qtr = 3 * first_qtr;

    StringBuilder compress(StringBuilder str) {
        ArrayList<Character> abc = get_abc(str);
        int[] freq = get_frequency(str, abc);
        StringBuilder outBit = encode_text(str, abc, freq);
        return outBit;
    }

    StringBuilder intToChar(StringBuilder outBit) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < outBit.length(); i += 8) {
            int a = 0;
            for (int j = 0; j < 8; j++) {
                if (i + j == outBit.length()) {
                    break;
                }
                a += outBit.charAt(i + j) == '1' ? 1 : 0;
                a = a << 1;
            }
            str.append((char) a);
        }
        return str;
    }
    StringBuilder intToChar(String outBit) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < outBit.length(); i += 8) {
            int a = 0;
            for (int j = 0; j < 8; j++) {
                if (i + j == outBit.length()) {
                    break;
                }
                a += outBit.charAt(i + j) == '1' ? 1 : 0;
                a = a << 1;
            }
            str.append((char) a);
        }
        return str;
    }
    void show_table(String text, String abc, int[] freq) {
        System.out.println(text);
        System.out.println(abc);
        for (int i = 0; i < abc.length(); i++) {
            System.out.println(abc.charAt(i) + " - " + freq[i]);
        }
        System.out.println();
    }

    ArrayList<Character> get_abc(StringBuilder text) {
        ArrayList<Character> alphabet = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            if (!alphabet.contains(text.charAt(i))) {
                alphabet.add(text.charAt(i));
            }
            Collections.sort(alphabet);
        }
        return alphabet;
    }

    /* Получение массива частоты использования символов */
    int[] get_frequency(StringBuilder text, ArrayList<Character> abc) {
        int[] freq = new int[abc.size()];
        for (int i = 0; i < abc.size(); i++) {
            freq[i] = 0;
        }
        for (int j = 0; j < abc.size(); j++) {
            for (int i = 0; i < text.length(); i++) {
                if (abc.get(j) == text.charAt(i)) {
                    freq[j]++;
                }
            }
            if (j > 0) {
                freq[j] += freq[j - 1];
            }
        }
        return freq;
    }

    /* Возвращаем номер символа в алфавите */
    int get_next_symbol(int i, StringBuilder text, ArrayList<Character> abc) {
        int current = 0;
        /* Индекс символа из текста в алфавите */
        boolean exit = false;
        for (; i < text.length() && !exit; i++) {
            char temp_text = text.charAt(i);
            for (int j = 0; j < abc.size() && !exit; j++) {
                char temp_abc = abc.get(j);
                if (temp_text == temp_abc) {
                    current = j;
                    exit = true;

                }
            }
        }
        return current;
    }

    /* Запись битов */
    void write_bits(boolean bit, int bits_to_foll, StringBuilder temp) {

        temp.append((bit ? 1 : 0));
        while (bits_to_foll > 0) {
            temp.append((!bit ? 1 : 0));
            bits_to_foll -= 1;
        }
    }


    StringBuilder encode_text(StringBuilder text, ArrayList<Character> abc, int[] freq) {
        int mass_size = text.length();
        System.out.println(mass_size);
        int[] _low = new int[mass_size];
        int[] _high = new int[mass_size];
        _low[0] = _low_edge;
        _high[0] = _high_edge;
        int current = 1;    /* Какой элемент берём */
        int i = 0;            /* Где находимся */
        int range = 0;
        /* Del - последняя накопленная частота */
        int del = freq[abc.size() - 1];
        int bits_to_foll = 0;
        StringBuilder code = new StringBuilder();
        StringBuilder encode = new StringBuilder();

        while (i < text.length()) {
            current = get_next_symbol(i, text, abc);
            i += 1;

                System.out.println(i);

            range = _high[i - 1] - _low[i - 1] + 1;
            _low[i] = _low[i - 1] + (range * freq[current - 1]) / del;
            _high[i] = _low[i - 1] + (range * freq[current]) / del - 1;


            for (; ; ) {
                if (_high[i] < half) {
                    write_bits(false, bits_to_foll, code);
                    bits_to_foll = 0;
                } else if (_low[i] >= half) {
                    write_bits(true, bits_to_foll, code);
                    bits_to_foll = 0;
                    _low[i] -= half;
                    _high[i] -= half;
                } else if (_low[i] >= first_qtr && _high[i] < third_qtr) {
                    bits_to_foll += 1;
                    _low[i] -= first_qtr;
                    _high[i] -= first_qtr;
                } else break;
                _low[i] = 2 * _low[i];
                _high[i] = 2 * _high[i] + 1;

                if (code.length() >= 8 * 64) {
                    encode.append(intToChar(code.substring(0, 8 * 64-1)));
                    code = new StringBuilder(code.substring( 8 * 64-1,code.length()-1));
                }
            }
        }

        encode.append(intToChar(code));
        return encode;
    }

    int to_int(int _pos, StringBuilder encode) {
        int n = 0;
        for (int i = _pos; i < 32 + _pos; i++) {
            n <<= 1;
            n |= encode.charAt(i) - '0';
        }
        return n;
    }
//
//       StringBuilder to_bits_16(int _value) {
//            Byte < 16 > a(_value); //convent number into bit array
//            string mystring = a.to_string < char,char_traits<char>,allocator<char> >();
//            return mystring;
//        }
//
//        int add_bit(int value, int count_taken ) {
//            boolean flag = false;
//            /* Создаем битсет объекты */
//            bitset < 16 > a(value);
//
//            /* Проверяем первый бит в буффере (если 1)*/
//            if (flag == 1) {
//                a.reset(0);
//            } else if (count_taken >= encode.length()) {
//                a.set(0);
//                flag = 1;
//            } else if (encode[count_taken] == '1') {
//                a.set(0);
//            } else if (encode[count_taken] == '0') {
//                a.reset(0);
//            }
//            value = (unsigned short int)(a.to_ulong());
//            return value;
//        }
//
//    StringBuilder decode_text(StringBuilder text, ArrayList<Character> abc,int[] freq) {
//            StringBuilder decode_text =new StringBuilder();
//            int mass_size = text.length();
//            int[] _low = new  int[mass_size];
//            int[] _high = new  int[mass_size];
//            _low[0] = _low_edge;
//            _high[0] = _high_edge;
//
//             int range = 0;
//             int cum = 0;
//            int del = freq[abc.size() - 1];
//
//              int value = to_int(0,text);        // Забираем 16 бит в value
//            int count_taken = 16;
//
//
//
//            for (int i = 1; ; i++) {
//                range = (_high[i - 1] - _low[i - 1]) + 1;
//                cum = (((value - _low[i - 1]) + 1) * del - 1) / range;
//
//                int symbol;
//                for (symbol = 1; freq[symbol] <= cum; symbol++) ; //Другой знак
//
//                _low[i] = _low[i - 1] + (range * freq[symbol - 1]) / del;
//                _high[i] = _low[i - 1] + (range * freq[symbol]) / del - 1;
//
//                decode_text .append( abc.get(symbol));
//
//
//                if (abc.get(symbol) == symbol_break) {
//                    break;
//                }
//
//                for (; ; ) {
//                    if (_high[i] >= half) {
//                        if (_low[i] >= half) {
//                            value -= half;
//                            _low[i] -= half;
//                            _high[i] -= half;
//                        } else if (_low[i] >= first_qtr && _high[i] < third_qtr) {
//                            value -= first_qtr;
//                            _low[i] -= first_qtr;
//                            _high[i] -= first_qtr;
//                        } else {
//                            break;
//                        }
//                    }
//
//                    _low[i] = 2 * _low[i];
//                    _high[i] = 2 * _high[i] + 1;
//                    value = add_bit(2 * value, count_taken, flag);
//                    count_taken++;
//                }
//            }
//        return decode_text;
//
//    }
}
