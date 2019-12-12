package cn.eccto.study.sb.propertiesvalidate;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * description
 *
 * @author EricChen 2019/12/12 10:50
 */
@Data
public class MainScreenProperties {
    @Min(1)
    private int refreshRate;
    @Min(50)
    @Max(1000)
    private int width;
    @Min(50)
    @Max(600)
    private int height;
}
