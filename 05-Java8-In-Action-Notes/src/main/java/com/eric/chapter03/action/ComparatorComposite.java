package com.eric.chapter03.action;

import com.eric.chapter02.dto.Apple;
import com.eric.chapter03.PredicateTest;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * 比较器复合
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public class ComparatorComposite {

    public static void main(String[] args) {
        List<Apple> inventory = Arrays.asList(new Apple("green", 155),
                new Apple("green", 80),
                new Apple("red", 120));

        Predicate<Apple> redApple = a ->a.getColor().equals("red");
        Predicate<Apple> notRedApple = redApple.negate();
        Predicate<Apple> redAndHeavyApple =
                redApple.and(a -> a.getWeight() > 150);
        Predicate<Apple> redAndHeavyAppleOrGreen =
                redApple.and(a -> a.getWeight() > 150)
                        .or(a -> "green".equals(a.getColor()));

        List<Apple> apples = PredicateTest.filter(inventory, redApple);
        System.out.println("只筛选红色苹果--"+apples);
        List<Apple> apples1 = PredicateTest.filter(inventory, notRedApple);
        System.out.println("只筛选不是红色苹果--"+apples1);
        List<Apple> apples2 = PredicateTest.filter(inventory, redAndHeavyApple);
        System.out.println("只筛选红色和超过150的苹果--"+apples2);
        List<Apple> apples3 = PredicateTest.filter(inventory, redAndHeavyAppleOrGreen);
        System.out.println("大于150的红色苹果或者绿色苹果--"+apples3);
    }
}
