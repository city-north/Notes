# 030-Prototype-Bean作用域

---
[TOC]

## prototype类型Bean的特点

- 每次请求,Spring容器返回的实例都是一个新的实例

- Spring容器没有办法管理prototype Bean的完整的生命周期
- 销毁回调方法将不会执行

## 图示

![prototype](../../assets/prototype.png)

配置方式:

```xml
<bean id="accountService" class="com.something.DefaultAccountService" scope="prototype"/>
```

## 销毁回调

Spring容器没有办法管理prototype Bean的完整的生命周期,也没有办法记录实例的存在

- 销毁回调方法将不会执行,可以利用BeanPostProcessor进行清理工作

```java
// 创建 BeanFactory 容器
AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
// 注册 Configuration Class（配置类） -> Spring Bean
applicationContext.register(BeanScopeDemo.class);

applicationContext.addBeanFactoryPostProcessor(beanFactory -> {
  beanFactory.addBeanPostProcessor(new BeanPostProcessor() {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
      System.out.printf("%s Bean 名称:%s 在初始化后回调...%n", bean.getClass().getName(), beanName);
      return bean;
    }
  });
});

// 启动 Spring 应用上下文
applicationContext.refresh();
```

