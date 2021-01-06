package cn.eccto.study.springframework.generic;

import org.springframework.core.GenericTypeResolver;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author EricChen 2021/01/06 18:59
 */
public class GenericTypeResolverDemo {

    public static void main(String[] args) throws Exception {

//        doResolveReturnType();
//        doResolveType();

//        resolveReturnTypeArgument(GenericTypeResolverDemo.class.getMethod("getString"), Comparable.class);   //java.lang.String
//        resolveReturnTypeArgument(GenericTypeResolverDemo.class.getMethod("getArrayList"), List.class);   // class java.lang.String
//        resolveReturnTypeArgument(GenericTypeResolverDemo.class.getMethod("getStringList"), List.class);  // class java.lang.String


        final Map<TypeVariable, Type> typeVariableMap = GenericTypeResolver.getTypeVariableMap(StringList.class);
        typeVariableMap.forEach((k, v) -> {
            System.out.println("getGenericDeclaration =" + k.getGenericDeclaration() + ", k = " + k);
            System.out.println(v.getTypeName());
        });
    }

    private static void doResolveType() throws NoSuchMethodException {
        final Type genericSuperclass = ArrayList.class.getGenericSuperclass();
        System.out.println(genericSuperclass); //java.util.AbstractList<E>
        final Type type = GenericTypeResolver.resolveType(genericSuperclass, StringList.class);
        System.out.println(type);       //java.util.AbstractList<java.lang.String>
    }

    /**
     * 解析方法返回值的参数类型
     *
     * @param method     方法
     * @param genericIfc 需要解析的类型
     */
    private static void resolveReturnTypeArgument(Method method, Class<?> genericIfc) {
        final Class<?> returnType = GenericTypeResolver.resolveReturnTypeArgument(method, genericIfc);
        //常规类型不具备泛型参数类型,也就是String
        System.out.println(returnType);
    }

    /**
     * 解析方法的返回值类型Class
     */
    private static void doResolveReturnType() throws NoSuchMethodException {
        final Method getString = GenericTypeResolverDemo.class.getMethod("getString");
        final Class<?> returnType = GenericTypeResolver.resolveReturnType(getString, ArrayList.class);
        System.out.println(returnType);

    }


    public static String getString() {
        return null;
    }

    public static <E> List<E> getList() {
        return null;
    }


    public static StringList getStringList() {
        return null;
    }


    /**
     * 泛型参数具体化(字节码有记录)
     */
    public static ArrayList<String> getArrayList() {
        return null;
    }


    /**
     * 泛型参数具体化(字节码有记录)
     */
    static class StringList extends ArrayList<String> {

    }

}
