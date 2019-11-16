# 使用 JSR-330 Provider 去注入一个更小作用域的 bean

> 版权声明
>
> 此篇文章参考于[logicbig](https://www.logicbig.com/),引用其文字/代码/图片仅用于学习笔记,不用于商业用途

从 Spring 3.0 开始, 支持了 JSR-330里的标准注解 `@javax.inject.Inject`(依赖注入),你可以使用它来替换掉`@Autowire`

为了解决 [[**更小作用域 Bean 注入问题**]](30-injecting-prototype-bean.md) ,我们可以有一个标准的选择:

- 使用`javax.inject.Provider<T>`接口

我们可以注入或自动装配`Provider`接口，将组件类型参数作为我们的原型bean:

```java
 @Autowired
    private Provider<MyPrototypeBean> myPrototypeBeanProvider;
```

根据规范,`Provider.get()`总会返回一个新的 T 类型的实例(上例中的`MyProtoTypeBean`)

## 优势

- 不需要另外配置我们`@Configuration`类
- 自带懒加载属性
- 不包含运行时代码生成

```java
/**
 * 使用 {@link Provider} 来解决更小 Scope bean 注入问题
 *
 * @author EricChen 2019/11/16 18:38
 */
@Configuration
public class NarrowerScopeBeanExample {

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
                new AnnotationConfigApplicationContext(NarrowerScopeBeanExample.class);
        MySingletonBean bean = context.getBean(MySingletonBean.class);
        bean.showMessage();
        Thread.sleep(1000);

        bean = context.getBean(MySingletonBean.class);
        bean.showMessage();
    }


}
```



```java
class MyNarrowerSingletonBean {
    @Autowired
    private Provider<MyPrototypeBean> provider;

    public void showMessage() {
        System.out.println("Hi, the time is " + provider.get().getDateTime());
    }
}


class MyPrototypeBean implements IPrototype {
    private String dateTimeString = LocalDateTime.now().toString();
    @Override
    public String getDateTime() {
        return dateTimeString;
    }
}
```



