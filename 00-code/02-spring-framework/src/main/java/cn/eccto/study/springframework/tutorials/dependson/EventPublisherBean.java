package cn.eccto.study.springframework.tutorials.dependson;

/**
 * 事件发布 bean
 *
 * @author EricChen 2019/11/14 14:24
 */
public class EventPublisherBean {


    public void initialize() {
        System.out.println("EventPublisherBean initializing");
        EventManager.getInstance().publish("event published from EventPublisherBean");
    }
}