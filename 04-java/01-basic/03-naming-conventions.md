# Java Naming Conventions

>  By Lokesh Gupta | Filed Under: [Java Basics](https://howtodoinjava.com/java/basics/)

**Java naming conventions** are sort of guidelines which application programmers are expected to follow to produce a consistent and readable code throughout the application. If teams do not not follow these conventions, they may collectively write an application code which is hard to read and difficult to understand.

Java heavily uses **Camel Case** notations for naming the methods, variables etc. and **[TitleCase](https://howtodoinjava.com/java/string/convert-string-to-titlecase/)** notations for classes and interfaces.

Let’s understand these naming conventions in detail with examples.

## 1. Packages naming conventions

Package names must be a group of words starting with all lowercase domain name (e.g. com, org, net etc). Subsequent parts of the package name may be different according to an organization’s own internal naming conventions.

```java
package com.howtodoinjava.webapp.controller;
 
package com.company.myapplication.web.controller;
 
package com.google.search.common;
```

## 2. Classes naming conventions

In Java, class names generally should be **nouns**, in title-case with the first letter of each separate word capitalized. e.g.

```java
public class ArrayList {}
 
public class Employee {}
 
public class Record {}
 
public class Identity {}
```

## 3. Interfaces naming conventions

In Java, interfaces names, generally, should be **adjectives**. Interfaces should be in titlecase with the first letter of each separate word capitalized. In same cases, interfaces can be **nouns** as well when they present a family of classes e.g. `List` and `Map`.

```java
public interface Serializable {}
 
public interface Clonable {}
 
public interface Iterable {}
 
public interface List {}
```

## 4. Methods naming conventions

Methods always should be **verbs**. They represent an action and the method name should clearly state the action they perform. The method name can be a single or 2-3 words as needed to clearly represent the action. Words should be in camel case notation.

```java
public Long getId() {}
 
public void remove(Object o) {}
 
public Object update(Object o) {}
 
public Report getReportById(Long id) {}
 
public Report getReportByName(String name) {}
```

## 5. Variables naming conventions

All instance, static and method parameter variable names should be in camel case notation. They should be short and enough to describe their purpose. Temporary variables can be a single character e.g. the counter in the loops.

```java
public Long id;
 
public EmployeeDao employeeDao;
 
private Properties properties;
 
for (int i = 0; i < list.size(); i++) {
     
}
```

## 6. Constants naming conventions

Java constants should be all **UPPERCASE** where words are separated by **underscore** character (“_”). Make sure to use final modifier with constant variables.

```java
public final String SECURITY_TOKEN = "...";
 
public final int INITIAL_SIZE = 16;
 
public final Integer MAX_SIZE = Integer.MAX;
```

## 7. Generic types naming conventions

Generic type parameter names should be uppercase single letters. The letter `'T'` for type is typically recommended. In JDK classes, `E` is used for collection elements, `S` is used for service loaders, and `K and V` are used for map keys and values.

```java
public interface Map <K,V> {}
 
public interface List<E> extends Collection<E> {}
 
Iterator<E> iterator() {}
```

## 8. Enumeration naming conventions

Similar to class constants, enumeration names should be all uppercase letters.

```java
enum Direction {NORTH, EAST, SOUTH, WEST}
```

## 9. Annotations naming conventions

Annotation names follow title case notation. They can be adjective, verb or noun based the requirements.

```java
public @interface FunctionalInterface {}
 
public @interface Deprecated {}
 
public @interface Documented {}
 
public @Async Documented {
 
public @Test Documented {
```

In this post, we discussed the Java naming conventions to be followed for consistent writing of code which make the code more readable and maintainable.

Naming conventions are probably the first best practice to follow while writing clean code in any programming language.