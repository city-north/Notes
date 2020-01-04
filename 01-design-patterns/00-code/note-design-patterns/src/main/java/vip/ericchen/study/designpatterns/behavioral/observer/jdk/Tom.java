package vip.ericchen.study.designpatterns.behavioral.observer.jdk;

import java.util.Observable;

/**
 * description
 *
 * @author EricChen 2020/01/04 14:23
 */
public class Tom extends VideoObserver {

    @Override
    protected void watch(Observable o, Object arg) {
        System.out.println("Tom 抢占前排,开始看");
    }
}
