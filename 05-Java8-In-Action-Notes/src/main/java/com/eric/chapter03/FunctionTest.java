package com.eric.chapter03;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public class FunctionTest {

    public static <T,R>List<R> map (List<T> list, Function<T,R> func){
        List<R> result = new ArrayList<>();
        for (T s :list){
            result.add( func.apply(s));
        }
        return result;
    }

    public static void main(String[] args) {
        List<String> strings = Arrays.asList("lambdas", "in", "action");
        List<Integer> map = map(strings, String::length);
        System.out.println(map);

        System.out.println(compute1(5,i -> i * 2,i -> i * i));
    }


    /**
     * Function接口中还内置了两个比较常用的默认方法（接口中增加的有具体实现的方法，
     * 扩展了接口功能，子类默认会继承该实现）
     */
    /**
     * compute1方法第一个参数是要计算的数据，第一个是先执行的函数，第二个参数是后执行的函数，
     * 因为输入输出都是数字类型，所以泛型都指定为Integer类型，
     * 通过after.compose(before);将两个函数串联起来然后执行组合后的Funtion方法apply(i)。
     * 当调用compute1(5,i -> i 2,i -> i i)时，先平方再乘以2所以结果是50。
     * 而compute2方法对两个Function的调用正好相反，所以结果是100。
     */

    public static int compute1(int i,Function<Integer,Integer> after,Function<Integer,Integer> before){
        return after.compose(before).apply(i);
    }

    public static int compute2(int i, Function<Integer,Integer> before,Function<Integer,Integer> after){
        return before.andThen(after).apply(i);
    }
}
