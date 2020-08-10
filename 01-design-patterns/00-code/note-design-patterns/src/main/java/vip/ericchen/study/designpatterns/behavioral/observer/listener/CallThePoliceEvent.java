package vip.ericchen.study.designpatterns.behavioral.observer.listener;

import java.util.EventObject;

/**
 * <p>
 * description
 * </p>
 *
 * @author qiang.chen04@hand-china.com 2020/08/10 21:51
 */
public class CallThePoliceEvent extends EventObject {

    public CallThePoliceEvent(Object source) {
        super(source);
    }
}
