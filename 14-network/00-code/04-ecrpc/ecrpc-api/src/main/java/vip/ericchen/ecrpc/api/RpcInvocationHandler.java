package vip.ericchen.ecrpc.api;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * <p>
 * description
 * </p>
 *
 * @author ericchen.vip@foxmail.com 2020/04/12 21:55
 */
public class RpcInvocationHandler implements InvocationHandler {
    private String host;
    private int port;

    public RpcInvocationHandler(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpiRequest = new RpcRequest(method.getDeclaringClass().getName(), method.getName(), args);
        RpcRequestHandler rpcRequestHandler = new RpcRequestHandler(host,port,rpiRequest);
        return rpcRequestHandler.send();
    }
}
