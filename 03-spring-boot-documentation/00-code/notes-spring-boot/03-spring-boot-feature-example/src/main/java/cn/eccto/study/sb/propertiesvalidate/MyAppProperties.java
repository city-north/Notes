package cn.eccto.study.sb.propertiesvalidate;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * description
 *
 * @author EricChen 2019/12/12 10:49
 */
@Data
@Component
@ConfigurationProperties("pv")
@Validated
public class MyAppProperties {

    @NotNull
    @Valid
    private MainScreenProperties mainScreenProperties;

    @NotNull
    @Valid
    private PhoneNumber adminContactNumber;
}
