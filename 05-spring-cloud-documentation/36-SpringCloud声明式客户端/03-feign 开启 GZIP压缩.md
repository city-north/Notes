# Feign开启 GZIP压缩

Spring Cloud Feign 可以对请求和相应进行 GZIP 压缩,以提高通讯效率

- application.properties:

```
feign:
    compression:
        request:
            enabled: true
            mime-types: text/xml,application/xml,application/json # 配置压缩支持的MIME TYPE
            min-request-size: 2048  # 配置压缩数据大小的下限
        response:
            enabled: true # 配置响应GZIP压缩

```

开启 gzip 压缩之后,feign 之间的调用通过二进制协议传播,所以返回值需要修改为 

`ResponseEntity<byte[]> `才能正常显示,否则会导致服务之间的调用结果乱码

```java
@FeignClient(name = "github-client", url = "https://api.github.com", configuration = HelloFeignServiceConfig.class)
public interface HelloFeignService {

    /**
     * content: {"message":"Validation Failed","errors":[{"resource":"Search","field":"q","code":"missing"}],
     * "documentation_url":"https://developer.github.com/v3/search"}
     * @param queryStr
     * @return
     */
    @RequestMapping(value = "/search/repositories", method = RequestMethod.GET)
    ResponseEntity<byte[]> searchRepo(@RequestParam("q") String queryStr);

}

```





