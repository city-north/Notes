package cn.eccto.study.sb.propertiesvalidate;

import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * description
 *
 * @author EricChen 2019/12/12 10:49
 */
@Data
public class PhoneNumber {
    @Pattern(regexp = "\\d{3}-\\d{3}-\\d{4}")
    private String value;
    @Pattern(regexp = "(?i)cell|house|work")
    private String type;
}
