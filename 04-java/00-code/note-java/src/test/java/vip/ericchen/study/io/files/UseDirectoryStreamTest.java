package vip.ericchen.study.io.files;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * <p>
 * description
 * </p>
 *
 * @author qiang.chen04@hand-china.com 2020/12/26 20:31
 */
public class UseDirectoryStreamTest {
    public static final String PATH = "F:\\Notes\\04-java\\00-code\\note-java\\src\\test";
    public static final String PATH_TO_DEL = "F:\\Notes\\04-java\\00-code\\note-java\\src\\test\\mysqy";

    @Test
    public void listAllDirectory() throws IOException {
        try(DirectoryStream<Path> paths = Files.newDirectoryStream(Paths.get(PATH),"*.java")){
            for (Path path : paths) {
                System.out.println(path);
            }
        }
    }

    @Test
    public void deleteDirectoryTree() throws IOException {
        Files.walkFileTree(Paths.get(PATH_TO_DEL),new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (exc != null) throw exc;
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }


    @Test
    public void visitFileListener() throws IOException {

        Files.walkFileTree(Paths.get("/"),new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                System.out.println("访问path之前的调用"+dir);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println("访问path"+file);
                return super.visitFile(file, attrs);
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                System.out.println("访问path失败"+file);
                return super.visitFileFailed(file, exc);
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                System.out.println("访问path失败之后"+dir);
                return super.postVisitDirectory(dir, exc);
            }
        });
    }
}
