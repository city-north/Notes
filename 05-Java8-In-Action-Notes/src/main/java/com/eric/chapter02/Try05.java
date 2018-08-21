package com.eric.chapter02;


import com.eric.chapter02.try05.Predicate;

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

/**
 * 第五次尝试，参数泛型化
 */
public class Try05 {

    public static <T> List<T> filterApples(List<T> inventory, Predicate<T> predicate) {
        List<T> result = new ArrayList<>();
        for (T apple: inventory){
            if (predicate.predicate(apple) ){
                result.add(apple);
            }
        }
        return result;
    }
}
