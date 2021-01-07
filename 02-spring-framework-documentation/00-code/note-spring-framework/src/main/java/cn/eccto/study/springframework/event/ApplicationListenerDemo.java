package cn.eccto.study.springframework.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.support.GenericApplicationContext;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author EricChen 2021/01/07 18:25
 */
public class ApplicationListenerDemo {

    public static void main(String[] args) {
        GenericApplicationContext applicationContext = new GenericApplicationContext();
        applicationContext.addApplicationListener(new ApplicationListener<ApplicationEvent>() {
            @Override
            public void onApplicationEvent(ApplicationEvent event) {
                System.out.println("接收到Spring事件" + event);
            }
        });
        applicationContext.refresh();
        applicationContext.start();
        applicationContext.stop();
        applicationContext.close();
    }

}
