package vip.Jonathan.study.io.files;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * <p>
 * description
 * </p>
 *
 * @author Jonathan 2021/01/04 13:29
 */
public class PathTest {


    @Test
    public void testResolve() {
        final Path path = Paths.get("/Users/ec/study/Notes/04-java/00-code/note-java/fileTest.txt");
        final Path resolve = path.resolve("/.fileTest.txt");
        System.out.println(resolve);
    }
}
