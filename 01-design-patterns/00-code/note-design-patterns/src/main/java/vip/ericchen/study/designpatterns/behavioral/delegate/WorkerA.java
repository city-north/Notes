package vip.ericchen.study.designpatterns.behavioral.delegate;

/**
 * description
 *
 * @author EricChen 2020/01/02 21:24
 */
public class WorkerA implements IWorker {

    @Override
    public void work(String command) {
        System.out.println("I can do painting" + ",收到命令:"+command);
    }
}
