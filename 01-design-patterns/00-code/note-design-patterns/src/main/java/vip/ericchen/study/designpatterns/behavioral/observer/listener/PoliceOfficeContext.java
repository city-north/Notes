package vip.ericchen.study.designpatterns.behavioral.observer.listener;

import java.util.EventListener;
import java.util.List;

/**
 * <p>
 * description
 * </p>
 *
 * @author qiang.chen04@hand-china.com 2020/08/10 21:53
 */
public class PoliceOfficeContext {

    private List<PoliceMan> policeManList;

    private List<FireMan> fireManList;


    public void addListener(AlarmType alarmType, EventListener eventListener) {
        if (alarmType.equals(AlarmType.FIRE)) {
            for (PoliceMan policeMan : policeManList) {
                policeMan.addListener(eventListener);
            }
        } else if (alarmType.equals(AlarmType.THIEF)) {
            for (FireMan policeMan : fireManList) {
                policeMan.addListener(eventListener);
            }
        }
    }

}
