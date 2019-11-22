# Spring 中的国际化

Spring 支持  Internationalization(i18n) 国际化 和本地化 Localization (L10n),专注于通过不同的locale 支持的语言展示 text 标签/消息 

Spring 支持根据外部配置文件(properties)提供不同语言支持



## MessageSource 接口

Spring 对于国际化的支持,我们可以看一个核心接口[org.springframework.context.MessageSource](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/context/MessageSource.html)

```java
public interface MessageSource {
	String getMessage(String code, Object[] args, String defaultMessage,
                                                                    Locale locale);
	String getMessage(String code, Object[] args, Locale locale)
                                                     throws NoSuchMessageException;
	String getMessage(MessageSourceResolvable resolvable,
                                      Locale locale) throws NoSuchMessageException;
}
```

- 形参 code , Spring根据这个 code 去外部文件中拿到国家化的文本
- 形参 args ,运行时进行占位符填充,如{0}或者{1}

![img](assets/msg-sample.png)

- 形参locale : 目标`java.util.Locale`实例
- 形参 resolvable :[MessageSourceResolvable](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/context/MessageSourceResolvable.html)动态消息解析接口,Spring core 校验错误类,例如(FieldError,ObjectError)实现了这个接口,为了简化实用,我们可以绑定所有跟消息源有关的的信息到实现类[DefaultMessageSourceResolvable](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/context/support/DefaultMessageSourceResolvable.html),它更容易作为方法参数传递。

## MessageSource 的实现类

Spring 提供两个开箱即用的实现类:

