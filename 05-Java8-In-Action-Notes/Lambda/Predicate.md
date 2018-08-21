```
返回目录
```

# Predicate

java.util.function.Predicate&lt;T&gt; 接口定义了一个名叫 test 的抽象方法，它接受泛型  
T 对象，并返回一个 boolean 。这恰恰和你先前创建的一样，现在就可以直接使用了。在你需要  
表示一个涉及类型 T 的布尔表达式时，就可以使用这个接口。比如，你可以定义一个接受 String  
对象的Lambda表达式，如下所示。

在Predicate中有：

```
@FunctionalInterface
public interface Predicate<T>{
    boolean test(T t);
}
```



```
public static <T> List<T> filter(List<T> list, Predicate<T> p) {
    List<T> results = new ArrayList<>();
    for(T s: list){
        if(p.test(s)){
        results.add(s);
    }
}
    return results;
}
Predicate<String> nonEmptyStringPredicate = (String s) -> !s.isEmpty();
List<String> nonEmpty = filter(listOfStrings, nonEmptyStringPredicate);
```

[返回目录](#)

