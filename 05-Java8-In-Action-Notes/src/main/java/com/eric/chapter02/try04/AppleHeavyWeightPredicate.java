package com.eric.chapter02.try04;

import com.eric.chapter02.dto.Apple;

/**
 * 重量筛选策略
 */
public class AppleHeavyWeightPredicate implements ApplePredicate {

    private int weight;
    public AppleHeavyWeightPredicate(int weight) {
        System.out.println("重量选择策略被初始化");
        this.weight = weight;
    }

    @Override
    public boolean predicate(Apple apple) {
        return apple.getWeight() > weight;
    }

}
