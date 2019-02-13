# Reduction操作

简介中的例子：

```
double average = roster
    .stream()
    .filter(p -> p.getGender() == Person.Sex.MALE)
    .mapToInt(Person::getAge)
    .average()
    .getAsDouble();
```

JDK包含许多终端操作，例如： [`average`](https://docs.oracle.com/javase/8/docs/api/java/util/stream/IntStream.html#average--java/lang/reflect/Executable.html), [`sum`](https://docs.oracle.com/javase/8/docs/api/java/util/stream/IntStream.html#sum--), [`min`](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html#min-java.util.Comparator-), [`max`](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html#max-java.util.Comparator-),  [`count`](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html#count--)，这些操作根据一个stream的呢日用组合，这些操作称之为`Reduction`操作，`jdk`也包含通用的两个操作`reduce`和`collect`，下面章节分为以下细节：

## The Stream.reduce 方法

这个`Stream.reduce`方法是一个通用的`reduction`操作，考虑使用线面的管道方法，计算`roster`集合中，男性会员的年龄：使用`Stream.sum`操作

```java
Integer totalAge = roster
    .stream()
    .mapToInt(Person::getAge)
    .sum();
```

相应的，可以对比着`Stream.reduce`操作去计算相同的值。

```java
Integer totalAgeReduce = roster
   .stream()
   .map(Person::getAge)
   .reduce(
       0,
       (a, b) -> a + b);
```

我们可以看到，这个例子中的`reduce`方法包含两个参数：

- `identity`，如果stream中没有元素，那么该值是初始值也是默认的结果。在本例中，默认值是`0`，如果collection`roster`中没有值，那么默认结果就是0；
- `accumulator`累加器，这个累加器函数接受两个参数，第一个参数是上次执行后的累加，第二个参数是本次执行到的元素。

`reduce`方法总是会返回一个新的值。然而这个累加器每次执行完一次流程以后都会返回一个新的值。假如你想`reduce`一个包含`添加操作到`该`collection`，那么累加器每次执行到该方法是，都会创建一个新的`collection`，这样的做法效率很低，想要效率高，可以使用Stream.collect方法：

## Stream.collect方法

不像上面介绍的`reduce`方法，`每次执行到一个元素时，创建一个新的Collection）`，`collect`方法修改，或者改变一个已经存在的值。

当我们想要查询steam中的值的平均值时，这时需要值的总数和值的总和。和`reduce`方法一样，`collect`方法也只返回一个值，我们可以创建一个类，用来存放这两个值:

```java
class Averager implements IntConsumer
{
    private int total = 0;
    private int count = 0;
        
    public double average() {
        return count > 0 ? ((double) total)/count : 0;
    }
        
    public void accept(int i) { total += i; count++; }
    public void combine(Averager other) {
        total += other.total;
        count += other.count;
    }
}
```

下面的方法可以打印出roster集合中的所有



```
Averager averageCollect = roster.stream()
    .filter(p -> p.getGender() == Person.Sex.MALE)
    .map(Person::getAge)
    .collect(Averager::new, Averager::accept, Averager::combine);
                   
System.out.println("Average age of male members: " +
    averageCollect.average());
```

```
<R, A> R collect(Collector<? super T, A, R> collector);
```

```
<R> R collect(Supplier<R> supplier,
              BiConsumer<R, ? super T> accumulator,
              BiConsumer<R, R> combiner);
```

`collect`方法包含三个形参：

- `supplier`:工厂函数，它为`collect`方法，构造了一个新的实例，代表目标理性实例的方法，在本例中`Averager`	类。
- `accumulator`:**无返回值**累加器函数合并一个stream的元素到一个指定`container`,在本例中，它修改了`Averager`的`count`值，然后累加stream中的元素，修改了`Averager`类中的total值
- `combiner`:**无返回值**，是将多个supplier生产的实例整合到一起的方法，代表规约操作，将多个结果合并。



注意：

- `supplier`是一个lambda表达式，或者一个方法引用
- `accumulator`，和`combiner`无返回值
- 可以在`combiner`中使用并行操作。如果你使用了并行操作，那么JDK会在`combiner`方法创建对象时，自动生成一个新的Thread，所以不需要担心线程安全问题。



collect操作完美适用于Collections,下面的例子收集所有男性成员的名字，然后生成为List.

```
List<String> namesOfMaleMembersCollect = roster
    .stream()
    .filter(p -> p.getGender() == Person.Sex.MALE)
    .map(p -> p.getName())
    .collect(Collectors.toList());
```

### Collectors.toList

`Collectors`包含许多有用的reductoion操作，如toList,返回一个	`Collectors`的实例：

```
List<String> namesOfMaleMembersCollect = roster
    .stream()
    .filter(p -> p.getGender() == Person.Sex.MALE)
    .map(p -> p.getName())
    .collect(Collectors.toList());
```

`groupBy`方法返回一个map,key是lambda表达式的返回值，在下面的例子里，map包含两个key：Person.Sex.MALE和Person.Sex.FEMALE，每个Key对应各个性别的会员列表

```
Map<Person.Sex, List<Person>> byGender =
    roster
        .stream()
        .collect(
            Collectors.groupingBy(Person::getGender));
```



每个会员根据gender分类：

`groupingBy`操作包含两个参数，一个是分类函数还有一个是一个`Collector`的实例，Collector参数也叫下游Collector(*downstream collector*)

```
Map<Person.Sex, List<String>> namesByGender =
    roster
        .stream()
        .collect(
            Collectors.groupingBy(
                Person::getGender,                      
                Collectors.mapping(
                    Person::getName,
                    Collectors.toList())));
```

每种性别的总年龄：

```
Map<Person.Sex, Integer> totalAgeByGender =
    roster
        .stream()
        .collect(
            Collectors.groupingBy(
                Person::getGender,                      
                Collectors.reducing(
                    0,
                    Person::getAge,
                    Integer::sum)));
```

每种性别的平均年龄

```
Map<Person.Sex, Double> averageAgeByGender = roster
    .stream()
    .collect(
        Collectors.groupingBy(
            Person::getGender,                      
            Collectors.averagingInt(Person::getAge)));
```