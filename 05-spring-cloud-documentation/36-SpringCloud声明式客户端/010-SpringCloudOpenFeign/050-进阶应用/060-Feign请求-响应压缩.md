# 060-Feign请求-响应压缩

```
可以通过下面的属性配置来让OpenFeign在发送请求时进行GZIP压缩：
feign.compression.request.enabled=true
feign.compression.response.enabled=true

OpenFeign的压缩配置属性和一般的Web Server配置类似。这些属性允许选择性地压缩某种类型的请求并设置最小的请求阈值，配置如下所示：
你也可以使用FeignContentGzipEncodingInterceptor来实现请求的压缩，需要在自定义配置文件中初始化该类型的实例，供OpenFeign使用，具体实现如下所示：
feign.compression.request.enabled=true
feign.compression.request.mime-types=text/xml,application/xml,application/json
feign.compression.request.min-request-size=2048
```

