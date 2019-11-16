# 注入一个基于类的代理对象的 bean

> 版权声明
>
> 此篇文章参考于[logicbig](https://www.logicbig.com/),引用其文字/代码/图片仅用于学习笔记,不用于商业用途

为了解决 [[**更小作用域 Bean 注入问题**]](30-injecting-prototype-bean.md) 

- 需要注入一个代理对象,这个代理对象暴露的接口与原对象的接口一致

- Spring 使用的 CGLIB 去创建代理对象

- 代理对象调用真正的对象方法
- 每次调用 隐式prototype 对象会有一个新的对象被创建

## Prototype bean 配置一个 Scoped 代理

```java
@Configuration
public class PrototypeProxyBeanExample {

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE,
            proxyMode = ScopedProxyMode.TARGET_CLASS)
    public MyPrototypeBean prototypeBean() {
        return new MyPrototypeBean();
    }

    @Bean
    public MySingletonBean singletonBean() {
        return new MySingletonBean();
    }

    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(PrototypeProxyBeanExample.class);
        MySingletonBean bean = context.getBean(MySingletonBean.class);
        bean.showMessage();
        Thread.sleep(1000);

        bean = context.getBean(MySingletonBean.class);
        bean.showMessage();
    }

}

```

#### 输出

```java
Hi, the time is 2019-11-16T16:21:22.798
Hi, the time is 2019-11-16T16:21:23.814
```

可以看到两个是 不同的对象

#### 值得注意的是

- 例子中使用`proxyMode = ScopedProxyMode.TARGET_CLASS`引起了 AOP代理注入到目标注入点(CGLIB)
- 也可以设置`proxyMode=ScopedProxyMode.INTERFACES`会使用 JDK 的动态代理,它只会拿到目标 bean 的接口类型

```java
public enum ScopedProxyMode {

	/**
	 * Default typically equals {@link #NO}, unless a different default
	 * has been configured at the component-scan instruction level.
	 */
	DEFAULT,

	/**
	 * Do not create a scoped proxy.
	 * <p>This proxy-mode is not typically useful when used with a
	 * non-singleton scoped instance, which should favor the use of the
	 * {@link #INTERFACES} or {@link #TARGET_CLASS} proxy-modes instead if it
	 * is to be used as a dependency.
	 */
	NO,

	/**
	 * Create a JDK dynamic proxy implementing <i>all</i> interfaces exposed by
	 * the class of the target object.
	 */
	INTERFACES,

	/**
	 * Create a class-based proxy (uses CGLIB).
	 */
	TARGET_CLASS;

}

```

