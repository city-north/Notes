package cn.eccto.study.springframework.tutorials.beanwapper;

import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyEditor;
import java.util.Currency;

/**
 * 使用 {@link BeanWrapperImpl} 可以胡哦哦去一个类的默认系统注册的 Editor
 *
 * @author EricChen 2019/11/21 20:30
 */
public class BeanWrapperExample {
    public static void main (String[] args) {
        BeanWrapperImpl wrapper = new BeanWrapperImpl();
        PropertyEditor editor = wrapper.getDefaultEditor(Currency.class);
        editor.setAsText("MYR");
        Currency value = (Currency) editor.getValue();
        System.out.println(value.getDisplayName());

    }
}
