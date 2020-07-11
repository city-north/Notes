package cn.eccto.study.java.concurrent.art.chapter05;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;

import java.util.concurrent.locks.LockSupport;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/07/11 16:40
 */
public class LockSupporttest3 {

    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(() -> {
            System.out.println("child thread begin park");
            //确保只有中断返回,才相应
            while (!Thread.currentThread().isInterrupted()) {
                LockSupport.park();
            }
            System.out.println("child thread unpark");
        });
        thread.start();
        Thread.sleep(1000);
        System.out.println("main thread begin unpark");
        //中断子线程
        thread.interrupt();
    }
}
