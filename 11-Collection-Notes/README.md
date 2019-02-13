# Introduction to Collections

Collections （ sometimes called a container），an object that groups multiple elements into a single unit，Collections are use to 

- store
- retrieve【[rɪˈtriv】检索
- manipulate【məˈnɪpjəˌlet】操作
- communicate aggregate data 

Typicallly , they represent data items that from natural group ,such as

- a poker hand (a collection of cards)
- a mail folder (a collection of letters)
- a telephone directory (a mapping of names to phone numbers)

## What Is a Collections Framework?

A *collections framework* is a unified architecture (统一架构) for representing and manipulating collections.

- **Interfaces:** These are abstract data types that represent collections. Interfaces allow collections to be manipulated independently of the details of their representation. In object-oriented languages, interfaces generally form a hierarchy【`haɪərɑ:rki`层次】.

```
接口往往代表相应的集合，接口可以独立地定义它代表的集合的一些操作特征
```



- **Implementations:** These are the concrete implementations of the collection interfaces. In essence【`ɛsəns`本质】, they are reusable【`riˈjuzəbl`可复用】 data structures.
- **Algorithms:** These are the methods that perform useful computations, such as searching and sorting, on objects that implement collection interfaces. The algorithms are said to be *polymorphic* ` [ˌpɒlɪ'mɔ:fɪk]`多态的: that is, the same method can be used on many different implementations of the appropriate collection interface. In essence, algorithms are reusable functionality.

## Benefits of the Java Collections Framework

The Java Collections Framework provides the following benefits:

- **减少编程工作量:**
- **提高程序速度和质量:** 
- **允许不相关的API之间的互操作性 **  
- **减少学习和使用新APIS的工作量:** 
- **减少设计新API的工作量:** 
- **促进软件重用** 