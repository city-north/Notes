[返回目录](/README.md)

# 流（Stream）

## 流引入

流是Java API的新成员，它允许你以**声明性方式处理数据集合**（通过查询语句，而不是临时编写一个实现）。

可以把流看成遍历数据集的高级迭代器。

流可以透明地并行处理，不用再写多线程代码了。

## 例子

需求：返回集合中低热量的菜肴名称，按照卡路里排序

基础数据：

```
List<Dish> menu = Arrays.asList(
new Dish("pork", false, 800, Dish.Type.MEAT),
new Dish("beef", false, 700, Dish.Type.MEAT),
new Dish("chicken", false, 400, Dish.Type.MEAT),
new Dish("french fries", true, 530, Dish.Type.OTHER),
new Dish("rice", true, 350, Dish.Type.OTHER),
new Dish("season fruit", true, 120, Dish.Type.OTHER),
new Dish("pizza", true, 550, Dish.Type.OTHER),
new Dish("prawns", false, 300, Dish.Type.FISH),
new Dish("salmon", false, 450, Dish.Type.FISH) )
```

Dish类的定义

```
public class Dish {
    private final String name;
    private final boolean vegetarian;
    private final int calories;
    private final Type type;
    public Dish(String name, boolean vegetarian, int calories, Type type) {
    this.name = name;
    this.vegetarian = vegetarian;
    this.calories = calories;
    this.type = type;
}
public String getName() {
    return name;
}
public boolean isVegetarian() {
    return vegetarian;
}
public int getCalories() {
    return calories;
}
public Type getType() {
    return type;
}
@Override
public String toString() {
    return name;
}
72 第 4章 引入流
public enum Type { MEAT, FISH, OTHER }
}
```

Java7:

```
List<Dish> lowCaloricDishes = new ArrayList<>();
for(Dish d: menu){
    if(d.getCalories() < 400){
        lowCaloricDishes.add(d);
    }
}
Collections.sort(lowCaloricDishes, new Comparator<Dish>() {
    public int compare(Dish d1, Dish d2){
        return Integer.compare(d1.getCalories(), d2.getCalories());
        }
});
List<String> lowCaloricDishesName = new ArrayList<>();
for(Dish d: lowCaloricDishes){
    lowCaloricDishesName.add(d.getName());
}
```

在这段代码中，你用了一个“垃圾变量” lowCaloricDishes 。它唯一的作用就是作为一次  
性的中间容器。在Java 8中，实现的细节被放在它本该归属的库里了。

Java 8:

```
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
List<String> lowCaloricDishesName =menu.stream()
                //选出400卡路里以下的菜肴
                .filter(d -> d.getCalories() < 400)
                //按照卡路里排序 
                .sorted(comparing(Dish::getCalories))
                //提取菜肴名称
                .map(Dish::getName)
                //将所有名称保存在List中
                .collect(toList());
```

为了利用多核架构并行执行这段代码，你只需要把 stream\(\) 换成 parallelStream\(\) ：

```
List<String> lowCaloricDishesName =menu.parallelStream()
    .filter(d -> d.getCalories() < 400)
    .sorted(comparing(Dishes::getCalories))
    .map(Dish::getName)
    .collect(toList());
```

## 优势

* **代码是以声明性方式写的**：说明想要完成什么（筛选热量低的菜肴）而不是说明如何实现。这种方法加上行为参数化让你可以倾诉应对变化的需求：你可以很容易再创建一个代码版本，利用Lambda表达式来筛选高卡路里的菜肴，不用复制粘贴了。
* **可以把几个基本操作连接起来，来表达复杂的数据处理流水线**（在Filter后面接上sorted,map,collect操作）同时保持代码清晰，filter的结果被传给了sorted方法，再传给map和collect方法。

![](/assets/import09.png)

因为 filter 、 sorted 、 map 和 collect 等操作是与具体线程模型无关的**高层次构件**，所以  
它们的内部实现可以是单线程的，也可能透明地充分利用你的多核架构！在实践中，这意味着你  
用不着为了让某些数据处理任务并行而去操心线程和锁了，Stream API都替你做好了

## 总结

* **声明性**——更简洁，更易读
* **可复合**——更灵活
* **可并行**——性能更好

## 流简介

Java8中的集合（Collection）中支持了一个新的方法，stream方法，它返回一个流。

![](/assets/import10.png)流：“**从支持数据处理操作的源生成的元素序列**”。

* **元素序列**——就像集合一样，流也提供了一个接口，可以访问特定元素类型的一组有序值。集合是数据结构，它的目的是以特定的时间/空间复杂度存储和访问元素（ArrayList与LinkedList），但是流的目的在于**表达计算**。
* **源——**流会使用一个提供数据的源，如集合，数组或者输入输出资源。请注意，从有序集合生成流时会保留原有的顺序。由列表生成的流，其元素与列表一致。
* **数据处理操作——流的数据处理功能支持类似于数据库的操作，以及函数是编程中常用的操作。**如filter/map/reduce/find/match/sort。

流的特点：

* 流水线——很多流操作本身会返回一个流，这样多个操作就可以连接起来，形成一个大的流水线。
* 内部迭代——与使用迭代器显式迭代的集合不同，流的迭代操作是在背后进行的。

```
import static java.util.stream.Collectors.toList;
List<String> threeHighCaloricDishNames =
menu.stream() //从menu中获得流
//建立操作流水线，首先选出高热量的菜肴
    .filter(d -> d.getCalories() > 300)
//获取菜名
    .map(Dish::getName)
//只选择头三个
    .limit(3)
//将结果保存在另一个List中
    .collect(toList());
System.out.println(threeHighCaloricDishNames);
```

![](/assets/impor11t.png)

* **filter——接受Lambda，从流中排除某些元素。**本例中，通过传递lamdba d -&gt; d,getCalories\(\)&gt;300,选择出热量超过300卡路里的菜肴。
* map**——接受一个Lambda，将元素转换成其他形式或提取信息。**在本例中：通过传递方法引用Dish::getName，相当于Lambda d -&gt; d.getName，提供每道菜的菜名。
* **limit——截断流，使其元素不超过给定数量**
* collect——将流转化为其他形式。

[返回目录](/README.md)

