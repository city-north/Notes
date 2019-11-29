package cn.eccto.study.springframework.tutorials.event;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextClosedEvent;

/**
 * 通过实现{@link ApplicationListener} 使用内置事件,测试类
 *
 * @author EricChen 2019/11/18 20:24
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
