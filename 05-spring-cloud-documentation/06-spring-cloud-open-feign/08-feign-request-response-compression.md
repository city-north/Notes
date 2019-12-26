# Feign request/response compression

您可以考虑为您的Feign请求启用请求或响应GZIP压缩。你可以通过启用其中一个属性来做到这一点:

```
feign.compression.request.enabled=true
feign.compression.response.enabled=true
```

佯装请求压缩给你类似的设置，你可以为你的web服务器:

```java
feign.compression.request.enabled=true
feign.compression.request.mime-types=text/xml,application/xml,application/json
feign.compression.request.min-request-size=2048
```

这些属性允许您选择压缩媒体类型和最小请求阈值长度。

对于除OkHttpClient外的http客户端，可以启用默认的gzip解码器对gzip响应进行ISO-8859-1编码解码:

```
---
feign.compression.response.enabled=true
feign.compression.response.useGzipDecoder=true
---
```

