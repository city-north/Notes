package vip.ericchen.study.io.files;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2021/01/04 13:09
 */
public class FileSystemTest {


    @Test
    public void getDefaultFileSystem() throws IOException {
        final FileSystem defaultFileSystem = FileSystems.getDefault();
        final String separator = defaultFileSystem.getSeparator();
        System.out.println(separator);
        final Path path = defaultFileSystem.getPath("/Users/ec/study/Notes/04-java/00-code/note-java", "fileTest.txt");
        final BufferedReader bufferedReader = Files.newBufferedReader(path);
        final String s = bufferedReader.readLine();
        System.out.println(s);
        System.out.println(path);
        System.out.println(defaultFileSystem);
    }
}
