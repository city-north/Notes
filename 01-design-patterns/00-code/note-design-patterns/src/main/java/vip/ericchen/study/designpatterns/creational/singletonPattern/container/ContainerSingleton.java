package vip.ericchen.study.designpatterns.creational.singletonPattern.container;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * description
 *
 * @author EricChen 2020/01/01 00:09
 */
public class ContainerSingleton {
    private static final Map<String, Object> CONTAINER = new ConcurrentHashMap<>();


    private ContainerSingleton() {
        //防止反射攻击
        throw new IllegalArgumentException("HungrySingleton not allow be constructed");
    }


    public static Object getInstance(String qualifiedName) {
        synchronized (CONTAINER) {
            try {
                if (!CONTAINER.containsKey(qualifiedName)) {
                    Object o = Class.forName(qualifiedName).newInstance();
                    CONTAINER.put(qualifiedName, CONTAINER);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return CONTAINER.get(qualifiedName);
        }
    }

}
