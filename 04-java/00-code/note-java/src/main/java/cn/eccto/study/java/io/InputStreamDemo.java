package cn.eccto.study.java.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/12/18 20:28
 */
public class InputStreamDemo {

    public static final String FILE_PATH = "/Users/ec/study/Notes/04-java/00-code/note-java/fileTest.txt";

    public static void main(String[] args) {
        readByFileInputStream(FILE_PATH);
    }

    private static void readByFileInputStream(String filePath) {
        try (InputStream in = new FileInputStream(Paths.get(filePath).toFile())) {
            byte[] buffer = new byte[2];
            int len;
            while ((len = in.read(buffer)) > 0) {
                System.out.println(Arrays.toString(buffer));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
