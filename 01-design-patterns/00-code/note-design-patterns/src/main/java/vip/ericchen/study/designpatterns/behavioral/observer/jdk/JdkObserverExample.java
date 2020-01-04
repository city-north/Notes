package vip.ericchen.study.designpatterns.behavioral.observer.jdk;


/**
 * JDK 中的观察者模式测试
 *
 * @author EricChen 2020/01/04 14:29
 */
public class JdkObserverExample {
    public static void main(String[] args) {
        YouTuber observer = new YouTuber();
        observer.addObserver(new Eric());
        observer.addObserver(new Tom());
        observer.postVideo();
        observer.notifyObservers();
    }

}
