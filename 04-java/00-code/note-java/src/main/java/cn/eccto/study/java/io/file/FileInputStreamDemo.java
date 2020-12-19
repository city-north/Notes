package cn.eccto.study.java.io.file;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * <p>
 * use Example for {@link FileInputStream} and {@link FileOutputStream}
 * </p>
 *
 * @author EricChen 2020/12/18 22:32
 */
public class FileInputStreamDemo {

    public static void main(String[] args) throws Exception {
//        copy("/Users/ec/study/Notes/04-java/00-code/note-java/src/main/resources/application.yml", "/Users/ec/study/Notes/04-java/00-code/note-java/src/main/resources/application2.yml");
//        copy2("/Users/ec/study/Notes/04-java/00-code/note-java/src/main/resources/application.yml", "/Users/ec/study/Notes/04-java/00-code/note-java/src/main/resources/application2.yml");
//        copy3("/Users/ec/study/Notes/04-java/00-code/note-java/src/main/resources/application.yml", "/Users/ec/study/Notes/04-java/00-code/note-java/src/main/resources/application2.yml");
//        copy4("/Users/ec/study/Notes/04-java/00-code/note-java/src/main/resources/application.yml", "/Users/ec/study/Notes/04-java/00-code/note-java/src/main/resources/application2.yml");
        copy5("/Users/ec/study/Notes/04-java/00-code/note-java/src/main/resources/application.yml", "/Users/ec/study/Notes/04-java/00-code/note-java/src/main/resources/application2.yml");
    }

    public static void copy(String src, String dest) throws IOException {
        //第一步,创建流
        FileInputStream in = new FileInputStream(src);
        FileOutputStream out = new FileOutputStream(dest);
        //2. 读写数据
        int len;
        while ((len = in.read()) != -1) {
            out.write(len);
        }
        //第三步,关闭流
        in.close();
        out.close();
    }


    public static void copy2(String src, String dest) throws IOException {
        FileInputStream in = new FileInputStream(src);
        FileOutputStream out = new FileOutputStream(dest);

        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }

        in.close();
        out.close();
    }


    public static void copy3(String src, String dest) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(src));
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(dest));


        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) > 0) {
            out.write(len);
            out.flush();
        }
        in.close();
        out.close();
    }


    public static void copy4(String src, String dest) throws IOException {
        final Path srcPath = Paths.get(src);
        final Path toPath = new File(dest).toPath();
        Files.copy(srcPath, toPath, StandardCopyOption.REPLACE_EXISTING);
    }


    public static void copy5(String src, String dest) throws IOException {
        //第一步,创建流
        FileOutputStream out = new FileOutputStream(dest);
        out.write(33);  //
        out.write(258); // 
        out.close();
    }
}
