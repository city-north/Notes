package com.eric.chapter03.action;

import com.eric.chapter02.dto.Apple;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static java.util.Comparator.comparing;

/**
 * 用不同的排序策略给一个 Apple 列表排序
 * 这会用到书中迄今讲到的所有概念和功能：行为参数化、匿名类、Lambda
 * 表达式和方法引用。
 */
public class Question {


    public static void main(String[] args) {
        List<Apple> inventory = Arrays.asList(new Apple("green", 155),
                new Apple("green", 80),
                new Apple("red", 120));
        /**
         * 第一步，传递代码
         */
        //Java 8的API已经为你提供了一个 List 可用的 sort 方法
        //void sort(Comparator<? super E> c)
        //自定义Comparator AppleComparator

//        inventory.sort(new AppleComparator());

        /**
         * 第二步，使用匿名内部类
         */
//        inventory.sort(new Comparator<Apple>() {
//            @Override
//            public int compare(Apple apple1, Apple apple2) {
//                return apple1.getWeight().compareTo(apple2.getWeight());
//            }
//        });

        /**
         * 第三步，Lambda表达式
         */
//        inventory.sort((a1,a2) -> a1.getWeight().compareTo(a2.getWeight()));

    /**
     * 你的代码还能变得更易读一点吗？ Comparator 具有一个叫作 comparing 的静态辅助方法，
     它可以接受一个 Function 来提取 Comparable 键值，并生成一个 Comparator 对象（我们会在第
     9章解释为什么接口可以有静态方法）。它可以像下面这样用（注意你现在传递的Lambda只有一
     个参数：Lambda说明了如何从苹果中提取需要比较的键值）
     */

//        inventory.sort(comparing((a) -> a.getWeight()));

        /**
         * 第四步，方法引用
         */
        inventory.sort(comparing(Apple::getWeight));

        System.out.println(inventory);
        /**
         * 恭喜你，这就是你的最终解决方案！这比Java 8之前的代码好在哪儿呢？它比较短；它的意
         思也很明显，并且代码读起来和问题描述差不多：“对库存进行排序，比较苹果的重量。
         */
        //逆序使用
        inventory.sort(comparing(Apple::getWeight).reversed());
        //重量相同的苹果根据颜色排序
        inventory.sort(comparing(Apple::getWeight).reversed().thenComparing(Apple::getColor));
    }
}
