package cn.eccto.study.java.guava;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import java.util.EventObject;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2021/08/19 18:24
 */
public class EventBusTest {

    public static void main(String[] args) {
        EventBus eventBus = new EventBus();
        eventBus.register(new EventBusChangeRecorder());
        MyEventObject event = new MyEventObject("123");
        eventBus.post(event);
    }


    static class EventBusChangeRecorder {
        @Subscribe
        public void recordCustomerChange(EventObject e) {
            System.out.println(e);
        }
    }
    static class MyEventObject extends EventObject {
        public MyEventObject(Object source) {
            super(source);
        }
    }


}
