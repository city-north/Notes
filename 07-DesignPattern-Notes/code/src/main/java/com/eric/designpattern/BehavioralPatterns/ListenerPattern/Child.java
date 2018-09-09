package com.eric.designpattern.BehavioralPatterns.ListenerPattern;

import java.util.*;

/**
 * 孩子在吃饭前需要洗手，吃饭方法时监听事件，触发洗手
 * @author Chen 2018/9/8
 */
public class Child implements Eatable  {

    private final String name;

    private Map<String,IBeforeEatListener> beforeEatListeners;

    public Child(String name) {
        this.name = name;
        beforeEatListeners = new HashMap<>();
    }

    public void eat() {
        if (beforeEatListeners.isEmpty()){

            beforeEatListeners.put(WashHandListener.class.getName(),new WashHandListener());
        }
        List<IBeforeEatListener> listenerList = new ArrayList<>();
        listenerList.addAll(beforeEatListeners.values());
        Collections.sort(listenerList);
        for (IBeforeEatListener listener : listenerList){
            System.out.print(name+"正在");
            listener.onBeforeEat();
        }
        System.out.println(name +"开始吃东西");
    }

    public void setEventListener(IBeforeEatListener listener){
        beforeEatListeners.put(listener.getClass().getName(),listener);
    }
    public void removeEventListerer(String className){
        beforeEatListeners.remove(className);
    }
}
