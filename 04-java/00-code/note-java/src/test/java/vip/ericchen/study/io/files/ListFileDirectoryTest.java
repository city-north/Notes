package vip.ericchen.study.io.files;


import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * <p>
 * description
 * </p>
 *
 * @author qiang.chen04@hand-china.com 2020/12/26 20:09
 */
public class ListFileDirectoryTest {
    public static final String PATH = "F:\\Notes\\04-java\\00-code\\note-java\\src\\main";
    public static final String TARGET = "F:\\Notes\\04-java\\00-code\\note-java\\src\\main2";

    @Test
    public void listAllFileEntriesWithFilter() throws IOException {
        Stream<Path> pathStream = Files.find(Paths.get(PATH), 2, (t, u) -> {
            if ("src".equalsIgnoreCase(t.getFileName().toString())) {
                return true;
            }
            return false;
        });
        pathStream.forEach(System.out::println);
    }

    @Test
    public void copyDirectory() throws Exception {
        Path path = Paths.get(PATH);
        Path target = Paths.get(TARGET);
        Files.walk(path).forEach(p->{
            try{
                //解析
                Path relativize = path.relativize(p);//相对路径
                Path resolve = target.resolve(relativize);
                if (Files.isDirectory(resolve)){
                    Files.createDirectory(resolve);
                }else {
                    Files.copy(p,resolve);
                }
            }catch (IOException e){
                throw new UncheckedIOException(e);
            }
        });
    }
}
