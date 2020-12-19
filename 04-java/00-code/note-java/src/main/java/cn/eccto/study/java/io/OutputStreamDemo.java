package cn.eccto.study.java.io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/12/19 14:47
 */
public class OutputStreamDemo {

    public static void main(String[] args) throws IOException {
        generateCharacters( "/Users/ec/study/Notes/04-java/00-code/note-java/src/main/resources/application2.yml");
    }


    public static void generateCharacters(String filePath) throws IOException {
        FileOutputStream out = new FileOutputStream(filePath);
        int firstPrintableCharacter = 33;
        int numberOfPrintableCharacters = 94;

        for (int i = firstPrintableCharacter; i < numberOfPrintableCharacters + firstPrintableCharacter; i++) {
            out.write(i);
        }
    }
}
