# Java hashCode() and equals()

Contract , rules and best practices

**[hashCode()](https://docs.oracle.com/javase/7/docs/api/java/lang/Object.html#hashCode())**  and  **[equals()](https://docs.oracle.com/javase/7/docs/api/java/lang/Object.html#equals(java.lang.Object))** methods have been defined in Object class which is parent class for java objects. For this reason, all java objects inherit a default implementation of these methods.

- Usage of hashCode() and equals() Method
- Override the default behavior
- EqualsBuilder and HashCodeBuilder
- Genrate hashCode() and equals() Method
- Important things to remember
- Special Attention When Using in ORM

## Usage of hashCode() and equals() Methods

- `equals(Object otherObject)` – As method name suggests, is used to simply verify the equality of two objects. It’s default implementation simply check the object references of two objects to verify their equality. *By default, two objects are equal if and only if they are stored in the same memory address.*

> 默认情况下`equals`方法使用的是`==` 比对内存地址

- `hashcode()` – Returns a unique integer value for the object in runtime. By default, integer value is mostly derived from memory address of the object in heap (but it’s not mandatory always).
  This hash code is used for determining the bucket location, when this object needs to be stored in some [HashTable](https://en.wikipedia.org/wiki/Hash_table) like data structure.

> 默认情况下，整数值主要来自堆中对象的内存地址(但并非总是强制的)。

#### 1.1. Contract between hashCode() and equals()

It is generally necessary to override the `hashCode()` method whenever `equals()` method is overridden, so as to maintain the general contract for the `hashCode()` method, which states that **equal objects must have equal hash codes**.

> 相同的对象一定有相等的 hashCode
>
> 当重写 equals 方法的时候,也要重写 hashCode 方法

- Whenever it is invoked on the same object more than once during an execution of a Java application, the `hashCode` method must consistently return the same integer, provided no information used in `equals` comparisons on the object is modified.
  This integer need not remain consistent from one execution of an application to another execution of the same application.

> 在Java应用程序的执行过程中，无论何时在同一个对象上多次调用它，`hashCode`方法都必须一致地返回相同的整数，前提是不修改对象上的' equals '比较中使用的信息。

- If two objects are equal according to the `equals(Object)` method, then calling the `hashCode` method on each of the two objects must produce the same integer result.

> 如果两个对象根据`equals(Object)`方法是相等的，那么在这两个对象上调用`hashCode `方法必须产生相同的整数结果。

- It is *not* required that if two objects are unequal according to the [`equals(java.lang.Object)`](https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#equals-java.lang.Object-) method, then calling the `hashCode` method on each of the two objects must produce distinct integer results.
  However, the programmer should be aware that producing distinct integer results for unequal objects may improve the performance of hash tables.

> 为不相等的对象生成不同的整数结果可能会提高哈希表的性能。

##  Java hashCode() and equals() best practices

1. Always use same attributes of an object to generate `hashCode()` and `equals()` both. As in our case, we have used employee `id`.
2. `equals()` must be *consistent* (if the objects are not modified, then it must keep returning the same value).
3. Whenever **a.equals(b)**, then *a.hashCode()* must be same as *b.hashCode()*.
4. If you override one, then you should override the other.

## Special Attention When Using in *ORM*

If you’re dealing with an ORM, make sure to **always use getters, and never field references in `hashCode()` and `equals()`**. This is for reason, in ORM, occasionally fields are lazy loaded and not available until called their getter methods.

For example, In our `Employee` class if we use `*e1.id == e2.id*`. It is very much possible that id field is lazy loaded. So in this case, one might be zero or null, and thus resulting in incorrect behavior.

But if uses `*e1.getId() == e2.getId()*`, we can be sure even if field is lazy loaded; calling getter will populate the field first.

This is all i know about **`hashCode()` and `equals()` methods**. I hope, it will help someone somewhere.

If you feel, I am missing something or wrong somewhere, please leave a comment. I will update this post again to help others.