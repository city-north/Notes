package cn.eccto.study.springframework.tutorials.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

/**
 * 使用{@link ApplicationEvent} 方式自定义应用事件实例
 * 1. 自定义MyEvent{@link MyEvent} 继承自 {@link ApplicationEvent}
 * 2. 自定义 事件发布类{@link MyEvenPublisherBean},引入了 发布器{@link ApplicationEventPublisher}
 * 3. 自定义监听类 {@link AListenerBean} ,标注注解{@link EventListener} 形参就是自定义的事件
 *
 * @author JonathanChen 2019/11/18 20:19
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
