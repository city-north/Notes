package cn.eccto.study.sb.customconverter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * description
 *
 * @author EricChen 2019/12/12 10:25
 */
@Data
@Component
@ConfigurationProperties("cc")
public class MyAppProperties {
    private boolean exitOnErrors;
    private LocalDate tradeStartDate;
}
