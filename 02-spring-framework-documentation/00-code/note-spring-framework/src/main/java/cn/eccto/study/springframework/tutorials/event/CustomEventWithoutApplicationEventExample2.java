package cn.eccto.study.springframework.tutorials.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

/**
 * /**
 * 不使用{@link org.springframework.context.ApplicationEvent} 方式自定义应用事件实例展示如何创建一个自定义事件拓展,只使用一个 POJO 然后
 * 通过实现{@link ApplicationEventPublisherAware} 接口 获取ApplicationEventPublisher 去触发自定义事件
 * 1. 自定义MyEvent{@link CustomEventWithoutApplicationEventExample2.MyEvent}
 * 2. 自定义 事件发布类{@link CustomEventWithoutApplicationEventExample2.MyEvenPublisherBean},引入了 发布器{@link ApplicationEventPublisher}
 * 3. 自定义监听类 {@link CustomEventWithoutApplicationEventExample2.AListenerBean} ,标注注解{@link EventListener} 形参就是自定义的事件
 *
 * @author JonathanChen 2019/11/18 20:27
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
