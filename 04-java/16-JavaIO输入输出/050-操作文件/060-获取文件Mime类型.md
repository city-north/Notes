# 060-获取文件Mime类型

[TOC]

## **1. Overview**

In this tutorial, we'll take a look at various strategies for getting MIME types of a file. We'll look at ways to extend the MIME types available to the strategies, wherever applicable.

We'll also point out where we should favor one strategy over the other.

## **2. Using Java 7**

Let's start with Java 7 – which provides the method [*Files.probeContentType(path)*](https://docs.oracle.com/javase/7/docs/api/java/nio/file/Files.html#probeContentType(java.nio.file.Path)) for resolving the MIME type:

```java
@Test
public void whenUsingJava7_thenSuccess() {
    Path path = new File("product.png").toPath();
    String mimeType = Files.probeContentType(path);
 
    assertEquals(mimeType, "image/png");
}
```

This method makes use of the installed *FileTypeDetector* implementations to probe the MIME type. It invokes the *probeContentType* of each implementation to resolve the type.

Now, if the file is recognized by any of the implementations, the content type is returned. However, if that doesn't happen, a system-default file type detector is invoked.

**However, the default implementations are OS specific and might fail depending on the OS that we are using.**

In addition to that, it's also important to note that the strategy will fail if the file isn't present in the filesystem. Furthermore, if the file doesn't have an extension, it will result in failure.

##  **3. Using \*URLConnection\***

*URLConnection* provides several APIs for detecting MIME types of a file. Let's briefly explore each of them.

### **3.1. Using \*getContentType()\***

We can use *getContentType()* method of *URLConnection* to retrieve a file's MIME type:

```java
@Test
public void whenUsingGetContentType_thenSuccess(){
    File file = new File("product.png");
    URLConnection connection = file.toURL().openConnection();
    String mimeType = connection.getContentType();
 
    assertEquals(mimeType, "image/png");
}
```

However, a major drawback of this approach is that **it's very slow**.

### **3.2. Using \*guessContentTypeFromName()\***

Next, let's see how we can make use of the *guessContentTypeFromName()* for the purpose:

```java
@Test
public void whenUsingGuessContentTypeFromName_thenSuccess(){
    File file = new File("product.png");
    String mimeType = URLConnection.guessContentTypeFromName(file.getName());
 
    assertEquals(mimeType, "image/png");
}
```

This method makes use of the internal *FileNameMap* to **resolve the MIME type from the extension**.

We also have the option of using *guessContentTypeFromStream()* instead, which uses the first few characters of the input stream, to determine the type.

### **3.3. Using \*getFileNameMap\*()**

A faster way to obtain the MIME type using *URLConnection* is using the *getFileNameMap()* method:

```java
@Test
public void whenUsingGetFileNameMap_thenSuccess(){
    File file = new File("product.png");
    FileNameMap fileNameMap = URLConnection.getFileNameMap();
    String mimeType = fileNameMap.getContentTypeFor(file.getName());
 
    assertEquals(mimeType, "image/png");
}
```

The method returns the table of MIME types used by all instances of *URLConnection.* This table is then used to resolve the input file type.

The built-in table of MIME types is very limited when it comes to *URLConnection*.

By default, **the class uses \*content-types.properties\*** file in *JRE_HOME/lib*. **We can, however, extend it, by specifying a user-specific table using the \*content.types.user.table\* property:**

```java
System.setProperty("content.types.user.table","<path-to-file>");
```

## **4. Using \*MimeTypesFileTypeMap\***

*MimeTypesFileTypeMap* resolves MIME types by using file's extension. This class came with Java 6, and hence comes very handy when we're working with JDK 1.6.

Now let's see how to use it:

```java
@Test
public void whenUsingMimeTypesFileTypeMap_thenSuccess() {
    File file = new File("product.png");
    MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
    String mimeType = fileTypeMap.getContentType(file.getName());
 
    assertEquals(mimeType, "image/png");
}
```

Here, we can either pass the name of the file or the *File* instance itself as the parameter to the function. However, the function with *File* instance as the parameter internally calls the overloaded method that accepts the filename as the parameter.

**Internally, this method looks up a file called \*mime.types\* for the type resolution. It's very important to note that the method searches for the file in a specific order:**

1. Programmatically added entries to the *MimetypesFileTypeMap* instance
2. .*mime.types* in the user's home directory
3. *<java.home>/lib/mime.types*
4. resources named *META-INF/mime.types*
5. resource named *META-INF/mimetypes.default* (usually found only in the *activation.jar* file)

However, if no file is found, it will return *application/octet-stream* as the response.

## **5. Using \*jMimeMagic\***

[jMimeMagic](https://github.com/arimus/jmimemagic) is a restrictively licensed library that we can use to obtain the MIME type of a file.

Let's start by configuring the Maven dependency:

```java
<dependency>
    <groupId>net.sf.jmimemagic</groupId>
    <artifactId>jmimemagic</artifactId>
    <version>0.1.5</version>
</dependency>
```

We can find the latest version of this library on [Maven Central](https://search.maven.org/classic/#search|gav|1|g%3A"net.sf.jmimemagic" AND a%3A"jmimemagic").

Next, we'll explore how to work with the library:

```java
@Test    
public void whenUsingJmimeMagic_thenSuccess() {
    File file = new File("product.png");
    Magic magic = new Magic();
    MagicMatch match = magic.getMagicMatch(file, false);
 
    assertEquals(match.getMimeType(), "image/png");
}
```

This library can work with a stream of data and hence doesn't require the file to be present in the file system.

## **6. Using Apache Tika**

[Apache Tika](https://tika.apache.org/) is a toolset that detects and extracts metadata and text from a variety of files. It has a rich and powerful API and comes with [tika-core](https://search.maven.org/classic/#search|gav|1|g%3A"org.apache.tika" AND a%3A"tika-core") which we can make use of, for detecting MIME type of a file.

Let's begin by configuring the Maven dependency:

```java
<dependency>
    <groupId>org.apache.tika</groupId>
    <artifactId>tika-core</artifactId>
    <version>1.18</version>
</dependency>
```

Next, we'll make use of the *detect()* method to resolve the type:

```java
@Test
public void whenUsingTika_thenSuccess() {
    File file = new File("product.png");
    Tika tika = new Tika();
    String mimeType = tika.detect(file);
 
    assertEquals(mimeType, "image/png");
}
```

The library relies on magic markers in the stream prefix, for type resolution.

## **7. Conclusion**

In this article, we've looked at the various strategies of obtaining the MIME type of a file. Furthermore, we have also analyzed the tradeoffs of the approaches. We have also pointed out the scenarios where we should favor one strategy over the other.

The full source code that is used in this article is available [over at GitHub](https://github.com/eugenp/tutorials/tree/master/core-java-modules/core-java-io), as always.