package vip.ericchen.ecrpc.server;

import vip.ericchen.ecrpc.api.RpcRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

/**
 * <p>
 * description
 * </p>
 *
 * @author ericchen.vip@foxmail.com 2020/04/12 21:32
 */
public class ProcessHander implements Runnable {
    private Socket socket;
    private RpcServer rpcServer;

    public ProcessHander(Socket accept, RpcServer rpcServer) {
        this.socket = accept;
        this.rpcServer = rpcServer;
    }

    public void run() {
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            Object object = objectInputStream.readObject();
            if (object instanceof RpcRequest) {
                RpcRequest rpiRequest = (RpcRequest) object;
                Map rpcHandler = rpcServer.getRpcHandler();
                if (rpcHandler == null) {
                    return;
                }
                objectOutputStream.writeObject(process(rpiRequest, rpcHandler));
                objectOutputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Object process(RpcRequest rpiRequest, Map rpcHandler) throws Exception {
        String className = rpiRequest.getClassName();
        String methodName = rpiRequest.getMethodName();
        Object[] parameter = rpiRequest.getParameter();
        Object serviceBean = rpcHandler.get(className);
        Method method = null;
        if (parameter != null) {
            Class[] params = new Class[parameter.length];
            for (int i = 0; i < parameter.length; i++) {
                params[i] = parameter[i].getClass();
            }
            method = serviceBean.getClass().getMethod(methodName, params);
        } else {
            method = serviceBean.getClass().getMethod(methodName);
        }
        return method.invoke(serviceBean, parameter);
    }
}
