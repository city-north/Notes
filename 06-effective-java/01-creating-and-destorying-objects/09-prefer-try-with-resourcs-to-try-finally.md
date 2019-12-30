---
title:  EffectiveJava第9条:优先使用 try-with-resource 而不是 try-finally
date:  2019-03-07 21:28:17
tags: effective-java
---

# 优先使用 try-with-resource  而不是  try-finally 

Java类库里包含了必须通过调用close方法来手动关闭的资源。比如InputStream，OutputStream还有java.sql.Connection。
我们通常使用这种方式来关闭这些资源:

```java
// try-finally - No longer the best way to close resources!
//使用 try finally 来关闭资源已经不是最好的方式了
static String firstLineOfFile(String path) throws IOException { 
    BufferedReader br = new BufferedReader(new FileReader(path)); 
    try {
        return br.readLine(); 
    } finally {
        br.close(); 
    }
}

// try-finally is ugly when used with more than one resource!
// 如果有多个资源需要关闭,代码会很丑.可读性很差
static void copy(String src, String dst) throws IOException {
    InputStream in = new FileInputStream(src); 
    try {
        OutputStream out = new FileOutputStream(dst); 
        try {
            byte[] buf = new byte[BUFFER_SIZE]; 
            int n;
            while ((n = in.read(buf)) >= 0)
                out.write(buf, 0, n); 
        } finally {
            out.close();
        }
    } finally {
        in.close(); 
    }
}
```

上面的代码显示了两个问题:
- 当出现多个资源需要关闭的时候,代码会很丑
- 如果`br.readLine(); `报错,那么 finally 块的 `close`方法也会报错,那么这个时候第二个报错就会覆盖掉第一个报错,在错误栈内就没有第一种异常的记录

<!-- more -->
## 如何解决这两个问题


- Java 7引入`try-with-resources`语句
- 若要使用这个语句，一个资源必须实现AutoCloseable接口，而这个接口只有一个返回类型为void的close（void-returning）方法。
- Java类库和第三方类库里面的许多类和接口现在都实现或继承了AutoCloseable接口。如果你写了一个类，这个类代表一个必须被关闭的资源，那么你的类也应该实现AutoCloseable接口。''

```java
// try-with-resources - the the best way to close resources!
static String firstLineOfFile(String path) throws IOException { 
    try (
        BufferedReader br = new BufferedReader(new FileReader(path))
    ) { 
        return br.readLine();
    } 
}

// try-with-resources on multiple resources - short and sweet
static void copy(String src, String dst) throws IOException {
    try (
        InputStream in = new FileInputStream(src); 
        OutputStream out = new FileOutputStream(dst)
    ) {
        byte[] buf = new byte[BUFFER_SIZE]; int n;
        while ((n = in.read(buf)) >= 0)
            out.write(buf, 0, n); 
    }
```

优势:
- 比起try-finally，try-with-resources语句不仅更简短和更可读，而且它们更容易排查问题。
- 如果`br.readLine(); `报错,那么 finally 块的 `close`方法也会报错,那么会报第一个错

我们也可以像之前的try-finally语句那样，往try-with-resources里面添加catch子句。这能让我们无需在另一层嵌套污染代码就能处理异常。下面是一个比较刻意的例子，这个版本中的firstLineOfFile方法不会抛出异常，但如果它不能打开文件或者不能读打开的文件，它将返回一个默认值：

```java
// try-with-resources with a catch clause
static String firstLineOfFile(String path, String defaultVal) { 
    try ( BufferedReader br = new BufferedReader(new FileReader(path))) { 
        return br.readLine();
    } catch (IOException e) { 
        return defaultVal;
    } 
}
```

结论很明显：面对必须要关闭的资源，我们总是应该优先使用try-with-resources而不是try-finally。随之产生的代码更简短，更清晰，产生的异常对我们也更有用。try-with-resources语句让我们更容易编写必须要关闭的资源的代码，若采用try-finally则几乎做不到这点。