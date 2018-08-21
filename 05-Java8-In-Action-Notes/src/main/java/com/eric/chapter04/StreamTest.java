package com.eric.chapter04;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author EricChen 2018-5-16
 */
public class StreamTest {
    public static void main(String[] args) {
        //流只能遍历一次
        List<String> title = Arrays.asList("Java8", "In", "Action");
        Stream<String> s = title.stream();
        s.forEach(System.out::println);
        //报错，流已经关闭
//        s.forEach(System.out::println);
    }
}
