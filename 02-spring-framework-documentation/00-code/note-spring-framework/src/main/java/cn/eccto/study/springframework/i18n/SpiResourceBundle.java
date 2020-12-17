package cn.eccto.study.springframework.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author EricChen 2020/12/17 20:30
 */
public class SpiResourceBundle {
    public static void main(String[] args) {

        ResourceBundle rb = ResourceBundle.getBundle(
                "messages/RBControl",
                Locale.CHINA);
        System.out.println(rb.getString("region"));
    }
}
