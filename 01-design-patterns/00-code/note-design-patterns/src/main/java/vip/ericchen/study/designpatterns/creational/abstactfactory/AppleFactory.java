package vip.ericchen.study.designpatterns.creational.abstactfactory;

/**
 * description
 *
 * @author EricChen 2019/12/31 22:17
 */
public class AppleFactory implements PcFactory {


    public IMouse createMouse() {
        return new AppleMouse();
    }

    public IKeyBo createKeybo() {
        return new AppleKeyBo();
    }
}
