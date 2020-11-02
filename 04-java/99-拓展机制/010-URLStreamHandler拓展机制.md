# 010-URLStreamHandler拓展机制

URL的关联协议(Protocool)对应 URLStreamHandler 实现类, JDK默认支持一下格式

| 格式  | 描述      | 实现类                               |
| ----- | --------- | ------------------------------------ |
| FILE  | 文件格式  | `sun.net.www.protocol.file.Handler`  |
| JAR   | jar包格式 | `sun.net.www.protocol.jar.Handler`   |
| HTTP  | HTTP      | `sun.net.www.protocol.http.Handler`  |
| HTTPS | HTTPS     | `sun.net.www.protocol.https.Handler` |
| FTP   | FTP       | `sun.net.www.protocol.ftp.Handler`   |

## 如何拓展

如果我们要拓展一个URL的读取方式

- 继承 URLStreamHandler

- 配置java系统属性(System.getProperties())

  ```
  设置 key =  java.protocl.handler.pkgs , 以 | 分隔多个包
  ```

## 典型

SpringBoot的引导包中的JarFIle

`org.springframework.boot.loader.jar.JarFile#registerUrlProtocolHandler`

```java
	/**
	 * Register a {@literal 'java.protocol.handler.pkgs'} property so that a
	 * {@link URLStreamHandler} will be located to deal with jar URLs.
	 */
	public static void registerUrlProtocolHandler() {
		String handlers = System.getProperty(PROTOCOL_HANDLER, "");
		System.setProperty(PROTOCOL_HANDLER, ("".equals(handlers) ? HANDLERS_PACKAGE
				: handlers + "|" + HANDLERS_PACKAGE));
		resetCachedUrlHandlers();
	}
```

由于我们知道SpringBoot当jar包归档到了

```
BOOT-INF/lib 
```

包中, 所以它需要自定义一个UrlStreamHandler 去加载这些jar包到classpath