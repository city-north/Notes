# 090-Spring-enabed模块注解

[toc]

## 什么是@Enable模块驱动编程模式

- 驱动注解：@EnableXXX
- 导入注解: @Import 具体实现
- 具体实现
  - 基于Configuration Class
  - 基于ImportSelector 接口实现
  - 基于 ImportBeanDefinitionRegisterar接口实现

## 实例代码

#### 第一步：通过 @EnableXXX 命名

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
// 第二步：通过 @Import 注解导入具体实现
//@Import(HelloWorldConfiguration.class) // 方法一： 通过 Configuration Class 实现
//@Import(HelloWorldImportSelector.class)// 方法二：通过 ImportSelector 接口实现
@Import(HelloWorldImportBeanDefinitionRegistrar.class)// 方法三：通过 ImportBeanDefinitionRegistrar
public @interface EnableHelloWorld { // 第一步：通过 @EnableXXX 命名
}

```

#### 第二步：通过 @Import 注解导入具体实现

有两种方法

- 方法一： 通过 Configuration Class 实现

- 方法二：通过 ImportSelector 接口实现
- 方法三：通过 ImportBeanDefinitionRegistrar

##### 方法一： 通过 Configuration Class 实现

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
// 第二步：通过 @Import 注解导入具体实现
@Import(HelloWorldConfiguration.class) // 方法一： 通过 Configuration Class 实现
public @interface EnableHelloWorld { // 第一步：通过 @EnableXXX 命名
}
```

创建 Configuration Class 实现

```java
@Configuration
public class HelloWorldConfiguration {

    @Bean
    public String helloWorld() {
        return "Hello,World";
    }
}
```

##### 方法二：通过 ImportSelector 接口实现

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
// 第二步：通过 @Import 注解导入具体实现
@Import(HelloWorldImportSelector.class)// 方法二：通过 ImportSelector 接口实现
public @interface EnableHelloWorld { // 第一步：通过 @EnableXXX 命名
}
```

创建选择器,返回扫描的包

```java
public class HelloWorldImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{"org.geekbang.thinking.in.spring.annotation.HelloWorldConfiguration"}; // 导入
    }
}
```

##### 方法三：通过 ImportBeanDefinitionRegistrar

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
// 第二步：通过 @Import 注解导入具体实现
@Import(HelloWorldImportBeanDefinitionRegistrar.class)// 方法三：通过 ImportBeanDefinitionRegistrar
public @interface EnableHelloWorld { // 第一步：通过 @EnableXXX 命名
}
```

手动去注册BeanDefinition

```java
public class HelloWorldImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                        BeanDefinitionRegistry registry) {
        AnnotatedGenericBeanDefinition beanDefinition = new AnnotatedGenericBeanDefinition(HelloWorldConfiguration.class);
        BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinition, registry);
    }
}
```

