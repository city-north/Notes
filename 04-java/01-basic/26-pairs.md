# Pairs in Java

Learn to work with key value pairs in Java using **Pair** classes e.g. `javafx.util.Pair`, `ImmutablePair`, `MmutablePair` (common langs) and `io.vavr.Tuple2` class.

## 1. Why we need pairs?

A pair provide a convenient way of associating a simple key to value. In Java, maps are used to store key-value pairs. Maps store a collection of pairs and operate them as a whole.

Sometimes, we need to work on requirements where a key-value pair shall exist on it’s own. e.g.

- A pair need to be passed in a method as argument
- Method need to return two values in form of a pair

## 2. javafx.util.Pair class

Java core APIs have `javafx.util.Pair` as closest match which serve the purpose of having two values as **name-value pair**. Follow this link to learn to add [JavaFx support in eclipse](https://www.eclipse.org/efxclipse/install.html).

`Pair` class provides following methods.

- `boolean equals(Object o)` – Test this Pair for equality with another Object.
- `K getKey()` – Gets the key for this pair.
- `V getValue()` – Gets the value for this pair.
- `int hashCode()` – Generate a hash code for this Pair.
- `String toString()` – String representation of this Pair.

Let’s see a **java program to create and use pair**.

```java
Pair<Integer, String> pair = new Pair<>(100, "howtodoinjava.com");
         
Integer key = pair.getKey();        //100
String value = pair.getValue();     //howtodoinjava.com
 
pair.equals(new Pair<>(100, "howtodoinjava.com"));    //true - same name and value
 
pair.equals(new Pair<>(222, "howtodoinjava.com"));    //false - different name
 
pair.equals(new Pair<>(100, "example.com"));      //false - different value
```

## 3. Pair, ImmutablePair and MutablePair – Apache commons lang

Commons lang library has a useful class which can used as pair i.e. [org.apache.commons.lang3.tuple.Pair](https://commons.apache.org/proper/commons-lang/javadocs/api-3.1/org/apache/commons/lang3/tuple/Pair.html). It has two subclasses which can also be used for same purpose i.e. [ImmutablePair](https://commons.apache.org/proper/commons-lang/javadocs/api-3.1/org/apache/commons/lang3/tuple/ImmutablePair.html) and [MutablePair](https://commons.apache.org/proper/commons-lang/javadocs/api-3.1/org/apache/commons/lang3/tuple/MutablePair.html).

- `Pair` class is a pair consisting of two elements.
- `Pair` refers to the elements as ‘left’ and ‘right’.
- `Pair` also implements the `Map.Entry` interface where the key is ‘left’ and the value is ‘right’.
- `ImmutablePair` is [immutable](https://howtodoinjava.com/java/basics/how-to-make-a-java-class-immutable/) representation on `Pair`. If mutable objects are stored in the pair, then the pair itself effectively becomes mutable. The class is also not `final`, so a subclass could add undesirable behavior.
- `ImmutablePair` is [thread-safe](https://howtodoinjava.com/java/multi-threading/what-is-thread-safety/) if the stored objects are thread-safe.

```java
ImmutablePair<Integer, String> pair = ImmutablePair.of(100, "howtodoinjava.com");
         
Integer key = pair.getKey();            //100
String value = pair.getValue();         //howtodoinjava.com
 
//Integer key = pair.getLeft();         //100
//String value = pair.getRight();       //howtodoinjava.com
 
pair.equals(ImmutablePair.of(100, "howtodoinjava.com"));    //true - same name and value
 
pair.equals(ImmutablePair.of(222, "howtodoinjava.com"));    //false - different name
 
pair.equals(ImmutablePair.of(100, "example.com"));      //false - different value
```

Do not forget to import the library into application classpath.

```xml
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-lang3</artifactId>
    <version>3.8.1</version>
</dependency>
```

## 4. io.vavr.Tuple2 – Vavr

Another useful class for storing key-value pair is [Tuple2](https://static.javadoc.io/io.vavr/vavr/0.9.0/io/vavr/Tuple2.html).

`Tuple2` provide lots of useful method to work on data stored in it. e.g.

- `T1 _1()` – Getter of the 1st element of this tuple.
- `T2 _2()` – Getter of the 2nd element of this tuple.
- `Tuple2 update1(T1 value)` – Sets the 1st element of this tuple to the given value.
- `Tuple2 update2(T2 value)` – Sets the 2nd element of this tuple to the given value.
- `Map.Entry toEntry()` – Converts the tuple to `java.util.Map.Entry` Tuple.
- `Tuple2 swap()` – Swaps the elements of this Tuple.
- `Tuple2 map(BiFunction mapper)` – Maps the components of this tuple using a mapper function.
- `int compareTo(Tuple2 that) `– Compare two Tuple2 instances.

```java
Tuple2<Integer, String> pair = new Tuple2<>(100, "howtodoinjava.com");
         
Integer key = pair._1();            //100
String value = pair._2();           //howtodoinjava.com
 
pair.equals(new Tuple2<>(100, "howtodoinjava.com"));  //true - same name and value
 
pair.equals(new Tuple2<>(222, "howtodoinjava.com"));  //false - different name
 
pair.equals(new Tuple2<>(100, "example.com"));        //false - different value
```

Do not forget to import the library into application classpath.

```xml
<dependency>
    <groupId>io.vavr</groupId>
    <artifactId>vavr</artifactId>
    <version>0.10.2</version>
</dependency>
```

