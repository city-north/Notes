package cn.eccto.study.springframework.tutorials.dependson;

/**
 * 事件监听 bean
 *
 * @author EricChen 2019/11/14 14:24
 */
public class EventListenerBean {
    private void initialize() {
        System.out.println("EventListenerBean initializing");
        EventManager.getInstance()
                .addListener(s -> System.out.println("event received in EventListenerBean : " + s));
    }
}
