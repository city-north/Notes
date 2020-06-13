package cn.eccto.study.java;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/06/12 22:42
 */
public class HelloWorld {
    public static void main(String[] args) {


    }
}

abstract class Animal {
    abstract void eat();
}

class JiWaWa extends Animal {

    @Override
    void eat() {
        System.out.println("吉娃娃在吃...");
    }
}
class Cat extends Animal{

    @Override
    void eat() {
        System.out.println("猫在吃");
    }
}










