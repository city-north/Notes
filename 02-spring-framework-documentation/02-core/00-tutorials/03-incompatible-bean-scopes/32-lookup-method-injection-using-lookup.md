# 使用@Lookup 注解查询方法进行注入

Spring 的 lookup 方法注入的过程就是动态重写一个注册bean 的方法

这个 bean 的方法应该标注为`@Lookup`

```java
@Component
public class MySingletonBean {

    public void showMessage(){
        MyPrototypeBean bean = getPrototypeBean();
        //each time getPrototypeBean() call
        //will return new instance
    }

    @Lookup
    public MyPrototypeBean getPrototypeBean(){
        //spring will override this method
        return null;
    }
}
```

上例中的方法`getPrototypeBean`返回 null,它会被 Spring 动态重写,为了完成这个过程,Spring 使用了 CGLIB

这个动态生成代码会从 ApplicationContext 查找目标 bean 

```java
    ...

    public MyPrototypeBean getPrototypeBean(){
      return applicationContext.getBean(MyPrototypeBean.class);
    }
    ...
```

这是问题 [30-injecting-prototype-bean.md](30-injecting-prototype-bean.md) 的一个解决办法,但是值得注意的是

- bean 的类不能为 final
- 方法必须标注`@Lookup`,不能为 private/static/final
- JavaConfig的工厂方法不起作用，即使用`@Bean`注释的工厂方法和返回一个手工创建的bean实例不起作用。由于容器不负责创建实例，因此它不能动态地创建运行时生成的子类.