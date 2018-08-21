package com.eric.chapter02.try04;


import com.eric.chapter02.dto.Apple;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public interface ApplePredicate {

    //将苹果的筛选抽象化
    boolean predicate(Apple t);
}
