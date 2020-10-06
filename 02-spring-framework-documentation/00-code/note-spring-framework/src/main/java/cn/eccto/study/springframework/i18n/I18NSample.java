package cn.eccto.study.springframework.i18n;

import java.net.IDN;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * <p>
 * description
 * </p>
 */
public class I18NSample {

    public static void main(String[] args) {

        //Locale 是对本地的封装
        //德国
//        Locale currentLocale = Locale.GERMANY;
//        Locale currentLocale = Locale.US;
        Locale currentLocale = Locale.FRANCE;

        //ResourceBundle 资源数数定义了消息资源
        ResourceBundle messages;

        //我们要获取Message必须要传入Lcoale
        messages = ResourceBundle.getBundle("messages/MessagesBundle", currentLocale);

        System.out.println(messages.getString("greetings"));
        System.out.println(messages.getString("inquiry"));
        System.out.println(messages.getString("farewell"));
        final String nb = IDN.toASCII("牛逼");
        System.out.println(nb);
    }

}
