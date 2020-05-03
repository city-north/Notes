package cn.eccto.study.java.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * JVM 堆内存溢出测试, VM Args = -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError  -XX:HeapDumpPath=/Users/ec/study/Notes/04-java/00-code/note-java
 * </p>
 *
 * @author EricChen 2020/05/02 16:55
 */
public class ExampleOfHeapOOM {
    static class OOMObject {

    }

    public static void main(String[] args) {
        List<OOMObject> list = new ArrayList<>();
        for (;;) {
            list.add(new OOMObject());
        }
    }
}
