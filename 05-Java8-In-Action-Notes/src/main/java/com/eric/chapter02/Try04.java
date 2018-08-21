package com.eric.chapter02;

import com.eric.chapter02.dto.Apple;
import com.eric.chapter02.try04.ApplePredicate;

import java.util.ArrayList;
import java.util.List;

/**
 * 原书第二章 【通过行为参数传递代码】
 *
 *  编写一个程序帮助农民了解自己的库存，这位农民可能想有一个查找库存中所有绿色苹果的功能，但是第二天，
 *  他可能告诉你：“其实我还想找出所有重量超过150g的苹果。”又过了两天，农民又跑过来补充到：“要是我
 *  可以找出即是绿色，重量也超过150g苹果就好了”
 *
 */


public class Try04 {

    public static List<Apple> filterApples(List<Apple> inventory, ApplePredicate applePredicate) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple: inventory){
            //十分笨拙地根据通过flag判断是颜色筛选还是重量筛选
            if (applePredicate.predicate(apple) ){
                result.add(apple);
            }
        }
        return result;
    }
}
