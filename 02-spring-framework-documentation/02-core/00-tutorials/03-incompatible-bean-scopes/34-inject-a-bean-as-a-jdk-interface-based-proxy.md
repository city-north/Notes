# 基于 注入JDK接口的代理对象的 bean

> 版权声明
>
> 此篇文章参考于[logicbig](https://www.logicbig.com/),引用其文字/代码/图片仅用于学习笔记,不用于商业用途

为了解决 [[**更小作用域 Bean 注入问题**]](30-injecting-prototype-bean.md) 

作为上一个例子的另外一个选择,我们可以在 bean 的工厂方法上标注代理模式为`@Scope `注解为`ScopedProxyMode.INTERFACES`,这个模式会指定我们要装载的是接口而不是一个具体的类

在这种模式下:

- Spring 使用 JDK 接口代理模式而不是 CGLIB 模式

```java
/**
 * 使用 JDK 代理技术
 *
 * @author EricChen 2019/11/16 16:20
 */
@Configuration
public class PrototypeJDKProxyBeanExample {

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE,
            proxyMode = ScopedProxyMode.INTERFACES)
    public MyPrototypeBean prototypeBean () {
        return new MyPrototypeBean();
    }

    @Bean
    public MySingletonBean singletonBean () {
        return new MySingletonBean();
    }


    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(PrototypeJDKProxyBeanExample.class);
        MySingletonBean bean = context.getBean(MySingletonBean.class);
        bean.showMessage();
        Thread.sleep(1000);

        bean = context.getBean(MySingletonBean.class);
        bean.showMessage();
    }

}

```

`IPrototype` 接口

```java
/**
 * description
 *
 * @author EricChen 2019/11/16 18:17
 */
interface IPrototype {
    String getDateTime();
}

/**
 * description
 *
 * @author EricChen 2019/11/16 18:17
 */
class MyPrototypeBean implements IPrototype {
    private String dateTimeString = LocalDateTime.now().toString();


    @Override
    public String getDateTime() {
        return dateTimeString;
    }
}

class MySingletonBean {
    @Autowired
    private IPrototype prototypeBean;

    public void showMessage() {
        System.out.println("Hi, the time is " + prototypeBean.getDateTime());
    }
}


```

输出

```
Hi, the time is 2019-11-16T18:20:46.028
Hi, the time is 2019-11-16T18:20:47.037
```

从输出接过来看, singleton bean `MySingletonBean`中引用的`MyPrototypeBean`确实是多例的