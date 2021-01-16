# 140-Spring拓展点-DisposableBean

org.springframework.beans.factory.DisposableBean

这个扩展点也只有一个方法：destroy()，其触发时机为当此对象销毁时，会自动执行这个方法。比如说运行applicationContext.registerShutdownHook时，就会触发这个方法。

扩展方式为：

```
publicclassNormalBeanAimplementsDisposableBean{@Overridepublicvoid destroy()throwsException{System.out.println("[DisposableBean] NormalBeanA");}}
```

