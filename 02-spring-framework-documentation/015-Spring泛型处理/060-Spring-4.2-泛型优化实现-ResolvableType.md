# 060-Spring-4.2-泛型优化实现-ResolvableType

[TOC]

## 一言蔽之

ResolvableType是一个工具类,可以获取到一个类的的泛型类型,屏蔽了复杂的反射API

- 是GenericTypeResolver的替代者

- 是GenericCollectionTypeResolver的替代者

## 核心API

org.springframework.core.ResolvableType

- 起始版本4.0
- 扮演角色
  - GenericTypeResolver 替代者
  - GenericCollectionTypeResolver 替代者

- 工厂方法 for* 方法
- 转换方法 as* 方法
- 处理方法 resolve* 方法

## 示例

```java
public class ResolvableTypeDemo {

    public static void main(String[] args) {
        // 工厂创建
        // StringList <- ArrayList <- AbstractList <- List <- Collection
        ResolvableType resolvableType = ResolvableType.forClass(StringList.class);

        resolvableType.getSuperType(); // ArrayList
        resolvableType.getSuperType().getSuperType(); // AbstractList

        System.out.println(resolvableType.asCollection().resolve()); // 获取 Raw Type
        System.out.println(resolvableType.asCollection().resolveGeneric(0)); // 获取泛型参数类型


    }
  
private HashMap<Integer, List<String>> myMap;
  
   public void example() {
       ResolvableType t = ResolvableType.forField(getClass().getDeclaredField("myMap"));
       t.getSuperType(); // AbstractMap<Integer, List<String>>
       t.asMap(); // Map<Integer, List<String>>
       t.getGeneric(0).resolve(); // Integer
       t.getGeneric(1).resolve(); // List
       t.getGeneric(1); // List<String>
       t.resolveGeneric(1, 0); // String
   }
}
```



## ResolvableType的局限性

- 局限一:ReslovableType无法处理泛型擦写

- 局限二:ResolvableType无法处理非具体化的ParameterizedType

