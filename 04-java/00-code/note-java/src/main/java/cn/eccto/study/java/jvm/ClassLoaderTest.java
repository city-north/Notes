package cn.eccto.study.java.jvm;

import java.io.InputStream;

/**
 * <p>
 * 不同的类加载器与 instanceof  关键字演示
 * </p>
 *
 * @author Jonathan 2020/07/10 20:27
 */
public class ClassLoaderTest {
    public static void main(String[] args) throws Exception {
        MyClassLoader myClassLoader = new MyClassLoader();
        final Object o = myClassLoader.loadClass("cn.eccto.study.java.jvm.DirectMemoryOOM").newInstance();
        System.out.println(o instanceof DirectMemoryOOM);
    }

}

class MyClassLoader extends ClassLoader {

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        try {

            String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
            InputStream is = getClass().getResourceAsStream(fileName);
            if (is == null) {
                return super.loadClass(name);
            }
            final byte[] bytes = new byte[is.available()];
            is.read(bytes);
            return defineClass(name, bytes, 0, bytes.length);
        } catch (Exception e) {
            throw new ClassNotFoundException(name);
        }
    }
}