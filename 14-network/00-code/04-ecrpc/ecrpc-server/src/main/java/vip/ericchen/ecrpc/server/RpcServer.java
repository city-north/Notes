package vip.ericchen.ecrpc.server;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import vip.ericchen.ecrpc.anno.RpcService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * <p>
 * description
 * </p>
 *
 * @author ericchen.vip@foxmail.com 2020/04/12 21:10
 */
public class RpcServer implements InitializingBean, ApplicationContextAware {

    private volatile boolean running = true;

    private ApplicationContext context;

    private Map rpcHandler = new HashMap();

    private int port;

    public RpcServer(int port) {
        this.port = port;
    }

//    private ExecutorService service = new ThreadPoolExecutor(
//            0, Integer.MAX_VALUE, 60L,
//            TimeUnit.SECONDS, new SynchronousQueue<Runnable>(),
//            Executors.defaultThreadFactory());

    private ExecutorService service = Executors.newCachedThreadPool();

    public void publish() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (running) {
            try {
                Socket accept = serverSocket.accept();
                service.execute(new ProcessHander(accept, this));
            } catch (Exception e) {
                if (serverSocket != null) {
                    try {
                        serverSocket.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                e.printStackTrace();
            }
        }

    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }


    public void afterPropertiesSet() throws Exception {
        Map<String, Object> beansWithAnnotation = context.getBeansWithAnnotation(RpcService.class);
        if (beansWithAnnotation == null) {
            return;
        }
        for (Object value : beansWithAnnotation.values()) {
            RpcService annotation = value.getClass().getAnnotation(RpcService.class);
            Class<?> service = annotation.service();
            rpcHandler.put(service.getName(), value);
        }
        publish();
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public Map getRpcHandler() {
        return rpcHandler;
    }

    public void setRpcHandler(Map rpcHandler) {
        this.rpcHandler = rpcHandler;
    }
}
