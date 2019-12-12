package cn.eccto.study.sb.typesafebinding;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 读取配置文件 application-type-safe.properties 和 application-type-safe.yml
 *
 * @author EricChen 2019/12/11 22:07
 */
@Data
@Component
@ConfigurationProperties("app")
public class MyAppProperties {

    boolean sendEmailOnErrors;
    boolean exitOnErrors;
    private List<String> adminEmails;
    private int refreshRate;
    private Map<String, String> orderScreenProperties;
    private Map<String, String> customerScreenProperties;

}