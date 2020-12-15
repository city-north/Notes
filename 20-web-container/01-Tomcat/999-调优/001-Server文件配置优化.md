# Server文件配置

- Executor线程池优化



## Connector优化

```xml
<Connector executor="tomcatThreadPool"
           port="8080" 
           protocol="org.apache.coyote.http11.Http11Nio2Protocol"
           connectionTimeout="20000"
           maxConnections="10000"
           enableLookups="false"
           acceptCount="100"
           maxPostSize="10485760"
           maxHttpHeaderSize="65536"
           compression="on"
           disableUploadTimeout="false"
           compressionMinSize="2048"
           noCompressionUserAgents="gozilla, traviata"
           acceptorThreadCount="2"
           processorCache="20000"
           tcpNoDelay="true"
           connectionLinger="5"
           URIEncoding="utf=8"
   				compressableMimeType="text/html,text/xml,text/plain,text/css,text/javascript,text/json,application/x-javascript,application/javascript,application/json"
           redirectPort="8443" />
```



|      | 属性名               | 设置 | 解释                                                         |
| ---- | -------------------- | ---- | ------------------------------------------------------------ |
| 1    | protocol             |      | tomcat8以上使用`org.apache.coyote.http11.Http11Nio2Protocol`，效率会更高 |
| 2    | connectionTimeout    |      | Connector接受一个连接后等待的时间(milliseconds)，默认值是60000 |
| 3    | maxConnections       |      | 这个值表示最多可以有多少个socket连接到tomcat上               |
| 4    | enableLookups        |      | 禁用DNS查询                                                  |
| 5    | acceptCount          |      | 当tomcat起动的线程数达到最大时，接受排队的请求个数，默认值为100 |
| 6    | maxPostSize          |      | 设置由容器解析的URL参数的最大长度，-1(小于0)为禁用这个属性，默认为2097152(2M) 请注意， FailedRequestFilter 过滤器可以用来拒绝达到了极限值的请求。 |
| 7    | maxHttpHeaderSize    |      | http请求头信息的最大程度，超过此长度的部分不予处理。一般8K   |
| 8    | compression          |      | 是否启用GZIP压缩 on为启用（文本数据压缩） off为不启用， force 压缩所有数据 |
| 9    | disableUploadTimeout |      | 这个标志允许servlet容器使用一个不同的,通常长在数据上传连接超时。如果不指定,这个属性被设置为true,表示禁用该时间超时 |
| 10   | compressionMinSize   |      | 当超过最小数据大小才进行压缩                                 |
| 11   | acceptorThreadCount  |      | 用于接受连接的线程数量。增加这个值在多CPU的机器上,尽管你永远不会真正需要超过2。也有很多非维持连接,您可能希望增加这个值。默认值是1 |
| 12   | processorCache       |      | 协议处理器缓存的处理器对象来提高性能。该设置决定多少这些对象的缓存。-1意味着无限的,默认是200。如果不使用Servlet 3.0异步处理,默认是使用一样的maxThreads设置。如果使用Servlet 3.0异步处理,默认是使用大maxThreads和预期的并发请求的最大数量(同步和异步) |
| 13   | tcpNoDelay           |      | 如果设置为true,TCP_NO_DELAY选项将被设置在服务器套接字,而在大多数情况下提高性能。这是默认设置为true |
| 14   | connectionLinger     |      | 秒数在这个连接器将持续使用的套接字时关闭。默认值是 -1,禁用socket 延迟时间 |
| 15   | URIEncoding          |      | 网站一般采用UTF-8作为默认编码                                |
| 16   | server               |      | 隐藏Tomcat版本信息，首先隐藏HTTP头中的版本信息               |
| 17   | compressableMimeType |      | 配置想压缩的数据类型                                         |

## Host标签

