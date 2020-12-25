# 040-Spring资源加载器-ResourceLoader

[TOC]

## 为什么说Spring应用上下文也是一种Spring资源加载器

因为AbstractApplicationContext 接口本身继承了ResourceLoader ,默认实现在DefaultResourceLoader

## ResourceLoader资源加载器继承结构

- ResourceLoader (org.springframework.core.io)
  - DefaultResourceLoader (org.springframework.core.io)
  - ClassRelativeResourceLoader (org.springframework.core.io) 在jar包中相对于Class的资源加载器
  - **AbstractApplicationContext (org.springframework.context.support)**
    - AbstractRefreshableApplicationContext (org.springframework.context.support)
    - GenericApplicationContext (org.springframework.context.support)
  - FileSystemResourceLoader (org.springframework.core.io)
  - ServletContextResourceLoader (org.springframework.web.context.support)

值得注意的是AbstractApplicationContext,基本上所有的ApplicationContext都继承这个类,也就是说ApplicationContext本身就是一个资源加载器,其默认实现在DefaultResourceLoader中

## 代码实例

```java
/**
 * 带有字符编码的 {@link FileSystemResourceLoader} 示例
 *
 * @see FileSystemResourceLoader
 * @see FileSystemResource
 * @see EncodedResource
 * @since
 */
public class EncodedFileSystemResourceLoaderDemo {

    public static void main(String[] args) throws IOException {
        String currentJavaFilePath = "/" + System.getProperty("user.dir") + "/thinking-in-spring/resource/src/main/java/org/geekbang/thinking/in/spring/resource/EncodedFileSystemResourceLoaderDemo.java";
        // 新建一个 FileSystemResourceLoader 对象
        FileSystemResourceLoader resourceLoader = new FileSystemResourceLoader();
        // FileSystemResource => WritableResource => Resource
        Resource resource = resourceLoader.getResource(currentJavaFilePath);
        EncodedResource encodedResource = new EncodedResource(resource, "UTF-8");
        // 字符输入流
        try (Reader reader = encodedResource.getReader()) {
            System.out.println(IOUtils.toString(reader));
        }
    }
}
```

