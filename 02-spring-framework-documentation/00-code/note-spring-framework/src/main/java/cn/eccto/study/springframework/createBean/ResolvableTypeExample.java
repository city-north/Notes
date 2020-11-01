package cn.eccto.study.springframework.createBean;

import org.springframework.core.ResolvableType;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author EricChen 2020/11/01 11:33
 */
public class ResolvableTypeExample {

    public static void main(String[] args) throws Exception {
        new ResolvableTypeExample().example();

    }
    private HashMap<Integer, List<String>> myMap;

    public void example() throws NoSuchFieldException {
        ResolvableType t = ResolvableType.forField(getClass().getDeclaredField("myMap"));
        t.getSuperType(); // AbstractMap<Integer, List<String>>
        t.asMap(); // Map<Integer, List<String>>
        t.getGeneric(0).resolve(); // Integer
        t.getGeneric(1).resolve(); // List
        t.getGeneric(1); // List<String>
        t.resolveGeneric(1, 0); // String
    }
}
