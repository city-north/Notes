package cn.eccto.study.java.io;


import java.io.*;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/05/11 09:54
 */
public class BioTest {
    public static void main(String[] args) throws Exception {
        String filePath = "fileTest.txt";
//        writeFile(filePath);
        readFile(filePath);
        readFileByBufferReader(filePath);
    }


    public static void writeFile(String filePath) {
        File file = new File(filePath);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            String context = "this is a test file";
            fileOutputStream.write(context.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static void readFile(String filePath) throws Exception {
        FileInputStream in = null;
        try {
            byte[] tempbytes = new byte[100];
            int byteread = 0;
            in = new FileInputStream(filePath);
            // 读入多个字节到字节数组中，byteread为一次读入的字节数
            while ((byteread = in.read(tempbytes)) != -1) {
                System.out.write(tempbytes, 0, byteread);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void readFileByBufferReader(String filePath) {
        FileInputStream input = null;
        try {
            input = new FileInputStream(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            final String value = reader.readLine();
            System.out.println(value);
        } catch (Exception e) {
        }
    }
}
