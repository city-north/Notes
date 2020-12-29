package cn.eccto.study.java.javabeans;


import com.google.common.collect.ImmutableMap;

import java.beans.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * <p>
 * description
 * </p>
 */
public class PropertyEditorExample {
    public static void main(String[] args) throws IntrospectionException {
//        testPropertyEditor();
        testPropertyEditor2();
    }

    private static void testPropertyEditor2() throws IntrospectionException {
        Map<String, Object> properties = ImmutableMap.of("age", 1, "username", "zhangsan", "createTime", "2020-01-01");
        User user = new User();
        BeanInfo beanInfo = Introspector.getBeanInfo(User.class);
        final PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        Stream.of(propertyDescriptors).forEach(propertyDescriptor -> {
            final String name = propertyDescriptor.getName();
            final Object value = properties.get(name);
            if (Objects.equals("createTime", properties)) {
                propertyDescriptor.setPropertyEditorClass(DatePropertyEditor.class);
                final PropertyEditor propertyEditor = propertyDescriptor.createPropertyEditor(user);
                propertyEditor.addPropertyChangeListener(evt -> {
                    final Object value1 = propertyEditor.getValue();
                    setPropertyValue(user, propertyDescriptor, value1);
                });
                propertyEditor.setAsText(String.valueOf(value));
                return;
            }
            setPropertyValue(user, propertyDescriptor, value);
        });
        System.out.println(user);
    }

    private static void testPropertyEditor() throws IntrospectionException {
        Map<String, Object> properties = ImmutableMap.of("age", 1, "username", "zhangsan", "createTime", "2020-01-01");
        User user = new User();
        //获取 User Bean 信息，排除 Object
        BeanInfo userBeanInfo = Introspector.getBeanInfo(User.class, Object.class);
        //属性描述
        PropertyDescriptor[] propertyDescriptors = userBeanInfo.getPropertyDescriptors();
        Stream.of(propertyDescriptors).forEach(propertyDescriptor -> {
            //获取属性名称
            String property = propertyDescriptor.getName();
            //值
            Object value = properties.get(property);
            if (Objects.equals("createTime", property)) {
                //设置属性编辑器
                propertyDescriptor.setPropertyEditorClass(DatePropertyEditor.class);
                //创建属性编辑器
                PropertyEditor propertyEditor = propertyDescriptor.createPropertyEditor(user);
                //添加监听器
                propertyEditor.addPropertyChangeListener(evt -> {
                    //获取转换后的value
                    Object value1 = propertyEditor.getValue();
                    setPropertyValue(user, propertyDescriptor, value1);
                });
                propertyEditor.setAsText(String.valueOf(value));
                return;
            }
            setPropertyValue(user, propertyDescriptor, value);
        });
        System.out.println(user);
    }

    /**
     * 设置属性值
     */
    private static void setPropertyValue(User user, PropertyDescriptor propertyDescriptor, Object value1) {
        try {
            Method writeMethod = propertyDescriptor.getWriteMethod();
            if (writeMethod != null){
                writeMethod.invoke(user, value1);
            }

        } catch (IllegalAccessException | InvocationTargetException ignored) {
        }
    }
}

/**
 * 日期属性编辑器
 */
class DatePropertyEditor extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) {
        try {
            setValue((text == null) ? null : new SimpleDateFormat("yyyy-MM-dd").parse(text));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}