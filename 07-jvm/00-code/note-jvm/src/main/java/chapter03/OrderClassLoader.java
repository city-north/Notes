package chapter03;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen
 */
public class OrderClassLoader extends ClassLoader {
    private String fileName;

    public OrderClassLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        //首先检查类是否已经被加载
        final Class<?> clazz = findClass(name);
        if (clazz == null) {
            System.out.println("加载类失败 className = " + name);
            return super.loadClass(name, resolve);
        }
        return clazz;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        final Class<?> clazz = this.findLoadedClass(name);
        if (null == clazz) {
            try {
                String classFile = getClassFile(name);
                FileInputStream fileInputStream = new FileInputStream(classFile);
                final FileChannel fileC = fileInputStream.getChannel();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                WritableByteChannel outC = Channels.newChannel(baos);
                final ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
                while (true) {
                    final int i = fileC.read(buffer);
                    if (i == 0 || i == -1) {
                        break;
                    }
                    buffer.flip();
                    outC.write(buffer);
                    buffer.clear();
                }
                fileInputStream.close();
                final byte[] bytes = baos.toByteArray();
                return defineClass(name, bytes, 0, bytes.length);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return clazz;

    }

    /**
     * 获取Class文件路径
     *
     * @param name Class名称
     * @return
     */
    private String getClassFile(String name) {
        return fileName + name + ".class";

    }

    public static void main(String[] args) throws ClassNotFoundException {
        OrderClassLoader orderClassLoader = new OrderClassLoader("/Users/ec/study/jvm/");
        final Class<?> person = orderClassLoader.loadClass("Person");
        System.out.println(person.getClassLoader());

        System.out.println("---Class loader tree ===");
        final ClassLoader classLoader = person.getClassLoader();
        while (classLoader != null) {
            System.out.println(classLoader);
            final ClassLoader parent = classLoader.getParent();
        }
    }
}
