package vip.ericchen.study.designpatterns.structural.bridge;

/**
 * <p>
 * description
 * </p>
 *
 * @author qiang.chen04@hand-china.com 2020/10/13 20:10
 */
public class SmsMessage implements IMessage {

    @Override
    public void send(String message, String address) {
        System.out.println("发送短信");
    }
}
