package vip.ericchen.study.designpatterns.structural.bridge;

/**
 * <p>
 * description
 * </p>
 *
 */
public class EmailMessage implements IMessage {

    @Override
    public void send(String message, String address) {
        System.out.println("发送邮件Message");
    }
}
