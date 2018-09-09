package com.eric.designpattern.BehavioralPatterns.ListenerPattern;

/**
 * @author Chen 2018/9/8
 */
public class main {
    public static void main(String[] args) {
        Child child  = new Child("小明");
        child.setEventListener(new WashHandListener());
        child.setEventListener(new CleanDeskListener());
        child.eat();
    }
}
