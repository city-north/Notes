# 030-Spring泛型类型辅助类

[TOC]

## 核心API

org.springframework.core.GenericTypeResolver

- 版本支持 2.5.2
- 处理类型相关(Type)相关方法
  - resolveReturnType
  - resolveType

- 处理泛型参数类型(ResolvableType) 相关方法
  - resolveReturnTypeArgument
  - resolveTypeArgument
  - resolveTypeArguments
- 处理泛型类型变量(TypeVariable) 相关方法
  - getTypeVariableMap

## GenericTypeResolver处理类型相关(Type)相关方法

解析方法的返回值类型Class 

```java
/**
* 解析方法的返回值类型Class
*/
private static void doResolveReturnType() throws NoSuchMethodException {
  final Method getString = GenericTypeResolverDemo.class.getMethod("getString");
  final Class<?> returnType = GenericTypeResolver.resolveReturnType(getString, ArrayList.class);
  System.out.println(returnType);  //String

}
public static String getString() {
	return null;
}

```





## GenericTypeResolver处理泛型参数类型(ResolvableType) 相关方法

```java
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

```

```java
resolveReturnTypeArgument(GenericTypeResolverDemo.class.getMethod("getString"), Comparable.class);   //java.lang.String
resolveReturnTypeArgument(GenericTypeResolverDemo.class.getMethod("getArrayList"), List.class);   // class java.lang.String
resolveReturnTypeArgument(GenericTypeResolverDemo.class.getMethod("getStringList"), List.class);  // class java.lang.String
```

```java
public static String getString() {
	return null;
}

/**
* 泛型参数具体化(字节码有记录)
*/
public static ArrayList<String> getArrayList() {
	return null;
}
public static StringList getStringList() {
        return null;
}

/**
 * 泛型参数具体化(字节码有记录)
 */
static class StringList extends ArrayList<String> {

}

```



## GenericTypeResolver处理泛型类型变量(TypeVariable) 相关方法

获取到所有泛型的Class,然后获取到泛型具体类型

```
final Map<TypeVariable, Type> typeVariableMap = GenericTypeResolver.getTypeVariableMap(StringList.class);
typeVariableMap.forEach((k, v) -> {
    System.out.println("getGenericDeclaration =" + k.getGenericDeclaration() + ", k = " + k);
    System.out.println(v.getTypeName());
});
```



```
getGenericDeclaration =class java.util.AbstractCollection, k = E  
java.lang.String
getGenericDeclaration =interface java.util.List, k = E
java.lang.String
getGenericDeclaration =class java.util.AbstractList, k = E
java.lang.String
getGenericDeclaration =interface java.lang.Iterable, k = T
java.lang.String
getGenericDeclaration =class java.util.ArrayList, k = E
java.lang.String
getGenericDeclaration =interface java.util.Collection, k = E
java.lang.String
```

