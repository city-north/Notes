package cn.eccto.study.java.generic;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * <p>
 * description
 * </p>
 *
 * @author Jonathan 2021/01/06 17:36
 */
public class TypeTest2 {
    public static void main(String[] args) {
        //原始类型 raw type
        final Class<Integer> integerClass = int.class;
        final Class arrayListClass = ArrayList.class;
        final ParameterizedType parameterizedType = (ParameterizedType) arrayListClass.getGenericSuperclass();

        System.out.println(parameterizedType.toString());
        final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        Stream.of(actualTypeArguments).forEach(System.out::println);
        final Type rawType = parameterizedType.getRawType();
        System.out.println("rawType=" + rawType);
        final Type ownerType = parameterizedType.getOwnerType();
        System.out.println("ownerType=" + ownerType);
        System.out.println(arrayListClass);


    }
}
