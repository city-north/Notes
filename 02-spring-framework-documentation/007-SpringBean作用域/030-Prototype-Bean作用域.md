# 030-Prototype-Bean作用域

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

#### 推荐使用 DisposableBean逐个销毁

```java
public class BeanScopeDemo implements DisposableBean {

    @Bean
    // 默认 scope 就是 "singleton"
    public static User singletonUser() {
        return createUser();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static User prototypeUser() {
        return createUser();
    }

    private static User createUser() {
        User user = new User();
        user.setId(System.nanoTime());
        return user;
    }

    @Autowired
    @Qualifier("singletonUser")
    private User singletonUser;

    @Autowired
    @Qualifier("singletonUser")
    private User singletonUser1;

    @Autowired
    @Qualifier("prototypeUser")
    private User prototypeUser;

    @Autowired
    @Qualifier("prototypeUser")
    private User prototypeUser1;

    @Autowired
    @Qualifier("prototypeUser")
    private User prototypeUser2;

    @Autowired
    private Map<String, User> users;

    @Autowired
    private ConfigurableListableBeanFactory beanFactory; // Resolvable Dependency


    @Override
    public void destroy() throws Exception {

        System.out.println("当前 BeanScopeDemo Bean 正在销毁中...");

        this.prototypeUser.destroy();
        this.prototypeUser1.destroy();
        this.prototypeUser1.destroy();
        this.prototypeUser2.destroy();
        // 获取 BeanDefinition
        for (Map.Entry<String, User> entry : this.users.entrySet()) {
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
          //如果是原型则销毁掉
            if (beanDefinition.isPrototype()) { // 如果当前 Bean 是 prototype scope
                User user = entry.getValue();
                user.destroy();
            }
        }

        System.out.println("当前 BeanScopeDemo Bean 销毁完成");
    }
}
```

