# 002-JavaBean属性处理

[TOC]

## 配置绑定

通过 `PropertyDescriptor` 可以基于字段名为可写属性设置值。

比如我们经常会使用这样的配置文件：

```yml
user:
  username: zhangsan
  age: 1
```

配置文件会与对象进行数据绑定。测试代码：

```java
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
```

输出结果

```java
User{username='zhangsan', age=1}
```

## 在Spring中的使用

在传统的 Spring 开发中我们需要在 web.xml 中指定一些配置参数，比如：

```xml
<servlet>
    <servlet-name>app</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value></param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
</servlet>
```

这里有一个 `contextConfigLocation` 参数，这个参数最终是与 `FrameworkServlet` 类中的一个属性进行绑定：

```java
public abstract class FrameworkServlet extends HttpServletBean implements ApplicationContextAware {
  private String contextConfigLocation;
}
```

那么 Spring 是如何将 web.xml 中的配置项与属性进行绑定的呢，可以参数看`org.springframework.web.servlet.HttpServletBean#init()` 方法：

```java
@Override
public final void init() throws ServletException {

  // Set bean properties from init parameters.
  PropertyValues pvs = new ServletConfigPropertyValues(getServletConfig(), this.requiredProperties);
  if (!pvs.isEmpty()) {
    try {
      BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(this);
      ResourceLoader resourceLoader = new ServletContextResourceLoader(getServletContext());
      bw.registerCustomEditor(Resource.class, new ResourceEditor(resourceLoader, getEnvironment()));
      initBeanWrapper(bw);
      bw.setPropertyValues(pvs, true);
    }
    catch (BeansException ex) {
      if (logger.isErrorEnabled()) {
        logger.error("Failed to set bean properties on servlet '" + getServletName() + "'", ex);
      }
      throw ex;
    }
  }

  // Let subclasses do whatever initialization they like.
  initServletBean();
}
```

可以看到 Spring 是通过 `BeanWrapper` 完成对属性的绑定：

```java
public interface BeanWrapper extends ConfigurablePropertyAccessor {
    // 获取属性描述器
    PropertyDescriptor[] getPropertyDescriptors();

    PropertyDescriptor getPropertyDescriptor(String var1) throws InvalidPropertyException;
}
```

而 `BeanWrapper` 又继承了 `PropertyAccessor` 接口：

```java
public interface PropertyAccessor {
    //读属性
    boolean isReadableProperty(String var1);
    //写属性
    boolean isWritableProperty(String var1);

    @Nullable
    Class<?> getPropertyType(String var1) throws BeansException;

    @Nullable
    TypeDescriptor getPropertyTypeDescriptor(String var1) throws BeansException;
}
```

也就是说 Spring 中 `BeanWrapper` 基于 Java 的内省机制实现了对属性的赋值工作，但是 Spring 并未局限于 Java 提供的 API，而是也进行了扩展和进一步的封装，如 `TypeDescriptor`。

可以参考 `org.springframework.web.servlet.HttpServletBean#init()` 中 `BeanWrapper` 的使用来实现对 `User` 对象的属性赋值：

```java
@Test
public void test5(){
    User user = new User();
    BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(user);
    MutablePropertyValues pvs = new MutablePropertyValues();
    pvs.add("username","zhangsan");
    pvs.add("age",1);
    bw.setPropertyValues(pvs);
    System.out.println(user);
}
```

输出结果：

```
User{username='zhangsan', age=1}
```

