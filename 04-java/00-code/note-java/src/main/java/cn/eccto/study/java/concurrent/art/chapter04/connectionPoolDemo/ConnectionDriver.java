package cn.eccto.study.java.concurrent.art.chapter04.connectionPoolDemo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 连接池驱动
 * </p>
 *
 * @author EricChen 2020/03/07 21:41
 */
public class ConnectionDriver {

    public static final Connection createConnection() {
        return (Connection) Proxy.newProxyInstance(ConnectionHandler.class.getClassLoader(), new Class[]{Connection.class}, new ConnectionHandler());
    }


    public static class ConnectionHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //模拟数据库提交
            if (method.getName().equals("commit")) {
                TimeUnit.MILLISECONDS.sleep(100);
            }
            return null;
        }
    }
}
