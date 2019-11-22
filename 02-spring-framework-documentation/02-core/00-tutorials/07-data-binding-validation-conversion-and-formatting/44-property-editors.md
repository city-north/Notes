# Property Editors

Spring core 框架使用 [PropertyEditor](https://docs.oracle.com/en/java/javase/11/docs/api/java.desktop/java/beans/PropertyEditor.html) 实例将 文本转化为对象 ,反之亦然

`java.beans.PropertyEditor`是[JavaBeans](http://docs.oracle.com/javase/8/docs/technotes/guides/beans/) 标准的一个一部分

PropertyEditor 通常情况下用在 Swing 的应用中,JavaBeans 标准设计 API 去自检并提取 bean 内部的细节,这些细节可以用于将 bean 属性可视化地显示为组件并可以通过 PropertyEditor 来编辑他们

Spring中使用 PropertyEditors:

- 当使用 XML 配置文件定义 bean的时候,所有 bean的属性都是以文本格式填写的,所以 Spring 需要将他们转化成 Java 类,,这个过程需要找到合适的 PropertyEditor, 例如,如果bean 的属性声明为`java.lang.Class`类型,那么Spring 会使用 `ClassEditor`(一个 PropertyEditor)的实现类来转换

- 在 SpringMVC 框架中,HTTP 请求参数和其他信息都是以文本展示,这个时候就需要使用PropertyEditors实现类对请求进行转换

## Spring 内置的 PropertyEditor 实现类

Spring 提供了许多 PropertyEditor 实现类

`CurrencyEditor` 的 代码片段如下

```java
package org.springframework.beans.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.util.Currency;

public class CurrencyEditor extends PropertyEditorSupport {

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		super.setValue(Currency.getInstance(text));
	}

	@Override
	public String getAsText() {
		Currency value = (Currency) super.getValue();
		return (value != null ? value.getCurrencyCode() : "");
	}
}
```

当我们在 xml 中声明时,Spring 会调用`getAsText`方法然后调用` getValue()`注入`Currency`类

```xml
 <bean id="myBean" class="com.example.MyBean">
        <property name="myCurrencyProp" value="JPY"/>
 </bean>
```

## 创建一个新的 PropertyEditor

在本例中,展示如何转换一个特殊格式的文本手机号到一个 Java Phone 对象

```java
  public class Phone {
        private String phoneNumber;
        private PhoneType phoneType;

       //getters and setters
    }
```

```java
 public enum PhoneType {
        LAND,
        CELL,
        WORK
    }
```

展示我们需要通过 xml 注入Phone 对象到的 bean

```java
public class Customer {
        private String customerName;
        private Phone phone;
    .....
    //getters and setters
}
```

xml

```xml
<beans .....
    <bean id="customer" class="com.logicbig.example.XmlCustomEditorExample.Customer">
        <property name="customerName" value="Steve Thomas"/>
        <property name="phone" value="cell | 907-111-2123"/>
    </bean>

    <bean class="org.springframework.beans.factory.config.CustomEditorConfigurer">
        <property name="customEditors">
            <map>
                <entry key="com.logicbig.example.XmlCustomEditorExample$Phone"
                       value="com.logicbig.example.XmlCustomEditorExample$CustomPhoneEditor"/>
            </map>
        </property>
    </bean>
</beans>
```

注册自定义的PropertyEditor

```java
 public class CustomPhoneEditor extends PropertyEditorSupport {

        @Override
        public void setAsText (String text) throws IllegalArgumentException {
            String[] split = text.split("[|]");
            if (split.length != 2) {
                throw new IllegalArgumentException(
                                    "Phone is not correctly defined. The correct format is " +
                                                        "PhoneType|111-111-1111");
            }

                PhoneType phoneType = PhoneType.valueOf(split[0].trim()
                                                                .toUpperCase());
                Phone phone = new Phone();
                phone.setPhoneType(phoneType);
                phone.setPhoneNumber(split[1].trim());
                setValue(phone);
        }
    }
```

## 代码实例

```java
/**
 * 本实例介绍了 使用 {@link Value} 注解 和{@link PropertySource} 注解注入 properties 参数值,我们不需要进行任何配置,因为 Spring 已经为我们做了这方便的转换,将 text 文本转换成 对象
 * @author EricChen 2019/11/22 11:05
 */
public class JConfigPropertySourceExample {
    public static void main (String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(Config.class);
        ClientBean bean = context.getBean(ClientBean.class);
        bean.doSomething();
    }

    @Configuration
    @PropertySource("classpath:tutorials/propertyeditor/app.properties")
    public static class Config {
        @Bean
        public ClientBean clientBean () {
            return new ClientBean();
        }
    }

    public static class ClientBean {
        @Value("${theCurrency}")
        private Currency currency;

        public void doSomething () {
            System.out.printf("The currency from prop file is %s%n", currency);
            System.out.printf("The currency name is %s%n", currency.getDisplayName());
        }
    }
}

```

```java
/**
 * 本例介绍了, 不适用 XML 注册方式,转而使用 Spring 的 {@link PropertyEditorRegistrar} 去注册一个 custom PropertyEditor 使用 JavaConfig的方式进行注
 *
 * @see JConfigPropertySourceExample2.MyCustomBeanRegistrar#registerCustomEditors 自定义注册方式
 * @see XmlSpringCustomEditorExample 使用 xml 方式进行注册
 * @author EricChen 2019/11/22 12:07
 */
public class JConfigPropertySourceExample2 {

    public static void main (String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(Config.class);
        ClientBean bean = context.getBean(ClientBean.class);
        bean.doSomething();
    }

    @Configuration
    @PropertySource("classpath:tutorials/propertyeditor/app.properties")
    public static class Config {

        @Bean
        public ClientBean clientBean () {
            return new ClientBean();
        }

        @Bean
        public static CustomEditorConfigurer customEditorConfigurer () {
            CustomEditorConfigurer cec = new CustomEditorConfigurer();
            cec.setPropertyEditorRegistrars(
                    new PropertyEditorRegistrar[]{
                            new MyCustomBeanRegistrar()});
            return cec;
        }
    }

    public static class ClientBean {
        @Value("${theTradeDate}")
        private Date tradeDate;

        public void doSomething () {
            System.out.printf("The trade date from prop file is %s%n", tradeDate);
        }
    }

    /**
     * 通过 Spring 提供的 Custom 类自定义解析方式
     */
    public static class MyCustomBeanRegistrar implements PropertyEditorRegistrar {
        @Override
        public void registerCustomEditors (PropertyEditorRegistry registry) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            dateFormatter.setLenient(false);
            registry.registerCustomEditor(Date.class, new CustomDateEditor(dateFormatter, true));
        }
    }
}
```



```java
/**
 * JavaConfig 配置方式, 自定义一个 Custom Editor 一次进行使用
 * 如果我们不想使用 Spring custom PropertyEditor 作为一个全局的类型转换类型,我们可以在运行时一个 custom Editor 实例.这样我们就可在一个指定的注入点使用一次
 *
 * @see Config#getPrice 注册了一个自定义的 {@link CustomNumberEditor}
 * @author EricChen 2019/11/22 11:10
 */
public class JConfigPropertySourceExample3 {

    public static void main (String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(Config.class);
        ClientBean bean = context.getBean(ClientBean.class);
        bean.doSomething();
    }

    @Configuration
    @PropertySource("classpath:tutorials/propertyeditor/app.properties")
    public static class Config {
        @Bean
        public ClientBean clientBean () {
            return new ClientBean();
        }

        @Bean
        @Qualifier("tradePrice")
        public Double getPrice (Environment env) {
            NumberFormat numberFormat = new DecimalFormat("##,###.00");

            CustomNumberEditor customNumberEditor = new CustomNumberEditor(Double.class, numberFormat, true);
            customNumberEditor.setAsText(env.getProperty("thePrice"));
            return (Double) customNumberEditor.getValue();
        }
    }

    public static class ClientBean {
        @Autowired
        @Qualifier("tradePrice")
        private Double price;


        public void doSomething () {
            System.out.printf("The price from prop file is %f%n", price);
        }
    }
}
```

app.properties

```properties
theCurrency=PLN
thePrice=12,323.7654
theTradeDate=2016-9-14
```



```java
/**
 * 使用 Spring xml 配置 的机制注册一个 {@link CustomNumberEditor}
 *
 * @author EricChen 2019/11/22 10:57
 */
public class XmlSpringCustomEditorExample {
    public static void main (String[] args) {
        ApplicationContext context = new
                ClassPathXmlApplicationContext("tutorials/propertyeditor/spring-config2.xml");
        MyBean bean = context.getBean(MyBean.class);
        System.out.println(bean);
    }

    public static class MyBean {
        private Double price;

        public Double getPrice () {
            return price;
        }

        public void setPrice (Double price) {
            this.price = price;
        }

        @Override
        public String toString () {
            return "MyBean{" + "price=" + price + '}';
        }
    }

    public static class MyCustomBeanRegistrar implements PropertyEditorRegistrar {
        @Override
        public void registerCustomEditors (PropertyEditorRegistry registry) {
            NumberFormat numberFormat = new DecimalFormat("##,###.00");
            registry.registerCustomEditor(java.lang.Double.class,
                    new CustomNumberEditor(java.lang.Double.class,
                            numberFormat, true));
        }
    }
}

```

Spring-config2:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
            http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="customer" class="cn.eccto.study.springframework.tutorials.propertyeditor.XmlSpringCustomEditorExample.MyBean">
        <property name="price" value="45,678.567"/>
    </bean>

    <bean class="org.springframework.beans.factory.config.CustomEditorConfigurer">
        <property name="propertyEditorRegistrars">
            <list>
                <bean class="cn.eccto.study.springframework.tutorials.propertyeditor.XmlSpringCustomEditorExample.MyCustomBeanRegistrar" />
            </list>
        </property>
    </bean>
</beans>
```



```java
/**
 * 本章主要介绍了自定义 {@link PropertyEditor} 的用法,自定义一个 {@link PropertyEditor}
 * Spring XML 配置时 Spring 如何从 text 文本转换成对象
 *
 * @author EricChen 2019/11/22 09:35
 */
public class XmlUserCustomEditorExample {

    public static class CustomPhoneEditor extends PropertyEditorSupport {

        @Override
        public void setAsText (String text) throws IllegalArgumentException {
            String[] split = text.split("[|]");
            if (split.length != 2) {
                throw new IllegalArgumentException(
                        "Phone is not correctly defined. The correct format is " +
                                "PhoneType|111-111-1111");
            }
            //the method is already throwing IllegalArgumentException
            //so don't worry about split[0] invalid enum value.
            PhoneType phoneType = PhoneType.valueOf(split[0].trim()
                    .toUpperCase());
            Phone phone = new Phone();
            phone.setPhoneType(phoneType);
            phone.setPhoneNumber(split[1].trim());
            setValue(phone);

        }
    }

    public static void main (String[] args) {
        ApplicationContext context = new
                ClassPathXmlApplicationContext("tutorials/propertyeditor/spring-config.xml");
        Customer bean = context.getBean(Customer.class);
        System.out.println(bean);
    }



    public static class Customer {
        private String customerName;
        private Phone phone;

        public String getCustomerName () {
            return customerName;
        }

        public void setCustomerName (String customerName) {
            this.customerName = customerName;
        }

        public Phone getPhone () {
            return phone;
        }

        public void setPhone (Phone phone) {
            this.phone = phone;
        }

        @Override
        public String toString () {
            return "Customer{" +
                    "customerName='" + customerName + '\'' +
                    ", phone=" + phone +
                    '}';
        }
    }

    public static class Phone {
        private String phoneNumber;
        private PhoneType phoneType;

        public String getPhoneNumber () {
            return phoneNumber;
        }

        public void setPhoneNumber (String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public PhoneType getPhoneType () {
            return phoneType;
        }

        public void setPhoneType (PhoneType phoneType) {
            this.phoneType = phoneType;
        }

        @Override
        public String toString () {
            return "Phone{" +
                    "phoneNumber='" + phoneNumber + '\'' +
                    ", phoneType=" + phoneType +
                    '}';
        }
    }

    public static enum PhoneType {
        LAND,
        CELL,
        WORK
    }


}

```

```properties
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
            http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="customer" class="cn.eccto.study.springframework.tutorials.propertyeditor.XmlUserCustomEditorExample.Customer">
        <property name="customerName" value="Steve Thomas"/>
        <property name="phone" value="cell | 907-111-2123"/>
    </bean>

    <bean class="org.springframework.beans.factory.config.CustomEditorConfigurer">
        <property name="customEditors">
            <map>
                <entry key="cn.eccto.study.springframework.tutorials.propertyeditor.XmlUserCustomEditorExample$Phone"
                       value="cn.eccto.study.springframework.tutorials.propertyeditor.XmlUserCustomEditorExample$CustomPhoneEditor"/>
            </map>
        </property>
    </bean>
</beans>
```

