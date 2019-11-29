package cn.eccto.study.springframework.constraints;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * description
 *
 * @author EricChen 2019/11/04 22:55
 */
public class MobileValidator implements ConstraintValidator<VaildMobile, String> {

    private boolean required;

    private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");


    @Override
    public void initialize(VaildMobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (required) {
            return isMobile(value);
        } else {
            if (StringUtils.isEmpty(value)) {
                return true;
            } else {
                return isMobile(value);
            }
        }
    }

    public static boolean isMobile(String src) {
        if (StringUtils.isEmpty(src)) {
            return false;
        }
        Matcher m = mobile_pattern.matcher(src);
        return m.matches();
    }

}
