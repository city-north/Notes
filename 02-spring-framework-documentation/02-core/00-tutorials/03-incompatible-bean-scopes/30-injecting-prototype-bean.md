# 将一个 Prototype Bean 注入到 Singleton Bean

如果相同 Scope 的 bean 被组合在一起没有问题,例如，一个单例bean A 被注入到另一个单例bean B中，但是如果bean A的作用域更窄，比如`prototype`作用域，那么就有问题了。

为了理解这个问题,我们可以看个例子,两个 bean 分别是

- `MyPrototypeBean` : scope 是`prototype`
- `MySingletonBean`:scope 是 `singleton`

我们通过方法将`MyPrototypeBean`注入到`MySingletonBean`,并且使用`context#getBean(MySingletonBean.class)`多次获取`MySingletonBean`

我们期望(根据原型规范)创建一个新的原型bean，并在每次都将其注入到`MySingletonBean`中。

```java
@Configuration
public class PrototypeProblemExample {
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public MyPrototypeBean prototypeBean() {
        return new MyPrototypeBean();
    }

    @Bean
    public MySingletonBean singletonBean() {
        return new MySingletonBean();
    }

    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(PrototypeProblemExample.class);
        MySingletonBean bean = context.getBean(MySingletonBean.class);
        bean.showMessage();
        System.out.println(bean.getPrototypeBean());
        Thread.sleep(1000);

        MySingletonBean bean2 = context.getBean(MySingletonBean.class);
        System.out.println(bean2.getPrototypeBean());
        System.out.println("bean 中的 MyPrototypeBean 是同一个对象吗" + bean.getPrototypeBean().equals(bean2.getPrototypeBean()));
        bean.showMessage();
    }
}
```

```java
Hi, the time is 2019-11-15T16:19:45.949
cn.eccto.study.springframework.tutorials.prototype.MyPrototypeBean@378542de
cn.eccto.study.springframework.tutorials.prototype.MyPrototypeBean@378542de
bean 中的 MyPrototypeBean 是同一个对象吗true
Hi, the time is 2019-11-15T16:19:45.949
```

根据输出我们可以看出,将多例scope 的 bean 注入到单例 scope 时,多例失效,

## 解决办法:

- inject `ApplicationContext` bean into `MySingletonBean` to get instance of `MyPrototypeBean`, whenever we need it. Example [here](https://www.logicbig.com/tutorials/spring-framework/spring-core/using-application-context-aware.html). 本笔记中 [30-injecting-prototype-bean.md](30-injecting-prototype-bean.md) 
- Lookup method injection. This approach involves in dynamic bytecode generation. Example [here](https://www.logicbig.com/tutorials/spring-framework/spring-core/using-lookup-method.html).本笔记中  [32-lookup-method-injection-using-lookup.md](32-lookup-method-injection-using-lookup.md) 
- Using Scoped Proxy. Example [here](https://www.logicbig.com/tutorials/spring-framework/spring-core/scoped-proxy.html) and [here](https://www.logicbig.com/tutorials/spring-framework/spring-core/scoped-interface-based-proxy.html). [33-injecting-a-bean-as-a-class-based-proxy-object.md](33-injecting-a-bean-as-a-class-based-proxy-object.md) 和 [34-inject-a-bean-as-a-jdk-interface-based-proxy.md](34-inject-a-bean-as-a-jdk-interface-based-proxy.md) 
- Using JSR 330 `Provider` injection by Spring. Example [here](https://www.logicbig.com/tutorials/spring-framework/spring-core/using-jsr-330-provider.html). [35-using-jsr-330-provider-to-inject-narrower-scoped-bean.md](35-using-jsr-330-provider-to-inject-narrower-scoped-bean.md) 