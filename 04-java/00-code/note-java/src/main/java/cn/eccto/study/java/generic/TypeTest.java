package cn.eccto.study.java.generic;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * <p>
 * description
 * </p>
 *
 * @author Jonathan 2021/01/06 17:01
 */
public class TypeTest<T> {
    List<String> list1;
    List list2;
    Map<String, Long> map1;
    Map map2;
    public Map.Entry<Long, Short> map3;
    Map<List<String>, List<Long>> map4;

    protected T[] obj;

    public static void main(String[] args) {
//        printFields();
        isParameterizedType();

//        isGenericArrayType(new TypeTest<String>());

    }

    public static void isGenericArrayType(Object object) {
        Field[] fields = new TypeTest2().getClass().getDeclaredFields();
        for (Field f : fields) {
            boolean isParameterizedType = false;
            if (f.getGenericType() instanceof GenericArrayType) {
                isParameterizedType = true;
            }
            //是否是ParameterizedType
            System.out.printf("filedName : %s , 是否是泛型数组 :%s \n", f.getName(), isParameterizedType);
            if (isParameterizedType) {
                final GenericArrayType genericType = (GenericArrayType) f.getGenericType();
                final Type genericComponentType = genericType.getGenericComponentType();
                System.out.println(genericComponentType);
            }

        }
    }

    private static void isParameterizedType() {
        Field[] fields = TypeTest.class.getDeclaredFields();
        for (Field f : fields) {
            boolean isParameterizedType = false;
            if (f.getGenericType() instanceof ParameterizedType) {
                isParameterizedType = true;
            }
            //是否是ParameterizedType
            System.out.printf("filedName : %s , 是否是泛型参数 :%s \n", f.getName(), isParameterizedType);
            if (isParameterizedType) {
                final ParameterizedType genericType = (ParameterizedType) f.getGenericType();
                final String typeName = genericType.getTypeName();
                final Type ownerType = genericType.getOwnerType();
                final Type rawType = genericType.getRawType();
                final Type[] actualTypeArguments = genericType.getActualTypeArguments();
                Stream.of(actualTypeArguments).forEach(System.out::println);
                System.out.printf("typeName = [%s] ,ownerType= [%s] , rawType= [%s] \n", typeName, ownerType, rawType);
            }
        }
    }

    private static void printFields() {
        final Field[] declaredFields = TypeTest.class.getDeclaredFields();
        final Field[] fields = TypeTest.class.getFields();
        Stream.of(declaredFields).forEach(System.out::println);
        System.out.println("-");
        Stream.of(fields).forEach(System.out::println);
    }
}
