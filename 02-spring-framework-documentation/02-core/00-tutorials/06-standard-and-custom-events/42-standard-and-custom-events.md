# 标准和自定义事件(Events)

Spring 框架提供了应用级别的事件监听与触发,基于观察者模式,参考笔记 [07-observer-pattern.md](../../../../01-design-patterns/04-behavioral-patterns/07-observer-pattern.md) 

内置的事件有

| Build-in Event                                               | Description                                                  |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| [ContextRefreshedEvent](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/context/event/ContextRefreshedEvent.html) | Event fired when an ApplicationContext gets initialized or refreshed (refreshed via `context.refresh()` call). |
| [ContextStartedEvent](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/context/event/ContextStartedEvent.html) | Event fired when `context.start()` method is called.         |
| [ContextStoppedEvent](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/context/event/ContextStoppedEvent.html) | Event fired when `context.stop()` method is called           |
| [ContextClosedEvent](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/context/event/ContextClosedEvent.html). | Event fired when `context.close()` method is called.         |
| [RequestHandledEvent](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/context/support/RequestHandledEvent.html) | This event can only be used in spring MVC environment. It is called just after an HTTP request is completed. |

更多信息参考笔记 [14-additional-capabilities-of-the-application-contxt.md](../../01-the-ioc-container/14-additional-capabilities-of-the-application-contxt.md) 

## 使用内置事件

- 使用注解 [EventListener](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/context/event/EventListener.html)
- 实现 [ApplicationListener](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/context/ApplicationListener.html)接口

### 使用注解 [EventListener](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/context/event/EventListener.html)

```java
/**
 * 通过 注解 {@link EventListener} 使用内置事件,测试类
 *
 * @author EricChen 2019/11/18 16:20
 */
@Configuration
public class BuildInAnnotationBasedEventExample {


    @Bean
    AListenerBean listenerBean() {
        return new AListenerBean();
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                BuildInAnnotationBasedEventExample.class);
        System.out.println("-- stopping context --");
        context.stop();
        System.out.println("-- starting context --");
        context.start();
        System.out.println("-- closing context --");
        context.close();
    }


    private static class AListenerBean {

        @EventListener
        public void handleContextRefreshed(ContextRefreshedEvent event) {
            System.out.print("context refreshed event fired: ");
            System.out.println(event);
        }

        @EventListener
        public void handleContextStarted(ContextStartedEvent event) {
            System.out.print("context started event fired: ");
            System.out.println(event);
        }

        @EventListener
        public void handleContextStopped(ContextStoppedEvent event) {
            System.out.print("context stopped event fired: ");
            System.out.println(event);
        }

        @EventListener
        public void handleContextClosed(ContextClosedEvent event) {
            System.out.print("context  closed event fired: ");
            System.out.println(event);
        }

    }
}

```

#### 实现 [ApplicationListener](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/context/ApplicationListener.html)接口

```java
/**
 * 通过实现{@link ApplicationListener} 使用内置事件,测试类
 *
 * @author EricChen 2019/11/18 16:24
 */
public class BuildInListenerBasedEventExample {
    @Bean
    AListenerBean listenerBean() {
        return new AListenerBean();
    }

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                BuildInListenerBasedEventExample.class);
        context.close();
    }

    private static class AListenerBean implements ApplicationListener<ContextClosedEvent> {

        @Override
        public void onApplicationEvent(ContextClosedEvent event) {
            System.out.print("context closed event fired: ");
            System.out.println(event);
        }
    }
}

```

### 代码实例

```java
/**
 * 使用{@link ApplicationEvent} 方式自定义应用事件实例
 * 1. 自定义MyEvent{@link MyEvent} 继承自 {@link ApplicationEvent}
 * 2. 自定义 事件发布类{@link MyEvenPublisherBean},引入了 发布器{@link ApplicationEventPublisher}
 * 3. 自定义监听类 {@link AListenerBean} ,标注注解{@link EventListener} 形参就是自定义的事件
 *
 * @author EricChen 2019/11/18 17:19
 */
public class CustomEventWithApplicationEventExample {


    @Bean
    AListenerBean listenerBean() {
        return new AListenerBean();
    }

    @Bean
    MyEvenPublisherBean publisherBean() {
        return new MyEvenPublisherBean();
    }


    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                CustomEventWithApplicationEventExample.class);
        MyEvenPublisherBean bean = context.getBean(MyEvenPublisherBean.class);
        bean.sendMsg("A test message");
    }

    private static class MyEvenPublisherBean {
        @Autowired
        ApplicationEventPublisher publisher;

        public void sendMsg(String msg) {
            publisher.publishEvent(new MyEvent(this, msg));
        }

    }

    private static class AListenerBean {

        /**
         * 通过形参获取到指定的监听事件
         *
         * @param event 自定义事件
         */
        @EventListener
        public void onMyEvent(MyEvent event) {
            System.out.print("event received: " + event.getMsg());
            System.out.println(" -- source: " + event.getSource());
        }
    }

    private static class MyEvent extends ApplicationEvent {
        private final String msg;


        public MyEvent(Object source, String msg) {
            super(source);
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }
    }
}
```

