package vip.ericchen.study.designpatterns.behavioral.observer.jdk;

import java.util.Observable;
import java.util.Observer;

/**
 * 视频订阅者
 *
 * @author EricChen 2020/01/04 14:20
 */
public abstract class VideoObserver implements Observer {

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("视频更新啦,快来看");
        watch(o, arg);
    }

    protected abstract void watch(Observable o, Object arg);


}
