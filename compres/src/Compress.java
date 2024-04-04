import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Compress {

    static String noCompressionPath = "noCompression.txt";
    static String deCompressionPath = "deCompression.txt";
    static String compressionPath = "Compression.txt";

    public  StringBuilder avtoCompress(StringBuilder str){


            Boolean oneChar = false;
            StringBuilder writer = new StringBuilder();


            int amount = 1;
            for (int i = 0; i < str.length() - 1; i++) {

                if (str.charAt(i) == str.charAt(i+1) && amount < 255) {
                    amount++;
                } else {

                        if (amount == 1) {
                            if (oneChar) {
                                writer.append((char) str.charAt(i));
                            } else {
                                writer.append((char) 0);
                                writer.append((char) str.charAt(i));
                                oneChar = true;
                            }

                        } else {
                            if (oneChar) {
                                oneChar = false;
                                writer.append((char) 0);
                            }
                            writer.append((char) amount);
                            writer.append((char) str.charAt(i));
                        }

                    amount = 1;

                }
            }

             {
                if (amount == 1) {
                    if (oneChar) {
                        writer.append((char) str.charAt(str.length()-1));
                    } else {

                        writer.append((char) 0);
                        writer.append((char) str.charAt(str.length()-1));
                        oneChar = true;
                    }

                } else {
                    if (oneChar) {
                        writer.append((char) 0);
                    }
                    writer.append((char) amount);
                    writer.append((char) str.charAt(str.length()-1));
                }

            }
      return writer;
    }
    public static void preparationForCompression(Scanner scanner) {
        ArrayList<Integer> result = new ArrayList<>();
        String strInput;
        String strPath;
        do {

            System.out.println("выберите объект сжатия, 'текст' или 'изображение'");
            strInput = scanner.nextLine();
        } while (!Objects.equals(strInput, "текст") && !Objects.equals(strInput, "изображение"));

        System.out.println("введите путь до объекта");

        strPath = scanner.nextLine();

        if (Objects.equals(strInput, "изображение")) {
            BufferedImage in;

            try {
                in = ImageIO.read(new File(strPath));
                ImageIO.write(in, "png", new File("imageOriginal.bmp"));

                int height = in.getHeight();
                int width = in.getWidth();

                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        result.add(in.getRGB(j, i));
                    }
                }
            } catch (IOException e) {
                System.out.println("ошибка файла");
                throw new RuntimeException(e);
            }
        } else {

            try {
                Scanner scannerFile = new Scanner(new File(strPath));
                while (scannerFile.hasNext()) {
                    String input = scannerFile.nextLine();
                    for (int i = 0; i < input.length(); i++) {
                        result.add((int) input.charAt(i));
                    }
                    result.add((int) '\n');
                }
                scannerFile.close();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
        int[] arr = new int[result.size()];

        for (int i = 0; i < result.size(); i++) {
            arr[i] = result.get(i);
        }
        Compression(arr, Objects.equals(strInput, "изображение"));
    }

    public static void Compression(int[] data, boolean foto) {
        try {

            Boolean oneChar = false;
            FileWriter writer = new FileWriter(compressionPath, false);
            FileWriter writer2 = new FileWriter(noCompressionPath, false);


            int amount = 1;
            for (int i = 0; i < data.length - 1; i++) {
                if (foto) {
                    for (int j = 0; j <4 ; j++) {
                        writer2.write((char) ByteBuffer.allocate(4).putInt(data[i]).array()[j] + 256);
                    }

                } else {
                    writer2.write((char) data[i]);
                }
                writer2.flush();

                if (data[i] == data[i + 1] && amount < 255) {
                    amount++;
                } else {
                    if (foto) {
                        writer.write((char) amount);
                        for (int j = 0; j <4 ; j++) {
                            writer.write((char) ByteBuffer.allocate(4).putInt(data[i]).array()[j] + 256);
                        }
                    } else {
                        if (amount == 1) {
                            if (oneChar) {
                                writer.write((char) data[i]);
                            } else {
                                writer.write((char) 0);
                                writer.write((char) data[i]);
                                oneChar = true;
                            }

                        } else {
                            if (oneChar) {
                                oneChar = false;
                                writer.write((char) 0);
                                writer.flush();
                            }
                            writer.write((char) amount);
                            writer.write((char) data[i]);
                        }
                    }
                    amount = 1;

                    writer.flush();
                }
            }

            if (foto) {
                writer.write((char) amount);
                writer.write((char) ByteBuffer.allocate(4).putInt(data[data.length - 1]).array()[0] + 256);
                writer.write((char) ByteBuffer.allocate(4).putInt(data[data.length - 1]).array()[1] + 256);
                writer.write((char) ByteBuffer.allocate(4).putInt(data[data.length - 1]).array()[2] + 256);
                writer.write((char) ByteBuffer.allocate(4).putInt(data[data.length - 1]).array()[3] + 256);
            } else {
                if (amount == 1) {
                    if (oneChar) {
                        writer.write((char) data[data.length - 1]);
                    } else {

                        writer.write((char) 0);
                        writer.write((char) data[data.length - 1]);
                        oneChar = true;
                    }

                } else {
                    if (oneChar) {
                        writer.write((char) 0);
                    }
                    writer.write((char) amount);
                    writer.write((char) data[data.length - 1]);
                }

            }
            writer.close();
            writer2.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void deCompression(Scanner scanner) {
        try {
            Boolean oneChar = false;
            FileWriter writer = new FileWriter(deCompressionPath, false);
            FileReader reader = new FileReader(compressionPath);
            String strInput;
            do {
                System.out.println("сжатый файл это фото? 'да' или 'нет'");
                strInput = scanner.nextLine();
            } while (!Objects.equals(strInput, "да") && !Objects.equals(strInput, "нет"));
            boolean foto = Objects.equals(strInput, "да");
            BufferedImage imgResult = null;
            int width = 0;
            int height;
            int numPixel = 0;
            if (foto) {
                System.out.println(" пожалуйста введите размеры изображения");
                width = scanner.nextInt();
                height = scanner.nextInt();
                imgResult = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
            }
            String str = null;
            int c;


            while ((c = reader.read()) != -1) {
                int n = c;

                int[] myByte = new int[4];
                if (foto) {
                    for (int i = 0; i < 4; i++) {
                        c = reader.read();
                        myByte[i] = c % 256;

                    }
                } else {
                    if (n == 0) {
                        if (oneChar) {
                            oneChar = false;
                            n = reader.read();

                        } else {
                            oneChar = true;
                            c = reader.read();

                        }
                    }
                    if (oneChar) {
                        myByte[3] = c;
                        n = 1;
                    } else {
                        myByte[3] = reader.read();


                    }
                }
                for (int i = 0; i < n % 256; i++) {
                    if (foto) {

                        writer.write((char) (myByte)[0]);
                        writer.write((char) (myByte)[1]);
                        writer.write((char) (myByte)[2]);
                        writer.write((char) (myByte)[3]);
                        imgResult.setRGB(numPixel % width, numPixel / width,
                                ((myByte[0] & 0xFF) << 24) + ((myByte[1] & 0xFF) << 16) + ((myByte[2] & 0xFF) << 8) + (myByte[3] & 0xFF));
                        numPixel++;

                    } else {
                        writer.write((char) myByte[3]);

                        writer.flush();

                    }
                }

            }
            if (foto) {
                ImageIO.write(imgResult, "png", new File("image.bmp"));
            }

            writer.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
