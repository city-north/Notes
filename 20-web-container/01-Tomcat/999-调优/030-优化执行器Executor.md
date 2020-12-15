# 优化执行器Executor

```xml
    <Executor name="tomcatThreadPool" 
              namePrefix="catalina-exec-"
              maxThreads="500" 
              minSpareThreads ="80"
              maxQueueSize="100"
              maxIdleTime="60000
              minSpareThreads="4" 
              prestartminSpareThreads="true"
              />
```

|      | 属性名                  | 解释                                                         |
| ---- | ----------------------- | ------------------------------------------------------------ |
| 1    | maxThreads              | 最大并发数，默认设置150，一般建议在500 ~ 1000 ，根据硬件设施和业务来判断 |
| 2    | minSpareThreads         | Tomcat创建时的初始化线程                                     |
| 3    | maxQueueSize            | 最大的等待队列数，超过则拒绝请求                             |
| 4    | maxIdleTime             | 空闲线程存活的时间，单位毫秒，默认60000=60秒                 |
| 5    | prestartminSpareThreads | 为 true时，minSpareThreads 才有效果                          |

<img src="../../../assets/image-20201031214808684.png" alt="image-20201031214808684"  />

## 如何查看线程数

我们可以在"http-nio-8080"下查看

图中是

- 最大线程为200
- 当前线程数是2