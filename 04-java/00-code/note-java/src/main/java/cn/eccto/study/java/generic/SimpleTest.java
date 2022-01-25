package cn.eccto.study.java.generic;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * description
 *
 * @author chen 2022/01/25 1:50 PM
 */
public class SimpleTest<T> {

    public T[] array;


    public static void main(String[] args) throws NoSuchFieldException {
        Class<String> stringClass = String.class;
        ClassLoader classLoader = stringClass.getClassLoader();
        //Bootstrap ClassLoader
//        System.out.println(classLoader);
//        Method[] methods = stringClass.getMethods();
//        for (Method method : methods) {
//            System.out.println(method);
//        }
//        Method[] declaredMethods = stringClass.getDeclaredMethods();
        testGenericArrayType();
    }

    private static void testGenericArrayType() throws NoSuchFieldException {
        Class<SimpleTest> simpleTestClass = SimpleTest.class;
        Field array = simpleTestClass.getField("array");
    }

}
