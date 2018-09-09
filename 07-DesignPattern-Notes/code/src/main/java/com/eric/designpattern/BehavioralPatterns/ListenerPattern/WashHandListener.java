package com.eric.designpattern.BehavioralPatterns.ListenerPattern;

/**
 * @author Chen 2018/9/8
 */
public class WashHandListener implements IBeforeEatListener {


    @Override
    public void onBeforeEat() {
        System.out.println("洗洗手。。。");
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