- **[ResourceBundleMessageSource](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/context/support/ResourceBundleMessageSource.html)** : 它使用了 [java.util.ResourceBundle](https://docs.oracle.com/javase/8/docs/api/java/util/ResourceBundle.html)作为内部使用
- **[ReloadableResourceBundleMessageSource](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/context/support/ReloadableResourceBundleMessageSource.html)**: 这个实现类可以 reload 资源文件,基于时间戳的改变,不需要重启服务器,它底层没有使用[java.util.ResourceBundle](https://docs.oracle.com/javase/8/docs/api/java/util/ResourceBundle.html)而是使用了它自己的加载以及解析逻辑,他可以自动查找 xml 并加载他们

![img](assets/classes.png)

值得注意的是[ApplicationContext](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/context/ApplicationContext.html)接口也集成了 MessageResource 但是他的实现类使用隐式配置的 MessageResource 作为代理,所以一般不认为它是一个成熟的实现类

## 命名转化资源文件

在使用 MessageResource 的时候,我们要遵循文件的命名格式(同时也是 java.util.ResourceBundle 标准)

```
basename_languageCode_countryCode.properties
```

- 其中`basename `可以关联到应用模块名

- 我们可以创建任意多的消息属性,我们可以在classpath 的任何地方
- 建议根据不同的文件组,文件模块归类到不同的文件夹内

下面是实例:

```
+--src
`--+--main
   |--+--java
      |     .........
   `--+--resources
      `--+--messages
         |--+--trade-module
         |  `--+--tradeDetails_en_us.properties
         |     `--tradeDetails_fr_FR.properties
         |        .........
         `--+--order-module
            `--+--orderHistory_en_us.properties
               |--orderHistory_fr_FR.properties
               |--orderPlacement_en_us.properties
               `--orderPlacement_fr_FR.properties
               |  .........

`
```

## 如何使用 MessageSource

Spring 不会提供任何注解方式的消息解决方案

- 通过注解解析,值往往是静态的,在启动装载的
- 在 SpringMVC 应用中,每次请求的 loacle 会根据 HTTP 请求客户端中来生成不同的语言

所以,唯一合适的方式是注入 MessageSource 的实现类作为一个 Bean

```java
@Configuration
    public class Config {

        @Bean
        public MyBean myBean () {
            return new MyBean();
        }

        @Bean
        public MessageSource messageSource () {
            ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
            messageSource.setBasename("messages/msg");
            return messageSource;
        }
    }
```

```jav
 public class MyBean {
        @Autowired
        private MessageSource messageSource;

        public void doSomething () {
            System.out.println(messageSource.getMessage("app.name", new Object[]{"Joe"},
                                                        Locale.getDefault()));
        }
 }
```

## ReloadableResourceBundleMessageSource 示例

```java
@Configuration
    public class Config {

        @Bean
        public MyBean myBean () {
            return new MyBean();
        }

        @Bean
        public MessageSource messageSource () {
            ReloadableResourceBundleMessageSource messageSource =
                                            new ReloadableResourceBundleMessageSource();
            messageSource.setBasename("classpath:messages/msg");
            messageSource.setDefaultEncoding("UTF-8");
            //refresh cache after every 500 mill-secs
            messageSource.setCacheMillis(500);
            return messageSource;
        }
    }
```

下面的实例中,访问消息 20 次然后每次休眠 2 秒,所有我们有足够的事件去修改文件看是否生效

```java
public static class MyBean {
        @Autowired
        private MessageSource messageSource;

        public void doSomething () {
            for (int i = 0; i < 20; i++) {
                System.out.println(messageSource.getMessage("app.name", new Object[]{"Joe"},
                                                            Locale.getDefault()));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
```

## 使用多种语言源

如果我们使用多重源和不同的 base 名称

- 使用一下方式设置多个源

```java
        @Bean
        public MessageSource messageSource () {
            ResourceBundleMessageSource messageSource =
                                          new ResourceBundleMessageSource();
            messageSource.setBasenames("messages/msg", "messages/msg2");
            return messageSource;
        }
```

- 按层次顺序设置源

```java
     @Bean
        public MessageSource messageSource () {
            ResourceBundleMessageSource messageSource =
                                            new ResourceBundleMessageSource();
            messageSource.setBasename("messages/msg2");
            ResourceBundleMessageSource parentMessageSource =
                                            new ResourceBundleMessageSource();
            parentMessageSource.setBasename("messages/msg");
            messageSource.setParentMessageSource(parentMessageSource);
            return messageSource;
        }
```

当我们想要从 parent 重写一些消息的时候,child MessageSource 会先搜索 MessageCode, 如果找不到就会去扫描 Parent 的 MessageCode

#### 值得注意的是

MessageSource 实现类的所有 setters 方法,都定义在它们的超抽象类中(`AbstractResourceBasedMessageSource extends AbstractMessageSource extends MessageSourceSupport`)

`AbstractMessageResource` 也实现了`HierarchicalMessageSource`接口, 这是分层消息解析的抽象。

## 实例代码

#### MessageSourceAware

```java

/**
 * {@link MessageSourceAware} 代码实例
 *
 * @author EricChen 2019/11/18 13:31
 */
@Configuration
public class MessageSourceAwareExample {
    @Bean
    public MyBean myBean () {
        return new MyBean();
    }

    @Bean
    public MessageSource messageSource () {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages/messages");
        messageSource.setDefaultEncoding("utf-8");
        return messageSource;
    }

    public static class MyBean implements MessageSourceAware {
        private MessageSource messageSource;

        public void doSomething () {
            System.out.println(messageSource.getMessage("app.name", new Object[]{"Joe"},
                    Locale.SIMPLIFIED_CHINESE));
        }

        @Override
        public void setMessageSource (MessageSource messageSource) {
            this.messageSource = messageSource;

        }
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MessageSourceAwareExample.class);
        MyBean bean = context.getBean(MyBean.class);
        bean.doSomething();
    }
}

```

```java
/**
 * 测试{@link ResourceBundleMessageSource}
 *
 * @author EricChen 2019/11/18 13:39
 */
@Configuration
public class MessageSourceExample {

    public static void main(String[] args) throws IOException {
        Locale.setDefault(Locale.SIMPLIFIED_CHINESE);
        //uncomment next line to change the locale
        //Locale.setDefault(Locale.FRANCE);
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(MessageSourceExample.class);

        MyBean bean = context.getBean(MyBean.class);
        bean.doSomething();
    }


    @Bean
    public MyBean myBean() {
        return new MyBean();
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages/messages");
        messageSource.setDefaultEncoding("utf-8");
        return messageSource;
    }

    public static class MyBean {
        @Autowired
        private MessageSource messageSource;

        public void doSomething() {
            System.out.println(messageSource.getMessage("app.name", new Object[]{"Joe"},
                    Locale.getDefault()));
        }
    }
}

```

#### ResourceBundleMessageSource

```java
/**
 * 测试 {@link ResourceBundleMessageSource} 的层次结构,如果 child 拿不到相关的信息,那么就去 parent 里拿
 *
 * @author EricChen 2019/11/18 13:47
 */
@Configuration
public class ParentMessageSourceExample {


    public static void main(String[] args) throws IOException {
        Locale.setDefault(Locale.SIMPLIFIED_CHINESE);
        //uncomment next line to change the locale
        //Locale.setDefault(Locale.FRANCE);
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ParentMessageSourceExample.class);

        ParentMessageSourceExample.MyBean bean = context.getBean(ParentMessageSourceExample.MyBean.class);
        bean.doSomething();
    }


    @Bean
    public ParentMessageSourceExample.MyBean myBean() {
        return new ParentMessageSourceExample.MyBean();
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages/messages");
        messageSource.setDefaultEncoding("utf-8");
        ResourceBundleMessageSource parent = new ResourceBundleMessageSource();
        parent.setBasename("messages/parent");
        parent.setDefaultEncoding("utf-8");
        messageSource.setParentMessageSource(parent);
        return messageSource;
    }

    public static class MyBean {
        @Autowired
        private MessageSource messageSource;

        public void doSomething() {
            System.out.println(messageSource.getMessage("test.parent", new Object[]{"Joe"},
                    Locale.getDefault()));
            System.out.println(messageSource.getMessage("test.child", new Object[]{"Joe"},
                    Locale.getDefault()));
        }
    }
}
```

#### ReloadableResourceBundleMessageSource

```java
/**
 * {@link ReloadableResourceBundleMessageSource} 测试类,通过反复读取配置文件中的配置,动态修改配置文件中的数据,会输出
 *
 * @author EricChen 2019/11/18 13:42
 */
@Configuration
public class ReloadableMessageSourceExample {

    public static void main(String[] args) throws IOException {
        Locale.setDefault(Locale.SIMPLIFIED_CHINESE);
        //uncomment next line to change the locale
        //   Locale.setDefault(Locale.FRANCE);
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ReloadableMessageSourceExample.class);

        MyBean bean = context.getBean(MyBean.class);
        bean.doSomething();
    }

    @Bean
    public MyBean myBean() {
        return new MyBean();
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource =
                new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheMillis(500);
        return messageSource;
    }

    public static class MyBean {
        @Autowired
        private MessageSource messageSource;

        public void doSomething() {
            //we are repeating 20 times with 2sec sleep, so that we can modify the
            //files outside (in target/classes folder) to watch the change reloading.
            for (int i = 0; i < 20; i++) {
                System.out.println(messageSource.getMessage("app.name",
                        new Object[]{"Joe"},
                        Locale.getDefault()));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

```

