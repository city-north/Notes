package com.eric.chapter03;

import com.eric.chapter02.dto.Apple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public class PredicateTest {
    public static void main(String[] args) {
        List<Apple> inventory = Arrays.asList(new Apple("green",155),
                new Apple("green",80),
                new Apple("red",120));

        List<Apple> filter = filter(inventory, (Apple apple) -> "green".equals(apple.getColor()) && apple.getWeight()>150);
        System.out.println(filter);
    }

    public static <T>List<T> filter (List<T> list, Predicate<T> p){
        List<T> results = new ArrayList<>();
        for (T s : list){
            if (p.test(s))
                results.add(s);
        }
        return results;
    }
}
