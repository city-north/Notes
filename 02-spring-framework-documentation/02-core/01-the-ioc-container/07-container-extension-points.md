# 容器拓展点(Container Extension Points)

- 通过BeanPostProcessor(后置处理器)定制Beans
- 通过BeanFactoryPostProcessor 定制配置元数据
- 通过自定义factoryBean来定制初始化逻辑

## 一、通过BeanPostProcessor(后置处理器)定制Beans
`BeanPostProcessor`接口定义了一个`callback `方法，通过实现这个接口，或者重写容器的默认来自定义初始化逻辑，依赖策略逻辑等等。如果你想实现一些在Spring容器结束实例化、配置、初始化 后自定义Bean的逻辑，你可以使用一个或者多个`BeanPostProcessor`

### 值得注意的是
- 配置多个`BeanPostProcessor`实例后，可以通过实现`Ordered`接口，设置`order`属性来设置执行顺序
- `BeanPostProcessor`实现类在bean实例上操作，所以，Spring IOC Container初始化bean完成后没然后这些`BeanPostProcessor`才开始工作
- `BeanPostProcessor`容器间不共享
- 一个`ApplicationContext`自动扫描元数据中定义的实现`BeanPostProcessor`接口的`Bean`,`ApplicationContext`扫描到这些Bean并将他们注册，以便调用，在`bean`创建时调用，这些处理器可以像任何其他Bean一样部署在容器中。

### 两个回调

```java
public interface BeanPostProcessor {

	//这个回调在容器初始化方法前执行，例如：（initializingBean.afterPropertiesSet()、XML定义的初始化方法）
	
	Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;

	//在容器初始化之后执行
	Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;

}
```

### 实例
下面这个例子通过自定义`BeanPostProcessor`实现类调用`toString() `方法。所有由容器创建的Bean都会进行打印这个`toString()`

### 值得注意的是：
- `postProcessBeforeInitialization`方法回调在容器初始化方法（例如`InitializingBean’s afterPropertiesSet()`或者其他定义的初始化方法 ）

```java
package scripting;

import org.springframework.beans.factory.config.BeanPostProcessor;

public class InstantiationTracingBeanPostProcessor implements BeanPostProcessor {

    // simply return the instantiated bean as-is

    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean; // we could potentially return any object reference here...
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.println("Bean '" + beanName + "' created : " + bean.toString());
        return bean;
    }
}
```

xml配置：
```java
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:lang="http://www.springframework.org/schema/lang"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/lang
        http://www.springframework.org/schema/lang/spring-lang.xsd">

    <lang:groovy id="messenger"
            script-source="classpath:org/springframework/scripting/groovy/Messenger.groovy">
        <lang:property name="message" value="Fiona Apple Is Just So Dreamy."/>
    </lang:groovy>

    <!--
    when the above bean (messenger) is instantiated, this custom
    BeanPostProcessor implementation will output the fact to the system console
    -->
    <bean class="scripting.InstantiationTracingBeanPostProcessor"/>

</beans>
```

### Spring内的一个例子
`RequiredAnnotationBeanPostProcessor`
一个`BeanPostProcessor`实现类，和Spring一起发行，其作用保证了JavaBean的属性都标有一个（或多个）注解都实际上被配置称为依赖注入的一个值



## 二、通过BeanFactoryPostProcessor 定制配置元数据

`org.springframework.beans.factory.config.BeanFactoryPostProcessor`
这个接口的语义和`BeanPostProcessor`类似，但有一个主要的区别：
`BeanFactoryPostProcessor`操作的是bean的配置元数据（configuration metadata），

### 值得注意的是

** Spring容器允许一个`BeanPostProcesso ` 读取配置元数据，并可能在容器实例化除`BeanFactoryPostProcessor`之外的任何bean之前更改它。除了`BeanFactoryPostProcessor`本身，容器允许它在任何bean初始化之前修改它**

你可以配置多个`BeanFactoryPostProcessors`然后通过实现接口`Ordered `并设置order属性的方式来配置执行优先顺序

### 执行顺序
一个bean工厂前置执行器（bean factory post-processor）在`ApplicationContext`声明后会自动执行，
Spring内置了许多bean工厂前置执行器，例如：`PropertyOverrideConfigurer`、
`PropertyPlaceholderConfigurer`
自定义的执行器可以做一些例如注册自定义属性编辑器。

### 例子
`PropertyPlaceholderConfigurer`
使用


```xml
<bean class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
    <property name="locations" value="classpath:com/foo/jdbc.properties"/>
</bean>

<bean id="dataSource" destroy-method="close"
        class="org.apache.commons.dbcp.BasicDataSource">
    <property name="driverClassName" value="${jdbc.driverClassName}"/>
    <property name="url" value="${jdbc.url}"/>
    <property name="username" value="${jdbc.username}"/>
    <property name="password" value="${jdbc.password}"/>
</bean>
```

实际上数据来源来自：
```
jdbc.driverClassName=org.hsqldb.jdbcDriver
jdbc.url=jdbc:hsqldb:hsql://production:9002
jdbc.username=sa
jdbc.password=root
```

如上述，`${jdbc.username}`会被在运行时替换为`sa`,`PropertyPlaceholderConfigurer`会检查占位符对应的属性值，另外可以定制占位符前缀和后缀。

可以使用一下方式指定一个placeholder:

```xml
<context:property-placeholder location="classpath:com/foo/jdbc.properties"/>
```

`PropertyPlaceholderConfigurer`不仅仅会检查你指定的Properties 文件默认他也会在找不到的情况下，去找Java`System`属性，你可以通过设置属性`systemPropertiesMode `来定制这个行为：
```
never (0): Never check system properties

fallback (1): Check system properties if not resolvable in the specified properties files. This is the default.

override (2): Check system properties first, before trying the specified properties files. This allows system properties to override any other property source.
```

### 例子
`PropertyOverrideConfigurer`
类似于上述的`PropertyPlaceholderConfigurer`，区别在于，你可以定义默认值，如果一个Properties文件无法找到一个确切的值，会使用默认的值。

配置文件格式：
```xml
PropertyPlaceholderConfigurer
```

例如：
```xml
dataSource.driverClassName=com.mysql.jdbc.Driver
dataSource.url=jdbc:mysql:mydb
```

声明：

```
<context:property-override location="classpath:override.properties"/>
```

##  三、 通过自定义factoryBean来定制初始化逻辑

实现`org.springframework.beans.factory.FactoryBean`来自定义一个工厂。


如果有相当复杂的初始化代码，可能会编写很长的XML,这个时候可以创建自己的工厂Bean，在该类中自定义初始化逻辑，然后将这个工厂Bean插入到容器中，

 `FactoryBean`接口：

```java
public interface FactoryBean<T> {
	//返回这个工厂创建的Bean实例，这个实例是否能够共享，要看使用的是单例还是原型
	@Nullable
	T getObject() throws Exception;
	
	//	返回创建的对象类型，如果不知道，返回Null
	@Nullable
	Class<?> getObjectType();
	
	
	//是否为单例
	default boolean isSingleton() {
		return true;
	}
}

```


### 值得注意的是：
- 这种方式在Spring中使用广泛，Spring内置超过50多个实现类
- 有时会有这样的需求，你要请求的不是这个工厂生成的Bean，而是这个工厂本身，这个时候在调用`ApplicationContext.getBean()`
方法时，在bean的id前面加上一个&，就会返回这个工厂对象本身：

```
ApplicationContext.getBean(“myBean”)；返回的是bean对象
ApplicationContext.getBean(“&myBean”)；返回的是工厂对象
```