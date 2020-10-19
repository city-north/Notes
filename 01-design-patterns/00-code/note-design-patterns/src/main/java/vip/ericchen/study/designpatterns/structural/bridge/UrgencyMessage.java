package vip.ericchen.study.designpatterns.structural.bridge;

/**
 * <p>
 * description
 * </p>
 *
 */
public class UrgencyMessage extends AbstractMessage {

    public UrgencyMessage(IMessage message) {
        super(message);
    }

    public void send(String message, String address) {
        System.out.println("加急");
        this.message.send(message, address);
    }
}
