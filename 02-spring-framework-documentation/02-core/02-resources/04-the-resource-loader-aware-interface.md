# `ResourceLoaderAware`接口

`ResourceLoaderAware`接口的一个特殊回调接口,会提供一个`ResourceLoader`的实例

```java
public interface ResourceLoaderAware {

    void setResourceLoader(ResourceLoader resourceLoader);
}
```

`ResourceLoaderAware`接口是一个特殊的回调接口,标识期望使用`ResourceLoader`引用提供的组件。

当一个 Spring bean 实现了这个接口时, Spring 上下文会调用`setResourceLoader`

## 值得注意的是

- 推荐直接使用`ResourceLoader`,因为能够与资源加载接口解耦 











