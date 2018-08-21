package com.eric.designpattern.CreationalPatterns.BP;


import com.eric.designpattern.CreationalPatterns.BP.exofeffectiveJava.Person;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public class Test {

    public static void main(String[] args) {
        Person person = new Person.Builder(1,"张三").address("地址").age(12).build();
        System.out.println(person);

        Director director = new Director();
        Milk milk = director.buildMilk(new MilkBuilder());
        Iphone iphone = director.buildIphone(new IphoneBuilder());
        System.out.println(milk);
        System.out.println(iphone);

        StringBuilder builder = new StringBuilder();
        String jack = builder.append(123).insert(1, "jack").reverse().toString();
        System.out.println(jack);
    }

}
