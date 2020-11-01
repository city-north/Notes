package cn.eccto.study.java.javabeans;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.beans.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/10/17 21:01
 */
public class JavaBeanExample {


    public static void main(String[] args) throws IntrospectionException {
//        testBeanInfo();
//        testYmlPropertiesFactoryBean2();

    }

    private static void testBeanInfo() throws IntrospectionException {
        final BeanInfo beanInfo = Introspector.getBeanInfo(User.class);
        final PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        System.out.println("属性描述：");
        Stream.of(propertyDescriptors).forEach(System.out::println);
        //方法描述
        System.out.println("方法描述：");
        MethodDescriptor[] methodDescriptors = beanInfo.getMethodDescriptors();
        Stream.of(methodDescriptors).forEach(System.out::println);
        //事件描述
        System.out.println("事件描述：");
        EventSetDescriptor[] eventSetDescriptors = beanInfo.getEventSetDescriptors();
        Stream.of(eventSetDescriptors).forEach(System.out::println);
    }


    /**
     * 测试从Yaml读取属性并通过自省设置到属性中
     *
     * @throws IntrospectionException
     */
    private static void testYmlPropertiesFactoryBean() throws IntrospectionException {
        //构建Yaml读取
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("application.yml"));
        String path = "user.";
        Properties properties = yaml.getObject();
        System.out.println(properties);
        User user = new User();
        //获取 User Bean 信息，排除 Object
        BeanInfo userBeanInfo = Introspector.getBeanInfo(User.class, Object.class);
        //属性描述
        PropertyDescriptor[] propertyDescriptors = userBeanInfo.getPropertyDescriptors();
        Stream.of(propertyDescriptors).forEach(propertyDescriptor -> {
            //获取属性名称
            String property = propertyDescriptor.getName();
            try {
                final Method writeMethod = propertyDescriptor.getWriteMethod();
                if (writeMethod != null) {
                    writeMethod.invoke(user, properties.get(path + property));
                }
            } catch (IllegalAccessException | InvocationTargetException ignored) {
            }
        });
        System.out.println(user);
    }


}
