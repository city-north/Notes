package vip.ericchen.ecrpc.api;

import java.lang.reflect.Proxy;

/**
 * <p>
 * description
 * </p>
 *
 * @author ericchen.vip@foxmail.com 2020/04/12 21:50
 */
public class RpcProxyClient {


    @SuppressWarnings("unchecked")
    public static <T> T clientProxy(final Class<T> targetInterface, final String host, final int port) {
        return (T) Proxy.newProxyInstance(targetInterface.getClassLoader(), new Class[]{targetInterface}, new RpcInvocationHandler(host,port));
    }
}
