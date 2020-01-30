package cn.eccto.study.java.utils;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Implementing this interface allows an object to be the target of the "for-each loop" statements
 * 实现这儿和接口允许对象作为 for 循环的目标对象
 *
 * @author EricChen 2020/01/24 20:03
 */
public interface MyIterable<T> {


    /**
     * 返回一个迭代器
     * return an iterator over elements of type
     *
     * @return 迭代器
     */
    MyIterator<T> iterator();



}
