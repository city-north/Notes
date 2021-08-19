package cn.eccto.study.java.javabeans;

import java.beans.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

/**
 * <p>
 * Example of {@link java.beans.BeanInfo}
 * </p>
 *
 * @author Jonathan 2020/10/17 21:02
 */
public class BeanInfoExample {
    public static void main(String[] args) throws IntrospectionException {
        final BeanInfo beanInfo = Introspector.getBeanInfo(Person.class, Object.class);
        Person person = new Person();
        final PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            final String propertyName = propertyDescriptor.getName();
            if ("age".equals(propertyName)) {
                propertyDescriptor.setPropertyEditorClass(String2IntegerPropertyEditor.class);
                final PropertyEditor propertyEditor = propertyDescriptor.createPropertyEditor(person);
                propertyEditor.setAsText("123");
            }
        }
        System.out.println(person);
    }


    static class String2IntegerPropertyEditor extends PropertyEditorSupport {
        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            final Integer integer = Integer.valueOf(text);
            setValue(integer);
        }
    }
}
