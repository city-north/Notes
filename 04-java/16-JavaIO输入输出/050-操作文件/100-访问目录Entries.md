# 100-访问目录Entries

[TOC]

## 获取路径下的所有目录

### 1. 使用FIles.list方法获取目录下的项

使用静态方法 Files.list 方法可以返回一个 `Stream<Path>`,返回的这个path实际上就是这个目录的目录项

这个方法是惰性读取的，所以在具有大量项的目录时更高效

```java
/**
     * 测试展示目录下的所有Entry ,不包含子目录
     *
     * @param path 路径
     */
public static void listAllEntries(String path) throws IOException {
    Stream<Path> list = Files.list(Paths.get(path));
    list.forEach(System.out::println);
}

```

输出结果仅仅是当前目录

### 2. 使用Files.walk方法获取到目录以及其子目录下的项

```java
    /**
     * 列出所有目录以及子目录
     * @param path
     */
    private static void listAllEntriesAndSubDirectory(String path) throws IOException {
        Stream<Path> walk = Files.walk(Paths.get(path));
        walk.forEach(System.out::println);
    }

```

特点：

1. 无论遍历的项是不是目录，那么首先会遍历外部的，等待外部的全部展开了才会访问其子目录
2. walk方法的第二个参数可以传入便利的深度

### 3. 使用find方法过滤返回的结果

如果要过来walk访问的路径，并且过来标准涉及到目录存储相关的文件属性，例如，尺寸，创建时间，和类型等等，那么就应该用find方法来替代wakl方法， 第三个参数是谓语函数来调用这个方法

```java
@Test
public void listAllFileEntriesWithFilter() throws IOException {
    Stream<Path> pathStream = Files.find(Paths.get(PATH), 2, (t, u) -> {
        if ("src".equalsIgnoreCase(t.getFileName().toString())) {
            return true;
        }
        return false;
    });
    pathStream.forEach(System.out::println);
}
```

## 代码示例-使用FIles.walk 拷贝目录

```java
    @Test
    public void copyDirectory() throws Exception {
        Path path = Paths.get(PATH);
        Path target = Paths.get(TARGET);
        Files.walk(path).forEach(p->{
            try{
                //解析
                Path relativize = path.relativize(p);//相对路径
                Path resolve = target.resolve(relativize);
                if (Files.isDirectory(resolve)){
                    Files.createDirectory(resolve);
                }else {
                    Files.copy(p,resolve);
                }
            }catch (IOException e){
                throw new UncheckedIOException(e);
            }
        });
    }
}

```

