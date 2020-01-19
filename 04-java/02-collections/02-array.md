# Array in Java

An **array** is a container object that holds a **fixed number of values** of a **single type** in a **contiguous memory location**. It is a data structure which is used to store finite number of elements and all elements must be of similar data type.

Arrays are index based data structure so they allow random access to elements, they store. Indices start with `'0'`.

## 1. Array representation in memory

In this example, we have create an array of 5 elements. Indexes will range from `'0'` to `'4'`.

```java
int[] a = new int[5];
 
a[0] = 1;
a[1] = 2;
a[2] = 4;
a[3] = 8;
a[4] = 16;
```

A pictorial representation of above example can be as below.

![Array in memory](assets/Array-in-memory.png)

## 2. Array Features

- Arrays are also a subtype of `Object` in Java.
- Arrays are objects so we can find the length of the array using attribute `'length'`.
- Java array are types. we can declare the [variables](https://howtodoinjava.com/java/basics/java-variables/) of array type.
- Arrays are ordered and each have an index beginning from `'0'` for the first element.
- Arrays can store primitives as well as objects. But all must be of a single type in one array instance.
- Just like other variables, arrays can also be `static`, `final` or used as method arguments.
- The size of an array must be specified by an `int` value.
- Java arrays are `Cloneable` and `Serializable`.

## 3. Types of Array in Java

An array can be one of two types.

#### 3.1. Single Dimensional Array 

> 一维数组

An array which store only primitives or objects is called single dimensional array. The general form of a one-dimensional array declaration is:

```java
type var-name[];
OR
type[] var-name;
 
//Examples
 
int[] numbers;
 
String names[];
```

#### 3.2. Multi-dimensional Array

A multi-dimensional array stores other arrays. It is **array of arrays**. In multi-dimensional array, each element of the array holding the reference of other array. A multidimensional array is created by appending one set of square brackets (`[ ]`) per dimension.

```java
type var-name[][];
OR
type[][] var-name;
 
//Examples
 
int[][] cordinates;
 
String nameSets[][];
```

