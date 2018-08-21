package com.eric.chapter03;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * 函数式编程实战
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public class FunctionInterfaceTest {
    /**
     * 再次以Predicate 为例
     * 编写一个静态方法，传入一串数组，使用自己的筛选方式，筛选出想要的数据，偶数、大于3的数字
     */
    public static <T> List <T> myFilter(List<T> list ,Predicate<T> predicate){
        List<T> result = new ArrayList<>();
        for (T t : list){
            if (predicate.test(t))
                result.add(t);
        }
        return result;
    }



    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        //我想筛选偶数
        List<Integer> integers = myFilter(list, i -> i % 2 == 0);
        System.out.println(integers);
        //筛选出大于3的
        List<Integer> integers1 = myFilter(list, i -> i > 3);
        System.out.println(integers1);
    }
}
