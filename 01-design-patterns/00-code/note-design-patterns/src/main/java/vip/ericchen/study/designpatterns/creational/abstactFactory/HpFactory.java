package vip.ericchen.study.designpatterns.creational.abstactFactory;

/**
 * description
 *
 * @author EricChen 2019/12/31 22:20
 */
public class HpFactory implements PcFactory {


    public IMouse createMouse() {
        return new HpMouse();
    }

    public IKeyBo createKeybo() {
        return new HpKeybo();
    }
}
