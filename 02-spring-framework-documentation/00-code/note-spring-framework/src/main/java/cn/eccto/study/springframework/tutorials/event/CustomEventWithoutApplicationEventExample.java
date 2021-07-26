package cn.eccto.study.springframework.tutorials.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

/**
 * 不使用{@link ApplicationEvent} 方式自定义应用事件实例展示如何创建一个自定义事件拓展,只使用一个 POJO 然后注入 ApplicationEventPublisher 去触发自定义事件
 * 1. 自定义MyEvent{@link CustomEventWithoutApplicationEventExample.MyEvent}
 * 2. 自定义 事件发布类{@link CustomEventWithoutApplicationEventExample.MyEvenPublisherBean},引入了 发布器{@link ApplicationEventPublisher}
 * 3. 自定义监听类 {@link CustomEventWithoutApplicationEventExample.AListenerBean} ,标注注解{@link EventListener} 形参就是自定义的事件
 *
 * @author JonathanChen 2019/11/18 20:27
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
