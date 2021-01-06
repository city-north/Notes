package cn.eccto.study.java.generic;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2021/01/05 20:34
 */
public class GenericTest {


    public static <T> Pair<T> makerArrayList(Class<T> c) throws IllegalAccessException, InstantiationException {
        return new Pair<>(c.newInstance(), c.newInstance());
    }


    public static class Pair<T> {


        private T left;
        private T right;

        public Pair(T left, T right) {
            this.left = left;
            this.right = right;
        }


        public static <T> Pair<T> makePair(Supplier<T> constr) {
            return new Pair<>(constr.get(), constr.get());
        }

        public static <T> Pair<T> makePair(Class<T> cl) throws IllegalAccessException, InstantiationException {
            return new Pair<>(cl.newInstance(), cl.newInstance());
        }
    }


    public static <T> void addAll(Collection<T> collection, T... ts) {
        for (T t : ts) {
            collection.add(t);
        }
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        //原生类型 primitive types
        final Class<Integer> integerClass = int.class;

        //数组类型 array types
        final Class<Object[]> aClass = Object[].class;

        //原生类型 raw types
        final Class<String> stringClass = String.class;

        //泛型类型
        final ParameterizedType genericSuperclass = (ParameterizedType)ArrayList.class.getGenericSuperclass();
        //泛型类型变量

        final Type rawType = genericSuperclass.getRawType();
        Arrays.stream(genericSuperclass.getActualTypeArguments()).forEach(System.out::println);

        System.out.println(integerClass);

//        Collection<Pair<String>> table = new ArrayList<>();
//        Pair<String> par1 = new Pair<>("1", "2");
//        Pair<String> par2 = new Pair<>("1", "2");
//
//        addAll(table, par1, par2);

    }


    @SuppressWarnings("unchecked")
    public static <T extends Throwable> void throwAs(Throwable e) throws T {
        throw (T) e;
    }
}
