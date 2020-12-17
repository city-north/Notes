# 对 Java Bean 的操纵

相关笔记  [03-bean-manipulation-bean-wrapper.md](../../03-validation-data-binding-type-conversion/03-bean-manipulation-bean-wrapper.md) 

Spring bean 创建以及操纵是基于标准 JavaBeans,包`org.springframework.beans`包含接口以及操作 bean 的类



## BeanWapper 接口

Spring 的 BeanWapper 是JavaBean 操作的一个中心接口

![img](assets/beanwrapper.png)

BeanWapper 提供方法去分析以及操作标准的 JavaBean 

- 设置 JavaBean的值
- 设置属性描述
- 查询属性是否可读是否可写
- BeanWrapper还支持设置索引属性。



BeanWapperImpl 是默认的BeanWapper 实现类

![img](assets/impl.png)

代码实例

```java
public class BeanWrapperExample {
    public static void main (String[] args) {
        BeanWrapper bw = new BeanWrapperImpl(new TestBean());
        bw.setPropertyValue("aString", "someString");
        PropertyValue pv = new PropertyValue("anInt", 3);
        //the next commented line will also work
        /*PropertyValue pv = new PropertyValue("anInt", "3");*/
        bw.setPropertyValue(pv);
        System.out.println(bw.getWrappedInstance());

    }
}
```

## ProperyAccessFactory 类

ProperyAccessFactory类是获取 BeanWapper 实例的另一个方法,它基于工厂方法,让我们不用担心具体使用哪个实现类

```java
public class PropertyAccessorFactoryExample {

    public static void main (String[] args) {
        BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(new TestBean());
        bw.setPropertyValue("aString", "anotherString");
        System.out.println(bw.getWrappedInstance());
    }
}
```

## BeanInfoFactory 接口

[org.springframework.beans.BeanInfoFactory](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/beans/BeanInfoFactory.html)接口实际上是[java.beans.Introspector](https://docs.oracle.com/javase/8/docs/api/java/beans/Introspector.html)创建 Spring 增强的[java.beans.BeanInfo](https://docs.oracle.com/javase/8/docs/api/java/beans/BeanInfo.html)的另一个 方法,BeanInfoFactory 只有下列一个方法:

```java
BeanInfo getBeanInfo(Class<?> beanClass)
```

BeanInfoFactory implementation, [org.springframework.beans.ExtendedBeanInfoFactory](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/beans/ExtendedBeanInfoFactory.html), accepts JavaBeans "non-standard" setter methods as 'writable' which returns some values instead of void.

`BeanInfoFactory`实现类,[org.springframework.beans.ExtendedBeanInfoFactory](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/beans/ExtendedBeanInfoFactory.html) 接受 JavaBeans 的"非标准"的 setter 方法作为一个"可写的"方法,返回一些替代 void 的值



```java
import org.springframework.beans.ExtendedBeanInfoFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

public class BeanInfoFactoryExample {
    public static void main (String[] args) throws IntrospectionException {
        System.out.println("-------- using JDK Introspector -");
        useIntrospector();
        System.out.println("-------- using Spring BeanInfoFactory -");
        useBeanInfoFactory();
    }

    private static void useBeanInfoFactory () throws IntrospectionException {
        ExtendedBeanInfoFactory factory = new ExtendedBeanInfoFactory();
        BeanInfo beanInfo = factory.getBeanInfo(MyBean.class);
        for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
            System.out.println(propertyDescriptor);
        }
    }

    private static void useIntrospector () throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(MyBean.class);
        for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
            System.out.println(propertyDescriptor);
        }
    }

    private static class MyBean {
        private String name;

        public String getName () {
            return name;
        }

        public String setName (String str) {
            this.name = "MyBean-" + str;
            return name;
        }
    }
}
```

