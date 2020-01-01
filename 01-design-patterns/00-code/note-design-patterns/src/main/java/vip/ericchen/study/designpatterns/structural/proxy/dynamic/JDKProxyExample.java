package vip.ericchen.study.designpatterns.structural.proxy.dynamic;

import com.sun.media.jfxmediaimpl.HostUtils;
import vip.ericchen.study.designpatterns.structural.proxy.dynamic.jdk.JDKProxy;
import vip.ericchen.study.designpatterns.structural.proxy.staticproxy.Action;
import vip.ericchen.study.designpatterns.structural.proxy.staticproxy.RealObject;


/**
 * JDK 代码实例
 *
 * @author EricChen 2020/01/01 12:22
 */
public class JDKProxyExample {

    public static void main(String[] args) {
        // 保存生成的代理类的字节码文件
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");

        // jdk动态代理测试
        Action action = new JDKProxy(new RealObject()).getProxy();
        System.out.println(action.getClass());
        action.doSomething();
    }

}
