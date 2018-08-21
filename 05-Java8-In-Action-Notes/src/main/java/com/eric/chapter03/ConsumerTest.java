package com.eric.chapter03;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public class ConsumerTest {
    public static void main(String[] args) {
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5);
        forEach(integers,(Integer i) -> System.out.println(i));
    }

    public static <T> void forEach(List<T> list, Consumer<T> c){
        for (T i : list){
            Integer i1 = (Integer) i;
            if (i1 % 2 == 0)
                c.accept(i);
        }
    }
}
