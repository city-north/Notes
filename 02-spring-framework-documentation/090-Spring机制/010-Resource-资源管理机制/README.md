# Resouce机制

## 目录

- [为什么Spring将资源封装成Resource接口](#为什么Spring将资源封装成Resource接口)
- [继承结构](#继承结构)
- [Resource接口](#Resource接口)

Spring的配置文件读取是通过ClassPathResource进行封装的，如

```java
 new ClassPathResource ("beanFactoryTest.xml") 
```

那么ClassPathResource完成了什么功能呢？

## Resource接口

```java
public interface InputStreamSource {
    InputStream getInputStream() throws IOException;
}
public interface Resource extends InputStreamSource {
    boolean exists();
    boolean isReadable(); 
    boolean isOpen();
    URL getURL() throws IOException;
    URI getURI() throws IOException;
    File getFile() throws IOException;
    long lastModified() throws IOException;
    Resource createRelative(String relativePath) throws IOException;
    String getFilename();
    String getDescription();
}
```

## 为什么Spring将资源封装成Resource接口

在Java中，将不同来源的资源抽象成URL，通过注册不同的handler（URLStreamHandler）来处理不同来源的资源的读取逻辑，一般handler的类型使用不同前缀（协议，Protocol）来识别，如“file:”“http:”“jar:”等，然而URL没有默认定义相对Classpath或ServletContext等资源的handler，虽然可以注册自己的URLStreamHandler来解析特定的URL前缀（协议），比如“classpath:”，然而这需要了解URL的实现机制，而且URL也没有提供基本的方法，如检查当前资源是否存在、检查当前资源是否可读等方法。

因而Spring对其内部使用到的资源实现了自己的抽象结构：Resource接口封装底层资源。

## 继承结构

![image-20200919230849447](../../../assets/image-20200919230849447.png)



-  
- InputStreamResource

有了Resource接口便可以对所有资源文件进行统一处理。至于实现，其实是非常简单的，

以getInputStream为例，

- ClassPathResource 中的实现方式便是通过 class 或者 classLoader 提供的底层方法进行调用
- FileSystemResource 的实现其实更简单，直接使用 FileInputStream 对文件进行实例化。

#### ClassPathResource.java

```java
	@Override
	public InputStream getInputStream() throws IOException {
		InputStream is;
		if (this.clazz != null) {
			is = this.clazz.getResourceAsStream(this.path);
		}
		else if (this.classLoader != null) {
			is = this.classLoader.getResourceAsStream(this.path);
		}
		else {
			is = ClassLoader.getSystemResourceAsStream(this.path);
		}
		return is;
	}
```

#### FileSystemResource.java

```java
	@Override
	public InputStream getInputStream() throws IOException {
		return Files.newInputStream(this.file.toPath());
	}
```

