[返回目录](/README.md)

# Function

java.util.function.Function&lt;T, R&gt; 接口定义了一个叫作 apply 的方法，它接受一个泛型 T 的对象，并返回一个泛型 R 的对象。

如果你需要定义一个Lambda，将输入对象的信息映射到输出，就可以使用这个接口（比如提取苹果的重量，或把字符串映射为它的长度）。

在下面的代码中，我们向你展示如何利用它来创建一个 map 方法，以将一个 String 列表映射到包含每个String 长度的 Integer 列表。

```
@FunctionalInterface
public interface Function<T, R>{
    R apply(T t);
}
```

```
public class FunctionTest {

    public static <T,R>List<R> map (List<T> list, Function<T,R> func){
        List<R> result = new ArrayList<>();
        for (T s :list){
            result.add( func.apply(s));
        }
        return result;
    }

    public static void main(String[] args) {
        List<String> strings = Arrays.asList("lambdas", "in", "action");
        List<Integer> map = map(strings, String::length);
        System.out.println(map);
    }
}
```

[返回目录](#)

