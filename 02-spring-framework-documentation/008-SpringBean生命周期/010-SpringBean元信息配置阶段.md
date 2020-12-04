# 010-SpringBean元信息配置阶段

## 目录

---

[TOC]

## BeanDefinition配置

- 面向资源
  - XML配置
  - [Properties资源配置](#Properties资源配置)
- 面向注解
- 面向API

### XML配置

略

### Properties资源配置

![image-20201116213208976](../../assets/image-20201116213208976.png)

```java
//org.springframework.beans.factory.support public class PropertiesBeanDefinitionReader extends AbstractBeanDefinitionReader
  
  
/**
	Bean definition reader for a simple properties format.
	Provides bean definition registration methods for Map/Properties and ResourceBundle. Typically applied to a 	DefaultListableBeanFactory.
*/

Example:
   employee.(class)=MyClass       // bean is of class MyClass
   employee.(abstract)=true       // this bean can't be instantiated directly
   employee.group=Insurance       // real property
   employee.usesDialUp=false      // real property (potentially overridden)
  
   salesrep.(parent)=employee     // derives from "employee" bean definition
   salesrep.(lazy-init)=true      // lazily initialize this singleton bean
   salesrep.manager(ref)=tony     // reference to another bean
   salesrep.department=Sales      // real property
  
   techie.(parent)=employee       // derives from "employee" bean definition
   techie.(scope)=prototype       // bean is a prototype (not a shared instance)
   techie.manager(ref)=jeff       // reference to another bean
   techie.department=Engineering  // real property
   techie.usesDialUp=true         // real property (overriding parent value)
  
   ceo.$0(ref)=secretary          // inject 'secretary' bean as 0th constructor arg
   ceo.$1=1000000                 // inject value '1000000' at 1st constructor arg
   
```



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
user.(class) =cn.eccto.study.springframework.User
user.id = 11
user.name = 小马哥
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