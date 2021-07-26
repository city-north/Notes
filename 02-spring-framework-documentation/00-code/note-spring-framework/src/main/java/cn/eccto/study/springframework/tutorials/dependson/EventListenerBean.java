package cn.eccto.study.springframework.tutorials.dependson;

/**
 * 事件监听 bean
 *
 * @author JonathanChen 2019/11/14 20:24
 */
public class EventListenerBean {
    private void initialize() {
        System.out.println("EventListenerBean initializing");
        EventManager.getInstance()
                .addListener(s -> System.out.println("event received in EventListenerBean : " + s));
    }
}
