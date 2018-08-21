package com.eric.chapter02;

import com.eric.chapter02.dto.Apple;
import com.eric.chapter02.try04.AppleHeavyWeightPredicate;
import com.eric.chapter02.try04.ApplePredicate;

import java.util.Arrays;
import java.util.List;

/**
 * 原书第二章 【通过行为参数传递代码】
 *
 *  编写一个程序帮助农民了解自己的库存，这位农民可能想有一个查找库存中所有绿色苹果的功能，但是第二天，
 *  他可能告诉你：“其实我还想找出所有重量超过150g的苹果。”又过了两天，农民又跑过来补充到：“要是我
 *  可以找出即是绿色，重量也超过150g苹果就好了”
 *
 *  通过完成一个案例来说明什么是行为参数化
 */
public class Question {



    public static void main(String[] args) {
        List<Apple> inventory = Arrays.asList(new Apple("green",155),
                new Apple("green",80),
                new Apple("red",120));
        /**
         * 第一次尝试没完成对绿色苹果的筛选
         */
        List<Apple> result01 = Try01.filterGreenApples(inventory);
        System.out.println("第一次尝试，筛选绿色苹果："+result01);

        //很显然，第一次尝试不够灵活，如果农民想要筛选出红色的苹果，需要再复制一次该方法，
        //解决办法：将color作为参数传入方法

        /**
         * 第二次尝试，将color作为参数传入方法
         */
        List<Apple> result02 = Try02.filterAppleByColor(inventory,"red");
        System.out.println("第二次尝试，将color作为参数传入方法："+result02);

        //如果农民要加上重量作为筛选条件呢？不能一直加参数吧？

        /**
         * 第三次尝试,对你能想到的每个属性做筛选
         */
        List<Apple> result03 = Try03.filterApples(inventory, "red", 0, true);
        System.out.println("第三次尝试，你能想到的每个属性做筛选："+result03);
        //上面的解决方案显然很笨拙，还需要传flag判断用哪个参数进行筛选

        //行为参数化
        //每一次苹果的筛选都是一个动作，我们可以将这个动作进行抽象，接口
        /**
         * 第四次尝试，策略模式，将行为参数化了
         */
        List<Apple> result04 = Try04.filterApples(inventory, new AppleHeavyWeightPredicate(150));
        System.out.println("第四次尝试，策略模式完成需求："+result04);

        //当然也可以自定义策略，使用匿名内部类
        List<Apple> result04_2 = Try04.filterApples(inventory, new ApplePredicate() {
            @Override
            public boolean predicate(Apple apple) {
                return "green".equals(apple.getColor());
            }
        });
        System.out.println("第四次尝试，使用匿名内部类");

        //缺点，太过啰嗦。第五次尝试使用Lambda表达式
        List<Apple> result05 = Try04.filterApples(inventory,(Apple apple)->"red".equals(apple.getColor()));
        System.out.println("第五次尝试，使用Lambda表达式"+result05);

        /**
         * 第六次尝试,参数泛型化
         */
        List<Apple> result06 = Try05.filterApples(inventory, (Apple apple) -> "green".equals(apple.getColor()));
        System.out.println("第六次尝试，参数泛型化:"+result06);

    }
}
