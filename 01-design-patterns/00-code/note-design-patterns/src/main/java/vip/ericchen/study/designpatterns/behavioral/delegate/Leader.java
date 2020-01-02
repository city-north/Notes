package vip.ericchen.study.designpatterns.behavioral.delegate;

import java.util.HashMap;
import java.util.Map;

/**
 * Leader 维护了谁可以干什么活,得到老板的命令后进行分配
 *
 * @author EricChen 2020/01/02 21:23
 */
public class Leader {
    private Map<String, IWorker> workers = new HashMap<>();

    {
        workers.put("paint", new WorkerA());
        workers.put("build", new WorkerB());
        workers.put("wash", new WorkerC());
    }

    public void dispatcher(String job, String command) {
        workers.get(job).work(command);
    }


}
