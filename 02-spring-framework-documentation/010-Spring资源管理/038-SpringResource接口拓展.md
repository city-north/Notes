# 038-Spring资源接口扩展

[TOC]

## SpringResource接口拓展

[TOC]

可写资源接口

- org.springframework.core.io.WritableResource
  - org.springframework.core.io.FileSystemResource
  - org.springframework.core.io.FileUrlResource(@since 5.0.2)
  - org.springframework.core.io.PathResource(@since 4.0) @deprecated

编码资源接口

- org.springframework.core.io.support.EncodedResource

## 代码实例

```java
/**
 * 带有字符编码的 {@link FileSystemResource} 示例
 *
 * @see FileSystemResource
 * @see EncodedResource
 * @since
 */
public class EncodedFileSystemResourceDemo {

    public static void main(String[] args) throws IOException {
        String currentJavaFilePath = System.getProperty("user.dir") + "/thinking-in-spring/resource/src/main/java/org/geekbang/thinking/in/spring/resource/EncodedFileSystemResourceDemo.java";
        File currentJavaFile = new File(currentJavaFilePath);
        // FileSystemResource => WritableResource => Resource
        FileSystemResource fileSystemResource = new FileSystemResource(currentJavaFilePath);
        EncodedResource encodedResource = new EncodedResource(fileSystemResource, "UTF-8");
        // 字符输入流
        // 字符输入流
        try (Reader reader = encodedResource.getReader()) {
            System.out.println(IOUtils.toString(reader));
        }
    }
}

```

