[返回目录](/README.md)

# 使用流

* 筛选、切片和匹配
* 查找、匹配和归约
* 使用数值范围等数值流
* 从多个源创建流
* 无限流

在上一章中你已看到了，流让你从**外部迭代**转向**内部迭代**。 这样，你就用不着写下面这样的代码来显式地管理数据集合的迭代（外部迭代）了：

```
List<Dish> vegetarianDishes = new ArrayList<>();
    for(Dish d: menu){
        if(d.isVegetarian()){
        vegetarianDishes.add(d);
    }
}
```

你可以使用支持 filter 和 collect 操作的Stream API（内部迭代）管理对集合数据的迭代。你只需要将筛选行为作为参数传递给 filter 方法就行了。

```
import static java.util.stream.Collectors.toList;
List<Dish> vegetarianDishes =
                menu.stream()
                .filter(Dish::isVegetarian)
                .collect(toList());
```

这种处理数据的方式很有用，因为你让Stream API管理如何处理数据。这样Stream API就可以在背后进行多种优化。此外，使用内部迭代的话，Stream API可以决定并行运行你的代码。这要是用外部迭代的话就办不到了，因为你只能用单一线程挨个迭代。

在本章中，你将会看到Stream API支持的许多操作。这些操作能让你快速完成复杂的数据查询，如筛选、切片、映射、查找、匹配和归约。接下来，我们会看看一些特殊的流：数值流、来自文件和数组等多种来源的流，最后是无限流。

[返回目录](/README.md)

