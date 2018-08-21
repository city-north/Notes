package com.eric.designpattern.CreationalPatterns.BP.exofeffectiveJava;

/**
 * Effective Java 中的建造者例子
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public class Person {
    //必要参数
    private final int id;
    private final String name;
    //可选参数
    private final int age;
    private final String sex;
    private final String phone;
    private final String address;
    private final String desc;

    public Person(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.age = builder.age;
        this.address = builder.address;
        this.sex = builder.sex;
        this.phone = builder.phone;
        this.desc = builder.desc;
    }

    public static class Builder{
        private final int id;
        private final String name;
        //可选参数
        private int age;
        private String sex;
        private String phone;
        private String address;
        private String desc;

        public Builder(int id,String name) {
            this.id = id;
            this.name = name;
        }

        public Builder age(int age){
            this.age = age;
            return this;
        }

        public Builder sex(String sex){
            this.sex = sex;
            return this;
        }
        public Builder phone(String phone){
            this.phone = phone;
            return this;
        }
        public Builder address(String address){
            this.address = address;
            return this;
        }
        public Builder desc (String desc){
            this.desc = desc;
            return this;
        }
        public Person build(){
            return new Person(this);
        }
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
