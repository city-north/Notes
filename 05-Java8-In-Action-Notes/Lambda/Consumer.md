[返回目录](/README.md)

# Consumer

java.util.function.Consumer&lt;T&gt; 定义了一个名叫 accept 的抽象方法，它接受泛型 T的对象，没有返回（ void ）。

你如果需要访问类型 T 的对象，并对其执行某些操作，就可以使用这个接口。

比如，你可以用它来创建一个 forEach 方法，接受一个 Integers 的列表，并对其中每个元素执行操作。在下面的代码中，你就可以使用这个 forEach 方法，并配合Lambda来打印列表中的所有元素



```
public class ConsumerTest {
    public static void main(String[] args) {
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5);
        forEach(integers,(Integer i) -> System.out.println(i));
    }

    public static <T> void forEach(List<T> list, Consumer<T> c){
        for (T i : list){
            Integer i1 = (Integer) i;
            if (i1 % 2 == 0)
                c.accept(i);
        }
    }
}

```

[返回目录](#)

