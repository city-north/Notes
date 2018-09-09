package com.eric.designpattern.BehavioralPatterns.ListenerPattern;

/**
 * @author Chen 2018/9/8
 */
public class CleanDeskListener implements IBeforeEatListener {
    @Override
    public void onBeforeEat() {
        System.out.println("收拾餐桌，准备吃饭");
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
