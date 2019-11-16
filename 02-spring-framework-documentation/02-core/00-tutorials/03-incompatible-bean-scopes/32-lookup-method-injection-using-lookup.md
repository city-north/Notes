# 使用@Lookup 注解查询方法进行注入

> 版权声明
>
> 此篇文章参考于[logicbig](https://www.logicbig.com/),引用其文字/代码/图片仅用于学习笔记,不用于商业用途

为了解决 [[**更小作用域 Bean 注入问题**]](30-injecting-prototype-bean.md) 

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

## 实例

 [00-code](../../../../00-code/02-spring-framework/src/main/java/cn/eccto/study/springframework/tutorials/lookup) 

```java
/**
 * 使用 {@link org.springframework.beans.factory.annotation.Lookup} 注解完成在一个 Singleton bean 中
 * 获取一个 Prototype 类型的 bean
 *
 * @author EricChen 2019/11/16 16:00
 */
@Configuration
@ComponentScan(basePackageClasses = MySingletonBean.class)
public class LookUpExample {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public MyPrototypeBean prototypeBean() {
        return new MyPrototypeBean();
    }

    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(LookUpExample.class);
        MySingletonBean bean = context.getBean(MySingletonBean.class);
        bean.showMessage();
        Thread.sleep(1000);

        bean = context.getBean(MySingletonBean.class);
        bean.showMessage();
    }
}
```

```java
@Component
class MySingletonBean {

    public void showMessage(){
        MyPrototypeBean bean = getPrototypeBean();
        System.out.println("Hi, the time is "+bean.getDateTime());
    }

    @Lookup
    public MyPrototypeBean getPrototypeBean(){
        //spring will override this method
        return null;
    }
}

```

