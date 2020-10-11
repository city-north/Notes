

# 09-Eureka的MetadataMap

在EurekaInstanceConfigBean中，相当大一部分内容是关于Eureka Client服务实例的信息，这部分信息称为元数据，它是用来描述自身服务实例的相关信息。Eureka中的标准元数据有主机名、IP地址、端口号、状态页url和健康检查url等用于服务注册与发现的重要信息。

开发者可以自定义元数据，这部分额外的数据可以通过键值对(key-value)的形式放在eureka.instance.metadataMap，如下所示：

```yml
eureka:
    instance:
        metadataMap:
                metadata-map
                    mymetaData: mydata
```

这里定义了一个键为mymetaData，值为mydata的自定义元数据，metadata-map在EurekaInstanceConfigBean会被配置为以下的属性：

```java
private Map <String, String> metadataMap = new HashMap<>();
```

这些自定义的元数据可以按照自身业务需要或者根据其他的特殊需要进行定制。

