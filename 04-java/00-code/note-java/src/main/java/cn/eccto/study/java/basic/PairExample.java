package cn.eccto.study.java.basic;

import javafx.util.Pair;

/**
 * Example of Pairs in Java
 *
 * @author EricChen 2020/01/18 19:50
 */
public class PairExample {

    public static void main(String[] args) {
        Pair<Integer, String> pair = new Pair<>(100, "howtodoinjava.com");
        Integer key = pair.getKey();        //100
        String value = pair.getValue();     //howtodoinjava.com

        pair.equals(new Pair<>(100, "howtodoinjava.com"));    //true - same name and value

        pair.equals(new Pair<>(222, "howtodoinjava.com"));    //false - different name

        pair.equals(new Pair<>(100, "example.com"));      //false - different value
    }

}
