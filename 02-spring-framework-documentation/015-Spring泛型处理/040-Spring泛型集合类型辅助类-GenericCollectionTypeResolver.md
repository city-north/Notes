# 040-Spring泛型集合类型辅助类

[TOC]

## 简介

GenericCollectionTypeResolver用于处理集合类型的解析,5.0以后被移除了,使用org.springframework.core.ResolvableType替换

## 核心API-org.springframe.core.GenericCollectionTypeResolver

5.0以后被移除了

- 版本支持:[2.0,4.3]
- 替换实现:org.springframework.core.ResolvableType
- 处理Collection相关
  - getCollection*Type
- 处理Map相关
  - getMapKey*Type
  - getMapValue*Type

## 使用实例

```java
public class GenericCollectionTypeResolverDemo {

    private static StringList stringList;

    private static List<String> strings;

    public static void main(String[] args) throws Exception {

        // StringList extends ArrayList<String> 具体化
        // getCollectionType 返回具体化泛型参数类型集合的成员类型 = String
        System.out.println(GenericCollectionTypeResolver.getCollectionType(StringList.class));

        System.out.println(GenericCollectionTypeResolver.getCollectionType(ArrayList.class));

        // 获取字段
        Field field = GenericCollectionTypeResolverDemo.class.getDeclaredField("stringList");
        System.out.println(GenericCollectionTypeResolver.getCollectionFieldType(field));

        field = GenericCollectionTypeResolverDemo.class.getDeclaredField("strings");
        System.out.println(GenericCollectionTypeResolver.getCollectionFieldType(field));
    }
}
class java.lang.String
null
class java.lang.String
class java.lang.String
```

