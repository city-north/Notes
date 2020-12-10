# 16-Java资源管理

| 职责         | 说明                                                         |
| ------------ | ------------------------------------------------------------ |
| 面向资源     | 文件系统<br />artifact(jar,war, ear文件) 远程资源            |
| API整合      | java.lang.ClassLoader#getResource<br />java.io.File, <br />java.net.URL |
| 资源定位     | java.net.URL<br />java.net.URI                               |
| 面向流式存储 | java.net.URLConnection                                       |
| 协议拓展     | java.net.URLStreamHandler<br />java.net.URLStreamHandlerFactory |

