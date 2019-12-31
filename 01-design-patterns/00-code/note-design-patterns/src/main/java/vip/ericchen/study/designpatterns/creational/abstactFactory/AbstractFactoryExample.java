package vip.ericchen.study.designpatterns.creational.abstactFactory;

/**
 * description
 *
 * @author EricChen 2019/12/31 21:10
 */
public class AbstractFactoryExample {

    public static void main(String[] args) {
        doWithoutPattern();
        doWithPattern();
    }


    private static void doWithoutPattern() {
        AppleKeyBo appleKeyBo = new AppleKeyBo();
        AppleMouse appleMouse = new AppleMouse();
        HpKeybo hpKeybo = new HpKeybo();
        HpMouse hpMouse = new HpMouse();
    }


    private static void doWithPattern(){
        AppleFactory appleFactory = new AppleFactory();
        IKeyBo keybo = appleFactory.createKeybo();
        IMouse mouse = appleFactory.createMouse();
    }
}
