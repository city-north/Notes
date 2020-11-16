# 010-SpringBean元信息配置阶段

 [003-SpringBean的定义-BeanDefiniation](../003-SpringBean的定义-BeanDefiniation/README.md) 

- BeanDefinition配置

## BeanDefinition配置

- 面向资源
  - XML配置
  - [Properties资源配置](#Properties资源配置)
- 面向注解
- 面向API

### Properties资源配置

![image-20201116213208976](../../assets/image-20201116213208976.png)

```java
**
 * Bean 元信息配置示例
 */
public class BeanMetadataConfigurationDemo {

    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 实例化基于 Properties 资源 BeanDefinitionReader
        PropertiesBeanDefinitionReader beanDefinitionReader = new PropertiesBeanDefinitionReader(beanFactory);
        String location = "META-INF/user.properties";
        // 基于 ClassPath 加载 properties 资源
        Resource resource = new ClassPathResource(location);
        // 指定字符编码 UTF-8
        EncodedResource encodedResource = new EncodedResource(resource, "UTF-8");
        int beanNumbers = beanDefinitionReader.loadBeanDefinitions(encodedResource);
        System.out.println("已加载 BeanDefinition 数量：" + beanNumbers);
        // 通过 Bean Id 和类型进行依赖查找
        User user = beanFactory.getBean("user", User.class);
        System.out.println(user);
    }
}

```

user.properties

```properties
#src/main/resources/META-INF/user.properties
user.(class) = org.geekbang.thinking.in.spring.ioc.overview.domain.User
user.id = 001
user.name = 小马哥
user.city = HANGZHOU
```

#### 面向注解

```
@Bean
public User user(){
 	return new User();
}
```



#### 面向API

 [010-BeanDefinition-定义SpringBean.md](../003-SpringBean的定义-BeanDefiniation/010-BeanDefinition-定义SpringBean.md) 