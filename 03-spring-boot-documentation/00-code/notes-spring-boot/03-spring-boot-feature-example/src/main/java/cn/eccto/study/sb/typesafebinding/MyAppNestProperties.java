package cn.eccto.study.sb.typesafebinding;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 读取配置文件 application-type-safe.properties 和 application-type-safe.yml
 *
 * @author EricChen 2019/12/11 22:07
 */
@Data
@Component
@ConfigurationProperties("nest")
public class MyAppNestProperties {
    boolean sendEmailOnErrors;
    boolean exitOnErrors;
    private List<String> adminEmails;
    private int refreshRate;
    private OrderScreenProperties orderScreenProperties;
    private CustomerScreenProperties customerScreenProperties;
    private MainScreenProperties mainScreenProperties;
}