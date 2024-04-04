import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Huffman {
    public HashMap<Character, String> getCodeTree(String str) {

        HashMap<Character, String> charCode ;

        ArrayList<HuffmanTreeNode> charRate = new ArrayList<>();
        for (int i = 0; i < str.length(); i++) {
            boolean flag = true;
            for (int i1 = 0; i1 < charRate.size(); i1++) {

                if (charRate.get(i1).getStr().charAt(0) == (str.charAt(i))) {
                    flag = false;
                    charRate.get(i1).addAmount();
                }
            }
            if (flag) {
                charRate.add(new HuffmanTreeNode("" + str.charAt(i), 1));
            }
        }
        Collections.sort(charRate);
        charCode = getCharCode(charRate);
       //  canonicalHuffmanCodesForCharCode(charCode);
        return charCode;

    }

    public HashMap<Character, String> getCharCode(ArrayList<HuffmanTreeNode> charRate) {
        while (charRate.size() > 1) {
            HuffmanTreeNode newNode = new HuffmanTreeNode(charRate.get(0), charRate.get(1));
            charRate.remove(0);
            charRate.remove(0);
            for (int i = 0; i < charRate.size(); i++) {
                if (charRate.get(i).getAmount() > newNode.getAmount()) {
                    charRate.add(i, newNode);
                    break;
                }
                if (charRate.size() - 1 == i) {
                    charRate.add(newNode);
                    break;
                }
            }
            if (charRate.size() == 0) {
                charRate.add(newNode);
            }

        }
        HashMap<Character, String> charCode = new HashMap<>();
        codeFromTree(charRate.get(0), charCode, "");
        return charCode;
    }

    public void codeFromTree(HuffmanTreeNode tree, HashMap<Character, String> charCode, String str) {
        if (tree != null) {
            if (tree.left == null && tree.right == null) {
                charCode.put(tree.getStr().charAt(0), str);
            } else {
                codeFromTree(tree.left, charCode, str + "0");
                codeFromTree(tree.right, charCode, str + "1");
            }
        }
    }

    public HashMap<String, Character> canonicalHuffmanCodesForCharCode(HashMap<Character, String> charCode) {
        ArrayList<HuffmanTreeNode> CodesLengthArray = new ArrayList<>();
        for (char value : charCode.keySet()) {
            CodesLengthArray.add(new HuffmanTreeNode(String.valueOf(value), (charCode.get(value).length())));
        }
      return   canonicalHuffman(CodesLengthArray);
    }

    public HashMap<String, Character> canonicalHuffman(ArrayList<HuffmanTreeNode> charLength) {
        Collections.sort(charLength);
        HashMap<String, Character> charCode = new HashMap<>();
        StringBuilder code = new StringBuilder("0");
        for (HuffmanTreeNode huffmanTreeNode : charLength) {
            while (code.length() < huffmanTreeNode.getAmount()) {
                code.append("0");
            }
            charCode.put(code.toString(), huffmanTreeNode.getStr().charAt(0));

            add(code);
        }
        //  System.out.println(charCode);
        return charCode;

    }

    public void add(StringBuilder a) {
        int n = a.length() - 1;
        while (a.charAt(n) == '1' && n > 0) {
            a.setCharAt(n, '0');
            n--;
        }
        a.setCharAt(n, '1');

    }

    public void codingInFile(String str, String Path) {
        HashMap<Character, String> charCode = getCodeCanonicForStr(str);

        StringBuilder output = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            output.append(charCode.get(str.charAt(i)));
        }
        try {
            FileWriter writer = new FileWriter(Path, false);
            //запись алфавита
            writer.append((char) charCode.size());
            writer.flush();
            for (Map.Entry<Character, String> entry : charCode.entrySet()) {
                writer.append(entry.getKey());
                writer.flush();
                writer.append((char) entry.getValue().length());
                writer.flush();
            }

            int i = 0;
            while (i + 8 <= output.length()) {
                int myChar = 0;
                for (int j = 0; j < 8; j++, i++) {
                    myChar = myChar << 1;
                    myChar += output.charAt(i) == '1' ? 1 : 0;
                }

                writer.append((char) myChar);
                writer.flush();
            }
            int myChar = 0;

            for (; i < output.length(); i++) {
                myChar = myChar << 1;
                myChar += output.charAt(i) == '1' ? 1 : 0;
            }

            writer.append((char) myChar);
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
   }

    public HashMap<Character, String> getCodeCanonicForStr(String str) {
        HashMap<Character, String> charCode =getCodeTree(str);
        ArrayList<HuffmanTreeNode> charLength = new ArrayList<>();
        for (Character character : charCode.keySet()) {
            int ch = character;
            int num = charCode.get(character).length();
            charLength.add(new HuffmanTreeNode("" + (char) ch, num));
        }
        HashMap<String,Character > charCodeCanon = canonicalHuffman(charLength);
        charCode=new HashMap<>();
        for (String s : charCodeCanon.keySet()) {
            charCode.put(charCodeCanon.get(s),s);
        }
        return  (charCode);
    }

    public String decoding(String Path) {
        try {
            FileReader reader = new FileReader(Path);
            int c = reader.read();
            ArrayList<HuffmanTreeNode> charLength = new ArrayList<>();
            for (int i = 0; i < c; i++) {
                int ch = reader.read();
                int num = reader.read();
                charLength.add(new HuffmanTreeNode("" + (char) ch, num));
            }
            HashMap<String, Character> charCode = canonicalHuffman(charLength);

            StringBuilder strBinarDeCompress = new StringBuilder();
            StringBuilder strDeCompress = new StringBuilder();
            int last=0;
            while ((c = reader.read()) != -1) {
                strBinarDeCompress.append(String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0'));
                last=c;
            }
            strBinarDeCompress.delete(strBinarDeCompress.length()-8,strBinarDeCompress.length());

            strBinarDeCompress.append(Integer.toBinaryString(last));
            StringBuilder code = new StringBuilder();
             for (int i = 0; i < strBinarDeCompress.length(); i++) {
                code.append(strBinarDeCompress.charAt(i));

                if (charCode.containsKey(code.toString())) {
                    strDeCompress.append(charCode.get(code.toString()));
                    code = new StringBuilder();
                }
            }
            return strDeCompress.toString();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
