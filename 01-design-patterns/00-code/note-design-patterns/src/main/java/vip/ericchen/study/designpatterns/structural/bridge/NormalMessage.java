package vip.ericchen.study.designpatterns.structural.bridge;

/**
 * <p>
 * description
 * </p>
 *
 * @author qiang.chen04@hand-china.com 2020/10/13 20:13
 */
public class NormalMessage extends AbstractMessage {


    public NormalMessage(IMessage message) {
        super(message);
    }
}
