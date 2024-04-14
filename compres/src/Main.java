
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;


public class Main {

    static String strInput;

    public static void main(String[] args) {

        Long start = System.currentTimeMillis();
        StringBuilder str8 = new StringBuilder();
        StringBuilder str7 = new StringBuilder();
        String path8 = "C:\\Users\\DarkCat\\Desktop\\отчёты Хорошков Вадим\\2 курс\\4 сем\\алгоритмы\\lab_1_compress\\enwik8";
      //  path8 = "C:\\Users\\DarkCat\\Desktop\\отчёты Хорошков Вадим\\2 курс\\4 сем\\алгоритмы\\lab1\\imageOriginal.bmp";
        String path7 = "C:\\Users\\DarkCat\\Desktop\\отчёты Хорошков Вадим\\2 курс\\4 сем\\алгоритмы\\lab_1_compress\\enwik7.txt";
        // path7 = "C:\\Users\\DarkCat\\Desktop\\отчёты Хорошков Вадим\\2 курс\\4 сем\\алгоритмы\\lab1\\1648085697_1-kartinkin-net-p-milie-kartinki-chb-1.jpg";

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
        RLE rle = new RLE();
        LZ77 lz77 = new LZ77();
        BWTFast burrowsWheeler = new BWTFast();
        MTF mtf = new MTF();
        ArithmeticCoding arithmetic = new ArithmeticCoding();
        Burrows_Wheeler bwt = new Burrows_Wheeler();

//       str8= getFoto(path8);
//        str7= getFoto(path7);
//
//        toFile(rle.compressionFotoToStr(str8),"rleFoto8");
//        toFile(rle.compressionFotoToStr(str7),"rleFoto7");
//
//        System.out.println("rleEnd");
//
//
//        toFile(rle.avtoCompress(burrowsWheeler.getBWT(String.valueOf(str8))),"BWTrleFoto8vv");
//        toFile(rle.avtoCompress(burrowsWheeler.getBWT(String.valueOf(str7))),"BWTrleFoto7vv");
//
//        System.out.println("BWTrleEnd");
        StringBuilder Out7=lz77.compress(String.valueOf(str7));
        StringBuilder Out8=lz77.compress(String.valueOf(str8));
        toFile(Out8,"lz8");
        toFile(Out7,"lz7");
        System.out.println("lzEnd");
        huffman.codingInFile(String.valueOf(Out7),"lz7Ha");
        huffman.codingInFile(String.valueOf(Out8),"lz8Ha");
        System.out.println("haEnd");
//        huffman.codingInFile(String.valueOf(str7),"7Ha");
//        huffman.codingInFile(String.valueOf(str8),"8Ha");
//        System.out.println("haEnd");
//
//        Out7=mtf.compress(String.valueOf(burrowsWheeler.getBWT(String.valueOf(str7))));
//        Out8=mtf.compress(String.valueOf(burrowsWheeler.getBWT(String.valueOf(str8))));
//
//        huffman.codingInFile(String.valueOf(Out7),"bwtMtf7Ha");
//        huffman.codingInFile(String.valueOf(Out8),"bwtMtf8Ha");
//
//
//        huffman.codingInFile(String.valueOf(rle.avtoCompress((Out7))),"bwtMtfRLE7Ha");
//        huffman.codingInFile(String.valueOf(rle.avtoCompress((Out8))),"bwtMtfRLe8Ha");

    }

    static void statsBwt(StringBuilder str8, BWTFast burrowsWheeler, RLE rle) {
        System.out.println("оценка бвт");
        StringBuilder out = new StringBuilder();
        for (int i = 10000000; i <= 10000000; i += 1000000) {

            Long startt = System.currentTimeMillis();
            System.out.print(i + " time: ");
            out = new StringBuilder();
            for (int j = 0; j < str8.length(); j += i) {
                out.append(burrowsWheeler.getBWT((str8).substring(j, Math.min(str8.length(), j + i))));
            }
            out = rle.avtoCompress(out);
            toFile(out, "enwic8TestSizeBWT_RLE" + i);
            System.out.println(System.currentTimeMillis() - startt);
        }
    }

    static void statsLz(StringBuilder str, LZ77 lz77,int start,int step, int finish) {
        System.out.println("оценка LZ77");
        for (int i = start; i <= finish; i += step) {

            Long startt = System.currentTimeMillis();
            System.out.print(i + " ");
            lz77.dictionary = i;

            toFile(lz77.compress(String.valueOf(str)), "lz" + i);

            //  System.out.println(System.currentTimeMillis()-startt);
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

    public static StringBuilder getFoto(String Path) {
        StringBuilder str = new StringBuilder();

        BufferedImage in;

        try {
            in = ImageIO.read(new File(Path));
            ImageIO.write(in, "png", new File("imageOriginal.bmp"));

            int height = in.getHeight();
            int width = in.getWidth();
            str.append((char) (width));
            str.append((char) (height));

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int color = in.getRGB(j, i);
                    for (int k = 0; k < 4; k++) {
                        str.append((char) (color % 256));
                        color >>= 8;
                    }

                }
            }
        } catch (IOException e) {
            System.out.println("ошибка файла");
            throw new RuntimeException(e);
        }

        return str;
    }

}