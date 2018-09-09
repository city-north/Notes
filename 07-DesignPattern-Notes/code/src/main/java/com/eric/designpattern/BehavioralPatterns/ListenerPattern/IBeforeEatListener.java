package com.eric.designpattern.BehavioralPatterns.ListenerPattern;


/**
 * 洗手Listener
 * @author Chen 2018/9/8
 */
public interface IBeforeEatListener extends Comparable<IBeforeEatListener> {
    void onBeforeEat();

    default int getOrder(){return 10;}

    @Override
    default int compareTo(IBeforeEatListener o){
        return getOrder() - o.getOrder();
    }
}
