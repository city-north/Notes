[返回目录](/README.md)

# Lambda实战：环绕执行模式

通过一个例子来看Lambda和行为参数化如何使代码更加灵活。

资源处理，常见的模式就是打开一个资源，做一些操作，然后关闭资源。

```
public static String processFile() throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
        return br.readLine();
    }
}
```

可以看到，打开资源相关代码，清理结束代码是围绕则任务展开的：

![](/assets/import03.png)

## 第一步：分析为什么要将行为参数化

上面的代码显然不够灵活，只读取了文件的第一行。如果我想要返回头两行，甚至是返回使用最频繁的词，那该怎么办？

答案： 将processFile方法的行为参数化，也就是将

**return br.readLine\(\); **这个行为，作为参数传入到方法体内执行，以完成不同的需求。

## 第二步：使用函数式接口来传递行为

Lambda仅可用于上下文是函数式接口的情况。你需要创建一个能匹配BufferReader -&gt; String，还可以抛出IOException 异常的接口

```
@FunctionalInterface
public interface BufferedReaderProcessor {
    String process(BufferedReader b) throws IOException;
}
```

## 第三步：执行一个行为

现在，这个接口就可以作为新的processFile方法的参数了（将行为参数化了）,方法体内执行接口中的方法。

```
public static String processFile(BufferedReaderProcessor p) throws IOException{
    try(BufferReader br = new BufferdReader(new FileReader("data.txt"))){
        return p.process(br);    
    }
```

## 第四步：传递Lamdba

现在就可以通过传递不同的Lambda重用processFile方法，并以不同的方法处理文件了。

```
String oneLine = processFile((BufferedReader br) -> br.readLine());
//处理两行
String twoLine = processFile((BufferedReader br) -> br.readLine() + br.readLine());
```

## 总结

一共分为四步，就可以**利用函数式接口来传递Lambda**

![](/assets/import04.png)

[返回目录](#)

