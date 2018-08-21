package com.eric.chapter03.action;

import com.eric.chapter02.dto.Apple;

import java.util.Comparator;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public class AppleComparator implements Comparator<Apple> {
    @Override
    public int compare(Apple apple1, Apple apple2) {
        return apple1.getWeight().compareTo(apple2.getWeight());
    }
}
