package cn.eccto.study.java.basic;

import java.io.File;
import java.util.HashMap;
import java.util.Properties;

/**
 * example of SystemProperties
 *
 * @author Jonathan 2020/01/18 15:26
 */
public class SystemPropertiesExample {

    public static void main(String[] args) {
        HashMap<String,String> testHashMap = new HashMap<>();




        Properties properties = System.getProperties();
        System.out.println(properties);
        System.out.println(properties.getProperty("java.home"));
        System.out.println(properties.getProperty("java.library.path"));
        System.out.println(properties.getProperty("java.class.path"));
        System.out.println(properties.getProperty("java.ext.dirs"));
        System.out.println(properties.getProperty("java.version"));
        System.out.println(properties.getProperty("java.runtime.version"));
        System.out.println(properties.getProperty("file.separator"));
        System.out.println(File.separator);
    }

}
