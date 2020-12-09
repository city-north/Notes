# 110-基于ExtensibleXML-authoring拓展SpringXML元素

[TOC]

## SpringXML拓展

1. 编写XML Schema文件：定义XML结构

2. 自定义NamespaceHandler 实现：命名空间绑定

3. 自定义BeanDefinitionReader 实现:XML元素与BeanDefinition解析

4. 注册XML扩展：命名空间与XMLSchema 映射

### 第一步：编写XML Schema文件

`src\main\resources\org\geekbang\thinking\in\spring\configuration\metadata`

```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>

<xsd:schema xmlns="http://www.ericchen.vip/users"
            xmlns:xsd="http://www.w3.org/2001/XMLSchema"
            targetNamespace="http://www.ericchen.vip/users">

    <xsd:import namespace="http://www.w3.org/XML/1998/namespace"/>


    <!-- User 类型定义 -->
    <xsd:complexType name="User">
        <xsd:attribute name="id" type="xsd:long" use="required"/>
        <xsd:attribute name="name" type="xsd:string" use="required"/>
        <xsd:attribute name="city" type="City" use="required"/>
    </xsd:complexType>

    <!-- City -->
    <xsd:simpleType name="City">
        <xsd:restriction base="xsd:string">
            <xsd:enumeration value="Beijing"/>
            <xsd:enumeration value="Shanghai"/>
            <xsd:enumeration value="Guangzhou"/>
        </xsd:restriction>
    </xsd:simpleType>

    <xsd:element name="user" type="User"/>

</xsd:schema>

```

#### 注册xml约束文件

`META-INF\spring.schemas`

```xml
http\://www.ericchen.vip/users.xsd=cn/eccto/study/springframework/metadata/extensible/users.xsd
```

#### 编写配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans
        xmlns="http://www.springframework.org/schema/beans"
        xmlns:users="http://www.ericchen.vip/users"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.ericchen.vip/users
        http://www.ericchen.vip/users.xsd">

    <users:user id="12" name="EricChenChengBei" city="Beijing"/>
</beans>
```

### 第二步：自定义NamespaceHandler

```java
package cn.eccto.study.springframework.metadata.extensible;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * <p>
 * User.xsd 版本的解析处理器 {@link org.springframework.beans.factory.xml.NamespaceHandler}
 * </p>
 *
 * @author qiang.chen04@hand-china.com 2020/12/9 21:28
 */
public class UserNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        this.registerBeanDefinitionParser("user", new UserBeanDefinitionParser());
    }
}
```

注册handler ,注册文件`META-INF\spring.handlers`

```properties
## 定义 namespace 与 NamespaceHandler 的映射
http\://time.geekbang.org/schema/users=org.geekbang.thinking.in.spring.configuration.metadata.UsersNamespaceHandler
```





### 第三步：自定义BeanDefinitionReader

```java
package cn.eccto.study.springframework.metadata.extensible;

import cn.eccto.study.springframework.User;
import org.springframework.beans.factory.config.FieldRetrievingFactoryBean;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * <p>
 * 自定义的BeanDefinition解析器
 * </p>
 *
 * @author qiang.chen04@hand-china.com 2020/12/9 21:30
 */
public class UserBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {
    @Override
    protected Class<?> getBeanClass(Element element) {
        return User.class;
    }
    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        setPropertyValue("id", element, builder);
        setPropertyValue("name", element, builder);
        setPropertyValue("city", element, builder);
    }

    private void setPropertyValue(String attributeName, Element element, BeanDefinitionBuilder builder) {
        String attributeValue = element.getAttribute(attributeName);
        if (StringUtils.hasText(attributeValue)) {
            builder.addPropertyValue(attributeName, attributeValue); // -> <property name="" value=""/>

        }
    }

}

```





### 测试DEMO

```java
public class ExtensibleXmlAuthoringDemo {
    public static void main(String[] args) {
        // 创建 IoC 底层容器
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 创建 XML 资源的 BeanDefinitionReader
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        // 记载 XML 资源
        reader.loadBeanDefinitions("META-INF/user-extensible.xml");
        // 获取 User Bean 对象
        User user = beanFactory.getBean(User.class);
        System.out.println(user);
    }
}
```

## 输出

```
User(user=null, id=12, city=Beijing, name=EricChenChengBei, classLoader=sun.misc.Launcher$AppClassLoader@18b4aac2, beanFactory=org.springframework.beans.factory.support.DefaultListableBeanFactory@4b5189ac: defining beans [12]; root of factory hierarchy, beanName=12)

```

