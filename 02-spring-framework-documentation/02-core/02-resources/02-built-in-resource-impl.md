# spring 内置的 Resource 实现类
1. `UrlResource`
2. `ClassPathResource`
3. `FileSystemResource`
4. `ServletContextResource`
5. `InputStreamResource`
6. `ByteArrayResource`

## UrlResource

`UrlResource` 包装了`Java.net.URL`类,可以用来获取 URL能获取的所有资源,例如

- 文件
- Http 请求
- FTP 请求

所有的`URL`都 以标准化的 String 类型代表,恰当的标准前缀可以用来指定 URL 类型

- file: 用来访问文件系统路径
- http: 用来访问 HTTP 协议资源
- ftp:用来访问 FTP 资源

`PropertyEditor`选择哪种类型去创建,如果是已经知道的类型(譬如 `classpath`),那么他会去创建这个前缀所代表的 `Resource`

## ClassPathResource

从 classpath 下获取的资源

- Thread contxt class loader 一个给定的类加载器
- 根据给定的 class 获取 resource

该类表示应该从类路径获得的资源。它使用线程上下文类加载器、给定的类加载器或给定的类来加载资源。

一个`ClassPathResource`是由 Java代码通过显式地通过类`ClassPathResource`构造器去创建的,但是通常在调用 API 方法的时候回隐式地去创建,该方法接受表示路径的字符串参数, `PropertyEditor识别字符串路径上的特殊前缀`classpath:，并在这种情况下创建一个`ClassPathResource`。

## FileSystemResource

这个`Resource`实现自`java.io.File` 和`java.nio.file.Path`handles ,支持以一个`File`解析或者是`URL`解析

## ServletContextResource

这是一个`Resource`实现类,为`ServletContext` 资源,翻译绝对路径到 web 应用的根目录

支持 URL流访问但是允许`java.io.file`访问(这个文件存在于文件系统),无论是存在于文件系统还是直接从 JAR 包里获取或者是数据库里获取(实际上依赖 Servlet 上下文容器)

## InputStreamResource

`InputStreamResource` 是一个给定`InputStream`的资源实现类,这个实现类仅仅在没有其他资源实现类适用的情况下适用,特别是，尽可能选择`ByteArrayResource`或任何基于文件的资源实现。

## ByteArrayResource

它是一个给定字节数组的`Resource`实现。它为给定的字节数组创建`ByteArrayInputStream`。

它对于从任何给定的字节数组加载内容都很有用，而不需要使用一次性的`InputStreamResource`。