package chapter02;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  模拟 Java堆溢出
 *  -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
 * </p>
 *
 * @author EricChen
 */
public class HeapOOM {

    static class OOMObject{

    }

    public static void main(String[] args) {
        List<OOMObject> list = new ArrayList<OOMObject>();
        while(true){
            list.add(new OOMObject());
        }
    }
}
