# 010-Path

[TOC]

## 什么是Path

Path @since 1.7表示的是一个目录名序列，其后还可以跟着一个文件名。

路径中的第一个部件可以是根部件，例如/或

```
C：\
```

## 源码

#### java.nio.file.Paths 7

```java
static Path get（String first，String...more）
通过连接给定的字符串创建一个路径。

```

#### ￼java.nio.file.Path 7

```java
//如果other是绝对路径，那么就返回other；否则，返回通过连接this和other获得的路径。
Path resolve（Path other）
Path resolve（String other）

//如果other是绝对路径，那么就返回other；否则，返回通过连接this的父路径和other获得的路径。
Path resolveSibling（Path other）
Path resolveSibling（String other）

//返回用this进行解析，相对于other的相对路径。
Path relativize（Path other）

//移除诸如.和..等冗余的路径元素。
Path normalize（）

//返回与该路径等价的绝对路径。
Path toAbsolutePath（）

//返回父路径，或者在该路径没有父路径时，返回null。
Path getParent（）

//返回该路径的最后一个部件，或者在该路径没有任何部件时，返回null。
Path getFileName（）

//返回该路径的根部件，或者在该路径没有任何根部件时，返回null。
Path getRoot（）

//从该路径中创建一个File对象。
toFile（）
```

#### ￼java.io.File.1.0

```java
Path toPath（）7
从该文件中创建一个Path对象。
```

## 绝对路径和相对路径

而允许访问的根部件取决于文件系统。以根部件开始的路径是绝对路径；否则，就是相对路径。

例如，我们要分别创建一个绝对路径和一个相对路径；其中，对于绝对路径，我们假设计算机运行的是类Unix的文件系统：

```java
//绝对路径
final Path ec = Paths.get("/Users", "ec");
System.out.println(ec);

//相对路径
final Path path = Paths.get("note-java", "pom.xml");
System.out.println(path);
```

注意：路径不必对应着某个实际存在的文件，它仅仅只是一个抽象的名字序列。

## 解析路径-resolve

组合或解析路径是司空见惯的操作，调用p.resolve（q）将按照下列规则返回一个路径：

- 如果q是绝对路径，则结果就是q。
- 否则，根据文件系统的规则，将“p后面跟着q”作为结果。

例如，假设你的应用系统需要查找相对于给定基目录的工作目录，其中基目录是从配置文件中读取的，就像前一个例子一样。

```java
final String property = System.getProperty("user.home");
final Path basePath = Paths.get(property);      //basePath :/Users/ec
final Path resolve = basePath.resolve("work");  //resolve path :/Users/ec/work
```

## 创建兄弟目录-resolveSibling

一个很方便的方法resolveSibling，它通过解析指定路径的父路径产生其兄弟路径。例如，如果workPath是/opt/myapp/work，那么下面的调用

```
Path tempPath = workPath.resoveSibling("temp");  //opt/myapp/temp
```

## 泛解析-relativize

resolve的对立面是relativize，即调用p.relativize（r）将产生路径q，而对q进行解析的结果正是r。

例如，以“/home/cay”为目标对“/home/fred/myprog”进行相对化操作，会产生“../fred/myprog”，其中，我们假设..表示文件系统中的父目录。

```java
Path p = Paths.get("/home/cay");  //   /home/cay
Path r = Paths.get("/home/fred/myprog");
Path result = p.relativize(r); //../fred/myprog
```

## 去重冗余-normalize

normalize方法将移除所有冗余的.和..部件（或者文件系统认为冗余的所有部件）。

例如，规范化/home/cay/../fred/./myprog将产生/home/fred/myprog。

## 产生给定路径的绝对路径-toAbsolutePath

toAbsolutePath方法将产生给定路径的绝对路径，该绝对路径从根部件开始，例如/home/fred/input.txt或c：\Users\fred\input.txt。

## Path类有许多有用的方法用来将路径断开

```java
Path a = Paths.get("/home", "fred", "myprog.properties");
Path parent = p.getParent();	//	the path /home/fred
Path file = p.getFileName();  // 		 the path myprog.properties
Path root = p.getRoot();      //     the path /
```



