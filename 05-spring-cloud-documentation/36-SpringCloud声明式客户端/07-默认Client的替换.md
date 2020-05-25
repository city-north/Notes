# 默认 Client 的替换

默认情况下,使用的是 JDK 原生的 URLCnnection 发送 HTTP 请求.没有连接池,但是对每个地址会保持一个长链接,即利用 HTTP的 persistece connection 

我们可以用用 Apache 的 HTTP Client 替换 Feign 原有的 HTTP Client , 通过设置连接池,超时时间等对服务之间的调用调优

- HttpClient 替换 URLConnection
- okHttp 替换 feign 默认的 Client

## 使用 Http Client 替换 Feign 默认 Client

```
        <!-- 使用Apache HttpClient替换Feign原生httpclient -->
        <dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpclient</artifactId>
        </dependency>

        <dependency>
            <groupId>com.netflix.feign</groupId>
            <artifactId>feign-httpclient</artifactId>
            <version>8.17.0</version>
        </dependency>
```

application.yml

```
feign:
  httpclient:
      enabled: true
```

## 使用okhttp

http 是目前通用的网络请求方式,用来访问请求交换数据,有效地使用 HTTP 可以使应用访问速度变得更快,更节省带宽,okhttp 具有以下功能和特性

- 只是 SPDY ,可以合并多个到同一个主机的请求
- 使用连接池技术减少请求的延迟,如果 SPDY 可用的话
- 使用 GZIP 压缩减少传输的数据量
- 避免相应重复的网络请求

```
feign:
    httpclient:
         enabled: false
    okhttp:
         enabled: true
```

OkHttpClient 是 okhttp 的核心功能的执行者,可以通过 new 创建

```
@Configuration
@ConditionalOnClass(Feign.class)
@AutoConfigureBefore(FeignAutoConfiguration.class)
public class FeignOkHttpConfig {
    @Bean
    public okhttp3.OkHttpClient okHttpClient(){
        return new okhttp3.OkHttpClient.Builder()
                 //设置连接超时
                .connectTimeout(60, TimeUnit.SECONDS)
                //设置读超时
                .readTimeout(60, TimeUnit.SECONDS)
                //设置写超时
                .writeTimeout(60,TimeUnit.SECONDS)
                //是否自动重连
                .retryOnConnectionFailure(true)
                .connectionPool(new ConnectionPool())
                //构建OkHttpClient对象
                .build();
    }

}
```