```java
/**
 * 不使用{@link ApplicationEvent} 方式自定义应用事件实例展示如何创建一个自定义事件拓展,只使用一个 POJO 然后注入 ApplicationEventPublisher 去触发自定义事件
 * 1. 自定义MyEvent{@link CustomEventWithoutApplicationEventExample.MyEvent}
 * 2. 自定义 事件发布类{@link CustomEventWithoutApplicationEventExample.MyEvenPublisherBean},引入了 发布器{@link ApplicationEventPublisher}
 * 3. 自定义监听类 {@link CustomEventWithoutApplicationEventExample.AListenerBean} ,标注注解{@link EventListener} 形参就是自定义的事件
 *
 * @author EricChen 2019/11/18 17:27
 */
public class CustomEventWithoutApplicationEventExample {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                CustomEventWithoutApplicationEventExample.class);
        MyEvenPublisherBean bean = context.getBean(MyEvenPublisherBean.class);
        bean.sendMsg("A test message");
    }

    @Bean
    AListenerBean listenerBean() {
        return new AListenerBean();
    }

    @Bean
    MyEvenPublisherBean publisherBean() {
        return new MyEvenPublisherBean();
    }


    private static class AListenerBean {

        @EventListener
        public void onMyEvent(MyEvent event) {
            System.out.print("event received: " + event.getMsg());

        }
    }

    private static class MyEvenPublisherBean {
        @Autowired
        ApplicationEventPublisher publisher;

        public void sendMsg(String msg) {
            publisher.publishEvent(new MyEvent(msg));

        }

    }

    /**
     * 没有继承 {@link ApplicationEvent}
     */
    private static class MyEvent {
        private final String msg;


        public MyEvent(String msg) {
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }
    }
}

```

```java
/**
 * /**
 * 不使用{@link org.springframework.context.ApplicationEvent} 方式自定义应用事件实例展示如何创建一个自定义事件拓展,只使用一个 POJO 然后
 * 通过实现{@link ApplicationEventPublisherAware} 接口 获取ApplicationEventPublisher 去触发自定义事件
 * 1. 自定义MyEvent{@link CustomEventWithoutApplicationEventExample2.MyEvent}
 * 2. 自定义 事件发布类{@link CustomEventWithoutApplicationEventExample2.MyEvenPublisherBean},引入了 发布器{@link ApplicationEventPublisher}
 * 3. 自定义监听类 {@link CustomEventWithoutApplicationEventExample2.AListenerBean} ,标注注解{@link EventListener} 形参就是自定义的事件
 *
 * @author EricChen 2019/11/18 17:27
 */
public class CustomEventWithoutApplicationEventExample2 {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                CustomEventWithoutApplicationEventExample2.class);
        MyEvenPublisherBean bean = context.getBean(MyEvenPublisherBean.class);
        bean.sendMsg("A test message");

    }

    @Bean
    AListenerBean listenerBean() {
        return new AListenerBean();
    }

    @Bean
    MyEvenPublisherBean publisherBean() {
        return new MyEvenPublisherBean();
    }

    //we are not autowiring ApplicationEventPublisher but implementing ApplicationEventPublisherAware this time

    /**
     * 使用 实现 {@link ApplicationEventPublisherAware}的方式设置 ApplicationEventPublisher ,而不是使用@Autowiring 注解
     */
    private static class MyEvenPublisherBean implements ApplicationEventPublisherAware {

        ApplicationEventPublisher publisher;

        public void sendMsg(String msg) {
            publisher.publishEvent(new MyEvent(msg));

        }

        @Override
        public void setApplicationEventPublisher(
                ApplicationEventPublisher applicationEventPublisher) {
            this.publisher = applicationEventPublisher;

        }
    }

    private static class AListenerBean {

        @EventListener
        public void onMyEvent(MyEvent event) {
            System.out.print("event received: " + event.getMsg());

        }
    }

    private static class MyEvent {
        private final String msg;


        public MyEvent(String msg) {
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }
    }
}

```

