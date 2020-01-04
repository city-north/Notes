package vip.ericchen.study.designpatterns.behavioral.observer.jdk;

import java.util.Observable;

/**
 * postVideo
 *
 * @author EricChen 2020/01/04 14:22
 */
public class YouTuber extends Observable {

    /**
     * 发布视频
     */
    public void postVideo(){
        super.setChanged();
    }
}
