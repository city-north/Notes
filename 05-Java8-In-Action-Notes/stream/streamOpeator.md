[返回目录](/README.md)

# 流操作

流操作可以分为两类：

* 中间操作-可以连接起来的流操作称为中间操作
* 终端操作-关闭流的操作称为终端操作

![](/assets/import13.png)

```
List<String> names = menu.stream()
    .filter(d -> d.getCalories() > 300)
    .map(Dish::getName)
    .limit(3)
    .collect(toList());
```

## 中间操作

诸如 filter 或 sorted 等中间操作会返回另一个流。这让多个操作可以连接起来形成一个查询。重要的是，除非流水线上触发一个终端操作，否则中间操作不会执行任何处理——它们很懒。这是因为中间操作一般都可以合并起来，在终端操作时一次性全部处理。

## 终端操作

终端操作会从流的流水线生成结果。其结果是任何不是流的值，比如 List 、 Integer ，甚至 void 。例如，在下面的流水线中， forEach 是一个返回 void 的终端操作，它会对源中的每道菜应用一个Lambda。把 System.out.println 传递给 forEach ，并要求它打印出由 menu 生成的流中的每一个 Dish ：

```
menu.stream().forEach(System.out::println);
```

```
测验4.1：中间操作与终端操作
在下列流水线中，你能找出中间操作和终端操作吗？
long count = menu.stream()
.filter(d -> d.getCalories() > 300)
.distinct()
.limit(3)
.count();
答案：流水线中最后一个操作 count 返回一个 long ，这是一个非 Stream 的值。因此它是
一个终端操作。所有前面的操作， filter 、 distinct 、 limit ，都是连接起来的，并返回一
个 Stream ，因此它们是中间操作。
```

## 使用流

使用流一般包括三件事：

* 一个数据源（如集合）来执行一个查询

* 一个中间操作链，形成一条流的流水线

* 一个终端操作，执行流水线，并能生成结果

流的流水线背后的理念类似于构建器模式。 在构建器模式中有一个调用链用来设置一套配置（对流来说这就是一个中间操作链），接着是调用 built 方法（对流来说就是终端操作）。

为方便起见，表4-1和表4-2总结了你前面在代码例子中看到的中间流操作和终端流操作。请注意这并不能涵盖Stream API提供的操作，你在下一章中还会看到更多

![](/assets/import14.png)



