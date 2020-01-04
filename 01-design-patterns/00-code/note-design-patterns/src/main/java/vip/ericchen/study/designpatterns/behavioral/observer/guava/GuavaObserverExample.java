package vip.ericchen.study.designpatterns.behavioral.observer.guava;

import com.google.common.eventbus.EventBus;
import vip.ericchen.study.designpatterns.behavioral.observer.jdk.Eric;
import vip.ericchen.study.designpatterns.behavioral.observer.jdk.Tom;

/**
 * guava test
 *
 * @author EricChen 2020/01/04 14:43
 */
public class GuavaObserverExample {

    public static void main(String[] args) {
        EventBus eventBus = new EventBus();
        eventBus.register(new Eric());
        eventBus.post("123");
    }
}
