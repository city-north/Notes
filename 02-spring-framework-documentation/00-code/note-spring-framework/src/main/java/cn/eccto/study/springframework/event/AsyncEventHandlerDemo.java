package cn.eccto.study.springframework.event;

import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * <p>
 * 异步的事件处理器监听器示例
 * </p>
 *
 * @author qiang.chen04@hand-china.com 2021/1/9 22:36
 */
public class AsyncEventHandlerDemo {


    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        context.addApplicationListener(new MySpringEventListener());

        context.refresh();

        final ApplicationEventMulticaster multicaster = context.getBean(AbstractRefreshableConfigApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME, ApplicationEventMulticaster.class);
        if (multicaster instanceof SimpleApplicationEventMulticaster){
            final SimpleApplicationEventMulticaster simpleApplicationEventMulticaster = (SimpleApplicationEventMulticaster) multicaster;
            simpleApplicationEventMulticaster.setTaskExecutor(Executors.newSingleThreadExecutor());

        }

    }
}
