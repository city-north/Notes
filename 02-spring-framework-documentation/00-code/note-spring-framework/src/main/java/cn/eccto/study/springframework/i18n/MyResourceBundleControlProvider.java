package cn.eccto.study.springframework.i18n;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.spi.ResourceBundleControlProvider;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author EricChen 2020/12/17 20:33
 */
public class MyResourceBundleControlProvider implements ResourceBundleControlProvider {

    @Override
    public ResourceBundle.Control getControl(String baseName) {
        return new ResourceBundle.Control() {
            @Override
            public List<Locale> getCandidateLocales(String baseName, Locale locale) {
                if (baseName == null)
                    throw new NullPointerException();

                if (locale.equals(new Locale("zh", "HK"))) {
                    return Arrays.asList(
                            locale,
                            Locale.TAIWAN,
                            // no Locale.CHINESE here
                            Locale.ROOT);
                } else if (locale.equals(Locale.TAIWAN)) {
                    return Arrays.asList(
                            locale,
                            // no Locale.CHINESE here
                            Locale.ROOT);
                }
                return super.getCandidateLocales(baseName, locale);
            }
        };
    }

    public static void main(String[] args) {

    }
}
