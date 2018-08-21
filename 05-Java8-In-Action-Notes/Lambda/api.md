[返回目录](/README.md)

# Java 8 函数式接口

[转载](http://www.runoob.com/java/java8-functional-interfaces.html)

函数式接口（Functional Interface）就是一个**有且仅有一个**抽象方法，但是可以由**多个**非抽象方法的接口。

原则上，SAM类型的接口（Single Abstract Method），只能有一个函数可以实现，但是有以下例外：

* 默认方法与静态方法不影响函数式接口，所以函数式接口可以有一个或者多个静态方法。
* Java8为了使得一些接口，原则上只能有一个方法被实现，但是由于历史原因不得不加入一些方法来兼容整个JDK中的API,所以就需要使用default关键字来定义这些方法。
* 可以有Object中覆盖的方法，也就是equals,toString,hashcode方法等

函数式接口，可以被隐式转化为Lamada表达式。

函数式接口，可以现有的函数友好地支持Lamada。

## Java8之前的函数式接口

| 序号 | 接口 |
| :--- | :--- |
| 1 | java.lang.Runnable |
| 2 | java.util.concurrent.Callable |
| 3 | java.security.PrivilegedAction |
| 4 | java.util.Comparator |
| 5 | java.io.FileFilter |
| 6 | java.nio.file,PathMatcher |
| 7 | java.lang.reflect.InvocationHandler |
| 8 | java.beans.PropertyChangeListener |
| 9 | java.awt.event.ActionListener |
| 10 | java.swing.eventChangeListener |

# Function包常用的函数

| 类 | 描述 |
| :--- | :--- |
| Consumer&lt;T&gt; | 接受T对象，不返回 |
| Predicate&lt;T&gt; | 接受T对象并返回boolean |
| Function&lt;T,R&gt; | 接受T对象，返回R对象 |
| Supplier&lt;T&gt; | 提供T对象，例如工厂，不接收值 |
| UnaryOperator | 不接收T对象，返回T对象 |
| BinaryOperator | 接受两个T对象，返回T对象 |

该注解只能标记在”有且仅有一个抽象方法”的接口上。

* JDK8接口中的静态方法和默认方法，都不算是抽象方法。
* 接口默认继承java.lang.Object，所以如果接口显示声明覆盖了Object中方法，那么也不算抽象方法。
* 该注解不是必须的，如果一个接口符合”函数式接口”定义，那么加不加该注解都没有影响。加上该注解能够更好地让编译器进行检查。如果编写的不是函数式接口，但是加上了@FunctionInterface，那么编译器会报错。
* 在一个接口中定义两个自定义的方法，就会产生Invalid ‘@FunctionalInterface’ annotation; FunctionalInterfaceTest is not a functional interface错误.





# Java8新增的函数接口

java.util.function

| 序号 | 接口 | 描述 |
| :--- | :--- | :--- |
| 1 | BiConsumer&lt;T,U&gt; | 代表了一个接受两个输入参数的操作，并且不返回任何结果 |
| 2 | BiFunction&lt;T,U,R&gt; | 代表了一个接受两个输入参数的方法，并且返回一个结果 |
| 3 | BinaryOperator&lt;T&gt; | 代表了一个作用于于两个同类型操作符的操作，并且返回了操作符同类型的结果 |
| 4 | BiPredicate&lt;T,U&gt; | 代表了一个两个参数的boolean值方法 |
| 5 | BooleanSupplier | 代表了boolean值结果的提供方 |
| 6 | Consumer&lt;T&gt; | 代表了接受一个输入参数并且无返回的操作 |
| 7 | DoubleBinaryOperator | 代表了作用于两个double值操作符的操作，并且返回了一个double值的结果。 |
| 8 | DoubleConsumer | 代表一个接受double值参数的操作，并且不返回结果。 |
| 9 | DoubleFunction&lt;R&gt; | 代表接受一个double值参数的方法，并且返回结果 |
| 10 | DoublePredicate | 代表一个拥有double值参数的boolean值方法 |
| 11 | DoubleSupplier | 代表一个double值结构的提供方 |
| 12 | DoubleToIntFunction | 接受一个double类型输入，返回一个int类型结果。 |
| 13 | DoubleToLongFunction | 接受一个double类型输入，返回一个long类型结果 |
| 14 | DoubleUnaryOperator | 接受一个参数同为类型double,返回值类型也为double 。 |
| 15 | Function&lt;T,R&gt; | 接受一个输入参数，返回一个结果。 |
| 16 | IntBinaryOperator | 接受两个参数同为类型int,返回值类型也为int 。 |
| 17 | IntConsumer | 接受一个int类型的输入参数，无返回值 |
| 18 | IntFunction&lt;R&gt; | 接受一个int类型输入参数，返回一个结果 。 |
| 19 | IntPredicate | 接受一个int输入参数，返回一个布尔值的结果 |
| 20 | IntSupplier | 无参数，返回一个int类型结果 |
| 21 | IntToDoubleFunction | 接受一个int类型输入，返回一个double类型结果 |
| 22 | IntToLongFunction | 接受一个int类型输入，返回一个long类型结果。 |
| 23 | IntUnaryOperator | 接受一个参数同为类型int,返回值类型也为int |
| 24 | LongBinaryOperator | 接受两个参数同为类型long,返回值类型也为long |
| 25 | LongConsumer | 接受一个long类型的输入参数，无返回值。 |
| 26 | LongFunction&lt;R&gt; | 接受一个long类型输入参数，返回一个结果。 |
| 27 | LongPredicate | R接受一个long输入参数，返回一个布尔值类型结果。 |
| 28 | LongSupplier | 无参数，返回一个结果long类型的值。 |
| 29 | LongToDoubleFunction | 接受一个long类型输入，返回一个double类型结果。 |
| 30 | LongToIntFunction | 接受一个long类型输入，返回一个int类型结果。 |
| 31 | LongUnaryOperator | 接受一个参数同为类型long,返回值类型也为long。 |
| 32 | ObjDoubleConsumer&lt;T&gt; | 接受一个object类型和一个double类型的输入参数，无返回值。 |
| 33 | ObjIntConsumer&lt;T&gt; | 接受一个object类型和一个int类型的输入参数，无返回值。 |
| 34 | ObjLongConsumer&lt;T&gt; | 接受一个object类型和一个long类型的输入参数，无返回值。 |
| 35 | Predicate&lt;T&gt; | 接受一个输入参数，返回一个布尔值结果。 |
| 36 | Supplier&lt;T&gt; | 无参数，返回一个结果。 |
| 37 | ToDoubleBiFunction&lt;T,U&gt; | 接受两个输入参数，返回一个double类型结果 |
| 38 | ToDoubleFunction&lt;T&gt; | 接受一个输入参数，返回一个double类型结果 |
| 39 | ToIntBiFunction&lt;T,U&gt; | 接受两个输入参数，返回一个int类型结果。 |
| 40 | ToIntFunctSupplierSupplierSupplierion&lt;T&gt; | 接受一个输入参数，返回一个int类型结果。 |
| 41 | ToLongBiFunction&lt;T,U&gt; | 接受两个输入参数，返回一个long类型结果。 |
| 42 | ToLongFunction&lt;T&gt; | 接受一个输入参数，返回一个long类型结果。 |
| 43 | UnaryOperator&lt;T&gt; | 接受一个参数为类型T,返回值类型也为T。 |

[返回目录](/README.md)

