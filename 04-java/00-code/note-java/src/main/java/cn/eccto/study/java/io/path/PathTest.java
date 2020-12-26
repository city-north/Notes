package cn.eccto.study.java.io.path;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/12/26 15:21
 */
public class PathTest {
    public static void main(String[] args) {
        //绝对路径
        final Path ec = Paths.get("/Users", "ec");
        System.out.println(ec);
        //相对路径
        final Path path = Paths.get("note-java", "pom.xml");
        System.out.println(path);
        final String property = System.getProperty("user.home");
        final Path basePath = Paths.get(property);
        final Path resolve = basePath.resolve("work");
        System.out.println("path1 :" + basePath);
        System.out.println("resolve path :" + resolve);

        Path p = Paths.get("/home/cay");  //   /home/cay
        Path r = Paths.get("/home/fred/myprog");
        Path result = p.relativize(r); //../fred/myprog
        System.out.println(result);
    }
}
