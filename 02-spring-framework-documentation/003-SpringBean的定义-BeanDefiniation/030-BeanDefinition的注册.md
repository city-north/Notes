# 030-BeanDefinition的注册

[TOC]

## 注册的方式

- XML配置
- Java注解配置元信息
  - @Bean
  - @Component
  - @Import
- [JavaAPI配置元信息](#JavaAPI配置元信息)

## 1.JavaAPI配置元信息

#### 1.1命名方式BeanDefinitionRegistry

```java
BeanDefinitionRegistry#registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
```

#### 1.2非命名方式BeanDefinitionReaderUtils

```java
BeanDefinitionReaderUtils#registerBeanDefinitionn(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry)
```

#### 1.3配置类方式

```java
AnnotationConfigApplicationContext#register
```

## 2.代码实例

```java
// 3. 通过 @Import 来进行导入
@Import(AnnotationBeanDefinitionDemo.Config.class)
public class AnnotationBeanDefinitionDemo {

    public static void main(String[] args) {
        // 创建 BeanFactory 容器
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        // 注册 Configuration Class（配置类）
        applicationContext.register(AnnotationBeanDefinitionDemo.class);

        // 通过 BeanDefinition 注册 API 实现
        // 1.命名 Bean 的注册方式
        registerUserBeanDefinition(applicationContext, "mercyblitz-user");
        // 2. 非命名 Bean 的注册方法
        registerUserBeanDefinition(applicationContext);

        // 启动 Spring 应用上下文
        applicationContext.refresh();
        // 按照类型依赖查找
        System.out.println("Config 类型的所有 Beans" + applicationContext.getBeansOfType(Config.class));
        System.out.println("User 类型的所有 Beans" + applicationContext.getBeansOfType(User.class));
        // 显示地关闭 Spring 应用上下文
        applicationContext.close();
    }

    public static void registerUserBeanDefinition(BeanDefinitionRegistry registry, String beanName) {
        BeanDefinitionBuilder beanDefinitionBuilder = genericBeanDefinition(User.class);
        beanDefinitionBuilder
                .addPropertyValue("id", 1L)
                .addPropertyValue("name", "小马哥");

        // 判断如果 beanName 参数存在时
        if (StringUtils.hasText(beanName)) {
            // 注册 BeanDefinition
            registry.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
        } else {
            // 非命名 Bean 注册方法
            BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinitionBuilder.getBeanDefinition(), registry);
        }
    }

    public static void registerUserBeanDefinition(BeanDefinitionRegistry registry) {
        registerUserBeanDefinition(registry, null);
    }

    // 2. 通过 @Component 方式
    @Component // 定义当前类作为 Spring Bean（组件）
    public static class Config {

        // 1. 通过 @Bean 方式定义

        /**
         * 通过 Java 注解的方式，定义了一个 Bean
         */
        @Bean(name = {"user", "xiaomage-user"})
        public User user() {
            User user = new User();
            user.setId(1L);
            user.setName("小马哥");
            return user;
        }
    }


}
```

## 3.注册中心

```java
public interface BeanDefinitionRegistry extends AliasRegistry {

	//注册BeanDefinition
	void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
			throws BeanDefinitionStoreException;

  //删除BeanDefinition
	void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

	//获取BeanDefinition
  BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

  //是否包含BeanDefintion
	boolean containsBeanDefinition(String beanName);

  //获取BeanDefinition的名字
	String[] getBeanDefinitionNames();
	
  //获取BeanDefinitoon个数
	int getBeanDefinitionCount();

	/**
	 * Determine whether the given bean name is already in use within this registry,
	 * i.e. whether there is a local bean or alias registered under this name.
	 * @param beanName the name to check
	 * @return whether the given bean name is already in use
	 */
	boolean isBeanNameInUse(String beanName);

}

```

