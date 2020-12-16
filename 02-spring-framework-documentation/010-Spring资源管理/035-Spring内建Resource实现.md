# 035-Spring内建Resource实现md

[TOC]

## 内建接口图示

![image-20200919230849447](../../assets/image-20200919230849447.png)

## 内建实现

Resource接口是抽象的,它的数据来源是不确定的,有可能是数组、类路径等等.

Java中的资源可以分为三类

- ClassLoader 对应ClassPathResource
- File 对应 FileSystemResource
- URL 对应 UrlResource

| 资源来源                             | 资源协议      | 实现类                                                       |
| ------------------------------------ | ------------- | ------------------------------------------------------------ |
| Bean定义<br />BeanDefinitionResource | /             | org.springframework.beans.factory.support.BeanDefinitionResource |
| 数组                                 | /             | org.springframework.core.io.ByteArrayResource                |
| 类路径                               | classpath:    | org.springframework.core.io.ClassPathResource                |
| 文件系统                             | file:         | org.springframework.core.io.FileSystemResource               |
| URL                                  | URL支持的协议 | org.springframework.core.io.UrlResource                      |
| ServletContext                       | /             | org.springframework.web.context.support.ServletContextResource |

ServletContext 关注当前应用目录所对应的资源

### BeanDefinitionResource

BeanDefinitionResource 封装了一个BeanDefinition,它既不可读,也不存在,也不支持获取输入流

主要作用是进行描述getDescription

```java
class BeanDefinitionResource extends AbstractResource {
	//包装一个Bean定义
	private final BeanDefinition beanDefinition;
	public BeanDefinitionResource(BeanDefinition beanDefinition) {
		Assert.notNull(beanDefinition, "BeanDefinition must not be null");
		this.beanDefinition = beanDefinition;
	}
	public final BeanDefinition getBeanDefinition() {
		return this.beanDefinition;
	}
  //Bean定义Resource不存在
	@Override
	public boolean exists() {
		return false;
	}
  //Bean定义Resource不可读
	@Override
	public boolean isReadable() {
		return false;
	}
	//不支持获取输入流
	@Override
	public InputStream getInputStream() throws IOException {
		throw new FileNotFoundException(
				"Resource cannot be opened because it points to " + getDescription());
	}
	@Override
	public String getDescription() {
		return "BeanDefinition defined in " + this.beanDefinition.getResourceDescription();
	}
}
```

### ByteArrayResource

字节数组的Resource,支持转换成ByteArrayInputStream

```java
public class ByteArrayResource extends AbstractResource {

	private final byte[] byteArray;

	private final String description;


	/**
	 * 获取字节数组输出流
	 */
	@Override
	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(this.byteArray);
	}

}

```

### ClassPathResource

ClassPathResource用来表示ClassPath下的资源,可以提供一个ClassLoader或者给定一个Class进行加载

- 如果是在文件系统中,那么支持获取File
- 如果是在一个jar包里,那么不支持获取FIle.支持获取URL

读取方式

- 提供一个Class进行读取:读取Class所在jar包的资源
- 提供一个ClassPath: 指定ClassPath下所有的

```java
public class ClassPathResource extends AbstractFileResolvingResource {

	private final String path;

	@Nullable
	private ClassLoader classLoader;

	@Nullable
	private Class<?> clazz;


	public ClassPathResource(String path, @Nullable ClassLoader classLoader) {
		Assert.notNull(path, "Path must not be null");
		String pathToUse = StringUtils.cleanPath(path);
		if (pathToUse.startsWith("/")) {
			pathToUse = pathToUse.substring(1);
		}
		this.path = pathToUse;
    //如果classLoader是null,则使用默认的ClassLoader
		this.classLoader = (classLoader != null ? classLoader : 
                        ClassUtils.getDefaultClassLoader());
	}

	@Override
	public InputStream getInputStream() throws IOException {
		InputStream is;
    //Class 方式读取
		if (this.clazz != null) {
			is = this.clazz.getResourceAsStream(this.path);
		}
    //使用类加载器读取
		else if (this.classLoader != null) {
			is = this.classLoader.getResourceAsStream(this.path);
		}
		else {
			is = ClassLoader.getSystemResourceAsStream(this.path);
		}
		if (is == null) {
			throw new FileNotFoundException(getDescription() + " cannot be opened because it does not exist");
		}
		return is;
	}


}

```

### FileSystemResource

```java
public class FileSystemResource extends AbstractResource implements WritableResource {

	private final File file;

	private final String path;


	public FileSystemResource(String path) {
		Assert.notNull(path, "Path must not be null");
		this.file = new File(path);
		this.path = StringUtils.cleanPath(path);
	}

	@Override
	public InputStream getInputStream() throws IOException {
		try {
      //静态内部类
			return Files.newInputStream(this.file.toPath());
		}
		catch (NoSuchFileException ex) {
			throw new FileNotFoundException(ex.getMessage());
		}
	}
}

```

## UrlResource

UrlResource代表使用URL方式的资源, 在获取输入流的时候,底层也是用的Java的URL.openConnection

```java
public class UrlResource extends AbstractFileResolvingResource {

	/**
	 * Original URI, if available; used for URI and File access.
	 */
	@Nullable
	private final URI uri;

	/**
	 * Original URL, used for actual access.
	 */
	private final URL url;

	/**
	 * Cleaned URL (with normalized path), used for comparisons.
	 */
	private final URL cleanedUrl;

	@Override
	public InputStream getInputStream() throws IOException {
		URLConnection con = this.url.openConnection();
		ResourceUtils.useCachesIfNecessary(con);
		try {
			return con.getInputStream();
		}
		catch (IOException ex) {
			// Close the HTTP connection (if applicable).
			if (con instanceof HttpURLConnection) {
				((HttpURLConnection) con).disconnect();
			}
			throw ex;
		}
	}
}

```

