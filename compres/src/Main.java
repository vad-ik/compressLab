
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;


public class Main {

    static String strInput;

    public static void main(String[] args) {

        Long start = System.currentTimeMillis();
        StringBuilder str8 = new StringBuilder();
        StringBuilder str7 = new StringBuilder();
        String path8 = "C:\\Users\\DarkCat\\Desktop\\отчёты Хорошков Вадим\\2 курс\\4 сем\\алгоритмы\\1\\compres\\enwik8";
        String path7 = "C:\\Users\\DarkCat\\Desktop\\отчёты Хорошков Вадим\\2 курс\\4 сем\\алгоритмы\\1\\compres\\enwik7.txt";
        try {
            FileReader reader8 = new FileReader(new File(path8));
            FileReader reader7 = new FileReader(new File(path7));
            int c;
            while ((c = reader8.read()) != -1) {
                str8.append((char) c);
            }
            while ((c = reader7.read()) != -1) {
                str7.append((char) c);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("конец чтения");
        System.out.println(System.currentTimeMillis() - start);
        System.out.println();

        Huffman huffman = new Huffman();
        Compress rle = new Compress();
        LZ77 lz77 = new LZ77();
        BWTFast burrowsWheeler = new BWTFast();
        MTF mtf = new MTF();
        ArithmeticInt arithmetic = new ArithmeticInt();


       statsBwt(str8,burrowsWheeler,rle);

//        out=(lz77.compress(String.valueOf(str8)));
//        out2=(lz77.compress(String.valueOf(str7)));
//        System.out.println("конец lz77");
//        System.out.println(System.currentTimeMillis()-start);
//        System.out.println();
//        System.out.println(str8.length());
//        for (int i = 0; i <str8.length() ; i+=10000000) {
//            out.append(burrowsWheeler.getBWT((str8).substring(i,Math.min(str8.length() ,i+10000000))));
//        }
//        out2.append(burrowsWheeler.getBWT(String.valueOf(str7)));
//        System.out.println("конец бвт");
//        System.out.println(System.currentTimeMillis() - start);
//        System.out.println();
//        StringBuilder outBWT=new StringBuilder(out);
//        StringBuilder outBWT2=new StringBuilder(out2);
////
//        outBWT = mtf.compress(outBWT.toString());
//        outBWT2 = mtf.compress(outBWT2.toString());
//        System.out.println("конец мтф");
//        System.out.println(System.currentTimeMillis()-start);
//        System.out.println();
////
////
////
//        out = rle.avtoCompress(out);
//        out2 = rle.avtoCompress(out2);
//        System.out.println("конец рле");
//        System.out.println(System.currentTimeMillis()-start);
//        System.out.println();
////
//
//        StringBuilder outBWT_MTF_RLE=new StringBuilder(outBWT);
//        StringBuilder outBWT_MTF_RLE2=new StringBuilder(outBWT2);
//
//        outBWT_MTF_RLE = rle.avtoCompress(outBWT_MTF_RLE);
//        outBWT_MTF_RLE2 = rle.avtoCompress(outBWT_MTF_RLE2);
//        System.out.println("конец рле");
//        System.out.println(System.currentTimeMillis()-start);
//        System.out.println();
//
//        huffman.codingInFile(String.valueOf(outBWT), "enwic8CompressBWT_MTF_HA");
//        huffman.codingInFile(outBWT2.toString(), "enwic7CompressBWT_MTF_HA");
//        System.out.println("конец хафмана");
//        System.out.println(System.currentTimeMillis()-start);
//        System.out.println();
//
//        huffman.codingInFile(String.valueOf(outBWT_MTF_RLE), "enwic8CompressBWT_MTF_RLE_HA");
//        huffman.codingInFile(outBWT_MTF_RLE2.toString(), "enwic7CompressBWT_MTF_RLE_HA");
//        System.out.println("конец хафмана");
//        System.out.println(System.currentTimeMillis()-start);
//        System.out.println();
//
//        //   out=arithmetic.compress( out);
//        //  out2=arithmetic.compress(new StringBuilder(out2));
//
////        System.out.println("конец AC");
////        System.out.println(System.currentTimeMillis()-start);
////        System.out.println();
//
//        toFile(out,"enwic8CompressBWT_RLE");
//        toFile(out2,"enwic7CompressBWT_RLE");
    }
static void statsBwt(StringBuilder str8, BWTFast burrowsWheeler, Compress rle){
    System.out.println("оценка бвт");
    StringBuilder out = new StringBuilder();
    StringBuilder out2 = new StringBuilder();
    for (int i = 10000; i < 10000000; i+=10000) {

        Long startt = System.currentTimeMillis();
        System.out.print(i+" time: ");
        out = new StringBuilder();
        for (int j = 0; j < str8.length(); j += i) {
            out.append(burrowsWheeler.getBWT((str8).substring(j, Math.min(str8.length(), j + i))));
        }
        out = rle.avtoCompress(out);
        toFile(out, "enwic8TestSizeBWT_RLE" + i);
        System.out.println(System.currentTimeMillis()-startt);
    }
}
    static void toFile(StringBuilder str, String path) {
        try {
            FileWriter writer = new FileWriter(path, false);
            writer.write(str.toString());
            writer.flush();
            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}