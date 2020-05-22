package cn.eccto.study.sb.configuration;

import java.io.IOException;
import java.util.Properties;

/**
 * <p>
 * 设置 properties
 * </p>
 *
 * @author qiang.chen04@hand-china.com 2020/05/21 12:44
 */
public class PropertiesDemo {

    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        properties.put("name", "城北");
        properties.put("age", 25);
        properties.storeToXML(System.out, "this is a common", "UTF-8");
    }
}
