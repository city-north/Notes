package cn.eccto.study.springframework.convertion;

import java.beans.PropertyEditorSupport;
import java.io.StringReader;
import java.util.Properties;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author EricChen 2020/12/24 21:46
 */
public class StringToPropertiesPropertyEditor extends PropertyEditorSupport {


    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        Properties properties = new Properties();
        try {
            properties.load(new StringReader(text));
        } catch (Exception e) {
            throw new IllegalArgumentException("");
        }

        //3.临时存储 Properties 对象
        setValue(properties);
    }

    public static void main(String[] args) {
        String text = "name=EricChen";
        StringToPropertiesPropertyEditor stringToPropertiesPropertyEditor = new StringToPropertiesPropertyEditor();
        stringToPropertiesPropertyEditor.setAsText(text);
        final Object value = stringToPropertiesPropertyEditor.getValue();
        System.out.println(value);
    }
}
