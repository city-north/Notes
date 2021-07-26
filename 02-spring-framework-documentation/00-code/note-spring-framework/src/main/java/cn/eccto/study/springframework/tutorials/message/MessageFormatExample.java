package cn.eccto.study.springframework.tutorials.message;

import java.text.MessageFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * <p>
 * description
 * </p>
 *
 * @author JonathanChen
 */
public class MessageFormatExample {

    public static void main(String[] args) {
        //①信息格式化串
        String pattern1 = "{0}，你好！你于{1}在工商银行存入{2} 元。";
        String pattern2 = "At {1,time,short} On{1,date,long}，{0} paid {2,number, currency}.";
        //②用于动态替换占位符的参数
        Object[] params = {"John", new GregorianCalendar().getTime(), 1.0E3};
        //③使用默认本地化对象格式化信息
        String msg1 = MessageFormat.format(pattern1, params);
        //④使用指定的本地化对象格式化信息
        MessageFormat mf = new MessageFormat(pattern2, Locale.US);
        String msg2 = mf.format(params);
        System.out.println(msg1); //John，你好！你于10/6/20 6:34 PM在工商银行存入1,000 元。
        System.out.println(msg2);// At 6:34 PM OnOctober 6, 2020，John paid $1,000.00.
    }
}
