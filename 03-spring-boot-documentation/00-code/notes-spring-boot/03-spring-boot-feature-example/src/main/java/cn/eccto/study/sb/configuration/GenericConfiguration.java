package cn.eccto.study.sb.configuration;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * <p>
 * description
 * </p>
 *
 * @author qiang.chen04@hand-china.com 2020/05/21 12:47
 */
public class GenericConfiguration {
    public static void main(String[] args) throws IOException {
        println(System.getProperty("user.home"));
        println(System.getProperty("user.age", "0"));
        // 将 System Properties 转换为 Integer 类型
        println(Integer.getInteger("user.age", 0));
        println(Boolean.getBoolean("user.male"));
        final Properties properties = System.getProperties();
        properties.storeToXML(System.out, "common", "UTF-8");
        final Map<String, String> getenv = System.getenv();
        System.out.println(getenv);
    }

    private static void println(Object object) {
        System.out.println(object);
    }
}
