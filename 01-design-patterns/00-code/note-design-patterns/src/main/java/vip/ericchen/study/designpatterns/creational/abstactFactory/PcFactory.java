package vip.ericchen.study.designpatterns.creational.abstactFactory;

/**
 * description
 *
 * @author EricChen 2019/12/31 22:19
 */
public interface PcFactory {

    IMouse createMouse();

    IKeyBo createKeybo();
}
