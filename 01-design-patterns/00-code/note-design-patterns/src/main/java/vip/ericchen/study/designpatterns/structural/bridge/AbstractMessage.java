package vip.ericchen.study.designpatterns.structural.bridge;

/**
 */
public abstract class AbstractMessage {

    IMessage message;

    public AbstractMessage(IMessage message) {
        this.message = message;
    }

    public void sendMessage(String message, String user) {
        //这里有点像代理模式,差距是这里是抽象类
        this.message.send(message, user);
    }
}
