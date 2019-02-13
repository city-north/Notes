# Parallelism 并行性

	并行计算是将一个大的问题拆分成若干个小的问题，并且同时进行计算（并行地，每一个单独的子问题会被放到一个独立的线程中运行），最后，将各个子问题的结果合并得到问题的结果。
	
	要想实现并行计算，Java SE提供了 [fork / join框架](https://docs.oracle.com/javase/tutorial/essential/concurrency/forkjoin.html)，使您可以更轻松地在应用程序中实现并行计算。但是，使用此框架，您必须指定问题如何细分（分区）。通过聚合操作，Java运行时为您执行此拆分和组合，屏蔽了复杂的实现过程。
	
	在使用集合的应用程序中实现并行性的难点是，集合不一定是线程安全的集合。这就意味着多个线程同时操作集合有可能会出现线程安全问题（线程干扰或者内存一致性相关的操作）。**通过使用聚合操作的并行操作，可以安全的操作非-线程安全集合**，前提是在进行操作集合时，不要进行修改。
	
	请注意，并行性不会比串行执行操作更快，尽管可能有足够的数据和处理器内核。主要消耗在于多个线程在CPU执行时的上下文切换。

## 并行地执行Stream

以下语句并行计算所有男性成员的平均年龄：

使用`parallelStream()`方法来创建并行流。

```
double average = roster
    .parallelStream()
    .filter(p -> p.getGender() == Person.Sex.MALE)
    .mapToInt(Person::getAge)
    .average()
    .getAsDouble();
```



下面的示例 按性别对成员进行分组。调用`collect`操作，该操作将集合reduction`roster`为`Map`：

```
Map<Person.Sex, List<Person>> byGender =
    roster
        .stream()
        .collect(
            Collectors.groupingBy(Person::getGender));
```

那么使用并行：

```
ConcurrentMap<Person.Sex, List<Person>> byGender =
    roster
        .parallelStream()
        .collect(
            Collectors.groupingByConcurrent(Person::getGender));
```

这叫做并行reduction,如果对包含collect操作的特定管道满足以下所有的条件，那么Java运行时将执行并发reduction:

- 首先这个Stream是并行的
- 操作的参数`collect`，即收集器，具有特征[`Collector.Characteristics.CONCURRENT`](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Collector.Characteristics.html#CONCURRENT)。要确定收集器的特征，请调用该 [`Collector.characteristics`](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Collector.Characteristics.html)方法。

- 流是无序的，或者收集器具有特征[`Collector.Characteristics.UNORDERED`](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Collector.Characteristics.html#UNORDERED)。要确保流是无序的，请调用该 [`BaseStream.unordered`](https://docs.oracle.com/javase/8/docs/api/java/util/stream/BaseStream.html#unordered--)操作。

值得注意的是，这个实例返回一个`ConcurrentMap`，调用的是`groupingByConcurrent`操作而不是`groupingBy`操作，`groupingBy`执行并行流的时候效果不佳（因为它在底层实现时，根据key融合两个map，这种方式效率较低）,类似的，[`Collectors.toConcurrentMap`](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Collectors.html#toConcurrentMap-java.util.function.Function-java.util.function.Function-)在并行Stream的处理中，效率上会优于 [`Collectors.toMap`](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Collectors.html#toMap-java.util.function.Function-java.util.function.Function-).

## 排序（Ordering）

管道处理流的元素的顺序取决于流是以串行还是并行方式执行、Stream的源，以及中间操作，下面的例子通过`forEach`操作打印了`ArrayList`的元素



```
Integer[] intArray = {1, 2, 3, 4, 5, 6, 7, 8 };
List<Integer> listOfIntegers =
    new ArrayList<>(Arrays.asList(intArray));

//打印了listOfIntegers原有的顺序
System.out.println("listOfIntegers:");
listOfIntegers
    .stream()
    .forEach(e -> System.out.print(e + " "));
System.out.println("");

//打印了listOfIntegers，根据 Collections.sort方法排序
System.out.println("listOfIntegers sorted in reverse order:");
Comparator<Integer> normal = Integer::compare;
Comparator<Integer> reversed = normal.reversed(); 
Collections.sort(listOfIntegers, reversed);  
listOfIntegers
    .stream()
    .forEach(e -> System.out.print(e + " "));
System.out.println("");
     
//第三个和第四个以明显随机的顺序打印了元素，请记住，流处理在处理流的元素时使
//用内部迭代。因此，当您并行执行流时，Java编译器和运行时确定处理流元素的顺
//序，以最大化并行计算的优势，除非流操作另有指定。
System.out.println("Parallel stream");
listOfIntegers
    .parallelStream()
    .forEach(e -> System.out.print(e + " "));
System.out.println("");
    
System.out.println("Another parallel stream:");
listOfIntegers
    .parallelStream()
    .forEach(e -> System.out.print(e + " "));
System.out.println("");
     
//第五个使用管道方法forEachOrdered，该方法按照源指定的顺序处理流的元素，
//无论是串行方式还是并行方式执行，如果在并行情况下，使用forEachOrdered可能
//会失去并行性的好处

System.out.println("With forEachOrdered:");
listOfIntegers
    .parallelStream()
    .forEachOrdered(e -> System.out.print(e + " "));
System.out.println("");
```

输出结果：

```
listOfIntegers:
1 2 3 4 5 6 7 8
listOfIntegers sorted in reverse order:
8 7 6 5 4 3 2 1
Parallel stream:
3 4 1 6 2 5 7 8
Another parallel stream:
6 3 1 5 7 8 4 2
With forEachOrdered:
8 7 6 5 4 3 2 1
```

## 副作用

一些方法或者表达式除了返回一个值，还修改了计算机的状态。

例如使用`collect`操作时,同时又调用了`System.out.println`方法进行debug。

JDK可以很好地处理管道中的某些副作用。特别是collect方法旨在以并行安全的方式执行具有副作用的最常见流操作。像`forEach`和`peek`这样的操作是为副作用设计的，比如以下操作：一个返回`void`的lambda表达式，调用了`System.out.println`，仅仅是为了调试，其实什么都没有做但是具有副作用。

即使专门为这种操作进行了设计，依然小心使用`forEach`和`peek`方法，如果你在并行流使用了该操作，那么Java运行时可能从多个线程并发地调用指定为其参数的lambda表达式

另外，不要吧Lambda表达式当做参数传递，这样会有很多副作用，例如`filter`和`map`操作，下面讨论的是 [interference](https://docs.oracle.com/javase/tutorial/collections/streams/parallelism.html#interference) and [stateful lambda expressions](https://docs.oracle.com/javase/tutorial/collections/streams/parallelism.html#stateful_lambda_expressions) 这两个可能是产生副作用的源，尤其是在使用并行操作时，但是在研究这两个之前，要先看一下**懒惰性**， 因为它直接影响到`interference`

## 懒惰性

所有的中间操作都是惰性的，它可以是一个表达式、一个方法或者算法（当它的值只有在需要的时候才去计算）（如果一个算法立刻被执行，那么它是及时性的）。

中间操作是**惰性**的，因为它们直到终端操作开始时才开始处理流的内容。

延迟处理流使Java编译器和运行时能够优化它们处理流的方式。比如，在调用`filter`-`mapToInt`-`average`时平均操作可以从mapToInt操作创建的流中获得前几个整数，mapToInt操作从筛选器操作中获取元素。平均值操作将重复这个过程，直到它从流中获得所有需要的元素，然后计算平均值。

## 干扰（Interference）

在Stream操作中的Lambda表达式不应该被干扰，干扰是在管道正在运行Stream时，修改源的值，

例如，下面的代码尝试连接列表listOfStrings中包含的字符串，但是抛出了 `ConcurrentModificationException`:

```
try {
    List<String> listOfStrings =
        new ArrayList<>(Arrays.asList("one", "two"));
         
    // This will fail as the peek operation will attempt to add the
    // string "three" to the source after the terminal operation has
    // commenced. 
             
    String concatenatedString = listOfStrings
        .stream()
        
        // Don't do this! Interference occurs here.
        .peek(s -> listOfStrings.add("three"))
        
        .reduce((a, b) -> a + " " + b)
        .get();
                 
    System.out.println("Concatenated string: " + concatenatedString);
         
} catch (Exception e) {
    System.out.println("Exception caught: " + e.toString());
}
```

上面的例子使用一个reduce操作将`listOfStrings` 中包含的String连接到一个Optional<String>，这是一个中断操作，本例中调用了中间操作`peek`，然后想网listOfStrings中添加新的元素

记住：

**所有的中间操作都是惰性操作**这意味着本例在执行`get`操作时调用，在`get`完成时结束，peek方法的操作视图在此过程中国改变Stream的源，所以抛出异常。、

## 有状态的Lambda表达式（Stateful Lambda Expressions）



避免在流操作中使用有状态lambda表达式作为参数

有状态lambda表达式的结果取决于管道执行过程中可能更改的任何状态。

下面的示例使用map中间操作将列表listOfIntegers中的元素添加到一个新的列表实例中。它这样做了两次，第一次用串行流，然后用并行流:

```
List<Integer> serialStorage = new ArrayList<>();
     
System.out.println("Serial stream:");
listOfIntegers
    .stream()
    
    // Don't do this! It uses a stateful lambda expression.
    .map(e -> { serialStorage.add(e); return e; })
    
    .forEachOrdered(e -> System.out.print(e + " "));
System.out.println("");
     
serialStorage
    .stream()
    .forEachOrdered(e -> System.out.print(e + " "));
System.out.println("");

System.out.println("Parallel stream:");
List<Integer> parallelStorage = Collections.synchronizedList(
    new ArrayList<>());
listOfIntegers
    .parallelStream()
    
    // Don't do this! It uses a stateful lambda expression.
    .map(e -> { parallelStorage.add(e); return e; })
    
    .forEachOrdered(e -> System.out.print(e + " "));
System.out.println("");
     
parallelStorage
    .stream()
    .forEachOrdered(e -> System.out.print(e + " "));
System.out.println("");
```

lambda 表达式 `e -> { parallelStorage.add(e); return e; }` 是一个有状态Lambda表达式. 代码每次运行都会返回结果，打印结果如下：

```
Serial stream:
8 7 6 5 4 3 2 1
8 7 6 5 4 3 2 1
Parallel stream:
8 7 6 5 4 3 2 1
1 3 6 2 4 5 8 7
```

`forEachOrdered`操作按照流指定的顺序处理元素，而不管流是串行执行还是并行执行。但是，当并行执行流时，map操作处理Java运行时和编译器指定的流元素。因此，lambda表达式`e -> { parallelStorage.add(e); return e; }` 的顺序添加到 `List``parallelStorage` 在每次代码执行时都会发生变化，

请确保流操作中的lambda表达式参数没有状态。



### 注意

这个例子使用了[`synchronizedList`](https://docs.oracle.com/javase/8/docs/api/java/util/Collections.html#synchronizedList-java.util.List-)方法， `List``parallelStorage`是线程安全的

记住如果集合是线程非安全的，那么这意味着多个线程不能再同一个时间访问特定的集合，假设你在创建`parallelStorage`时调用方法`synchronizedList `

```
List<Integer> parallelStorage = new ArrayList<>();
```

那么本例就会变得不规律，因为多个线程访问并且修改了parallelStorage，没有任何同步机制去规划List实例的访问，因此，本例子会输出：

```
Parallel stream:
8 7 6 5 4 3 2 1
null 3 5 4 7 8 1 2
```