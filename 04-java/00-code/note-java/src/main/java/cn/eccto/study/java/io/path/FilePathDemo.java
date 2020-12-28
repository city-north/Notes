package cn.eccto.study.java.io.path;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * <p>
 * 获取指定目录下的子目录以及文件的代码实例
 * </p>
 *
 * @author qiang.chen04@hand-china.com 2020/12/26 19:54
 */
public class FilePathDemo {
    public static final String PATH = "F:\\Notes\\04-java\\00-code\\note-java";
    public static void main(String[] args) throws Exception {
        listAllEntries(PATH);
//        listAllEntriesAndSubDirectory(PATH);
    }

    /**
     * 列出所有目录以及子目录
     * @param path
     */
    private static void listAllEntriesAndSubDirectory(String path) throws IOException {
        Stream<Path> walk = Files.walk(Paths.get(path));
        walk.forEach(System.out::println);
    }


    /**
     * 测试展示目录下的所有Entry ,不包含子目录
     *
     * @param path 路径
     */
    public static void listAllEntries(String path) throws IOException {
        Stream<Path> list = Files.list(Paths.get(path));
        list.forEach(System.out::println);
    }


}
