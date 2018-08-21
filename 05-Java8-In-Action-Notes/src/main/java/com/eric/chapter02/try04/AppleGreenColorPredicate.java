package com.eric.chapter02.try04;

import com.eric.chapter02.dto.Apple;

/**
 * 颜色筛选策略
 */
public class AppleGreenColorPredicate implements ApplePredicate {

    @Override
    public boolean predicate(Apple apple) {
        return "green".equals(apple.getColor());
    }
}
