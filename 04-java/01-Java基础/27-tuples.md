# Java tuple – Working with tuples in Java

元组

In this Java tutorial, we will learn about **Java Tuple** – a generic data structure and how we can use tuples in a Java program. Tuple, by default, are not present in Java programming language as data structure, so we will use one nice third-party library **javatuples** for it.

## 1. What is tuple?

A tuple can be seen as an *ordered collection of objects of different types*. These objects do not necessarily relate to each other in any way, but collectively they will have some meaning.

For an example `["Sajal Chakraborty", "IT Professional", 32]` can be a tuple where each value inside the tuple does not have any relation but this whole set of values can have some meaning in the application. For example, give tuple may represent an employee data with name, department and age.

Let’s see some more *java tuple examples*.

```
["Java", 1.8, "Windows"]
["Alex", 32, "New York", true]
[3, "Alexa", "howtodoinjava.com", 37000]
```

## 2. Tuple in Java

Java doesn’t have any such inbuilt data structure to support tuples. Whenever required, we can obviously create a class which can act like tuple.

Also, in Java part of the tuple functionality can be written using `List` or `Array` but those will not allow us to hold different types of data types by design. So we can say that heterogeneous tuple using standard data structure is not possible in Java.

#### 2.1. Comparison of Tuples vs Lists/Arrays

Tuple is often compared with List as it looks very much like a list. But they differ in some aspects.

1. A tuple is an object that can contain heterogeneous data. Lists are designed to store elements of a single type.
2. Out of all data structures, a tuple is considered to be the fastest and they consume the least amount of memory.
3. While array and list are mutable which means you can change their data value and modify their structures, a tuple is immutable.
4. Like an array, a tuple is also fixed in size. That is why tuples aims to replace array completely as they are more efficient in all parameters.
5. If you have a dataset which will be assigned only once in lifetime and its value should not change again, you need a tuple.

## 3. Javatuples library

#### 3.1. javatuples maven dependency

**javatuples** library is present in the maven central repo and we can add this dependency to use the library.

```xml
<dependency>
    <groupId>org.javatuples</groupId>
    <artifactId>javatuples</artifactId>
    <version>1.2</version>
</dependency>
```

#### 3.2. javatuples – classes

Java tuples supports tuples of size up to `'10'` and for each size it has provided a tuple implementation like below.

- `Unit` (one element)
- `Pair` (two elements)
- `Triplet` (three elements)
- `Quartet` (four elements)
- `Quintet` (five elements)
- `Sextet` (six elements)
- `Septet` (seven elements)
- `Octet` (eight elements)
- `Ennead` (nine elements)
- `Decade` (ten elements)

On top of above classes, it provides two more classes for easy representation of pairs. Those mostly same as `Pair` but has more verbose syntax.

1. **KeyValue**
2. **LabelValue**

#### 3.3. javatuples – features

*Different types of java tuple* are:

1. Type safe
2. Immutable
3. Iterable
4. Serializable
5. Comparable (implements Comparable)
6. Implementing `equals()` and `hashCode()`
7. Implementing `toString()`