package vip.ericchen.study.designpatterns.structural.bridge;

import net.sf.cglib.asm.$AnnotationVisitor;

/**
 * <p>
 * description
 * </p>
 *
 * @author qiang.chen04@hand-china.com 2020/10/13 20:15
 */
public class BridgeExample {
    public static void main(String[] args) {
         IMessage smsMessage = new SmsMessage();
        AbstractMessage normalMessage = new NormalMessage(smsMessage);
        normalMessage.sendMessage("asd", "ericchen");
        smsMessage = new EmailMessage();
    }

}
