package vip.ericchen.study.designpatterns.behavioral.observer.jdk;

import com.google.common.eventbus.Subscribe;

import java.util.Observable;

/**
 * description
 *
 * @author EricChen 2020/01/04 14:24
 */
public class Eric extends VideoObserver {


    @Override
    protected void watch(Observable o, Object arg) {
        System.out.println("Eric 搬起小板凳,开始看");
    }

    @Subscribe
    public void guava(String message){
        System.out.println("Eric" +message );
    }
}
