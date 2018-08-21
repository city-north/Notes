以下是你应从本章中学到的关键概念。

* Lambda 表达式可以理解为一种匿名函数：它没有名称，但有参数列表、函数主体、返回

* 类型，可能还有一个可以抛出的异常的列表。

* Lambda 表达式让你可以简洁地传递代码。

* 函数式接口就是仅仅声明了一个抽象方法的接口。

* 只有在接受函数式接口的地方才可以使用 Lambda 表达式。

* Lambda 表达式允许你直接内联，为函数式接口的抽象方法提供实现，并且将整个表达式作为函数式接口的一个实例。

* Java 8 自带一些常用的函数式接口，放在 java.util.function 包里，包括 Predicate&lt;T&gt; 、 Function&lt;T,R&gt; 、 Supplier&lt;T&gt; 、 Consumer&lt;T&gt; 和 BinaryOperator&lt;T&gt;

* 为了避免装箱操作，对 Predicate&lt;T&gt; 和 Function&lt;T, R&gt; 等通用函数式接口的原始类型特化： IntPredicate 、 IntToLongFunction 等。

* 环绕执行模式（即在方法所必需的代码中间，你需要执行点儿什么操作，比如资源分配和清理）可以配合 Lambda 提高灵活性和可重用性。

* Lambda 表达式所需要代表的类型称为目标类型。

* 方法引用让你重复使用现有的方法实现并直接传递它们。

* Comparator 、 Predicate 和 Function 等函数式接口都有几个可以用来结合 Lambda 表达  
  式的默认方法。



