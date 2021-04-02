package cn.eccto.study.springframework.aop.interceptor;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author EricChen
 */
public class InterceptorDemo {

    public static void main(String[] args) {
        final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        EchoService echoService = new DefaultEchoService();
        EchoService proxy = new ProxyEchoService().proxy(echoService);
        final String echo = proxy.echo("123");
        System.out.println(echo);
    }

}
