package com.eric.designpattern.BehavioralPatterns.StatePattern;

/**
 * @author Chen 2018/9/9
 */
public class Context {
    private State state;
    /**
     * 其他属性值，该属性值的变化会导致对象的状态发生变化
     */
    private int value;

    public void setState(State state) {
        this.state = state;
    }

    public void request(){
        state.handle();
    }

}
