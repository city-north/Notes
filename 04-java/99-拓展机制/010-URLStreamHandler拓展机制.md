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

 [031-SpringBoot可执行jar运行原理.md](../../03-spring-boot-documentation/020-理解独立的Spring应用/031-SpringBoot可执行jar运行原理.md) 

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

## 原理



```kava
  //java.net.URL
  static URLStreamHandler getURLStreamHandler(String protocol) {

        URLStreamHandler handler = handlers.get(protocol);
        if (handler == null) {

            boolean checkedWithFactory = false;

            // Use the factory (if any)
            if (factory != null) {
                handler = factory.createURLStreamHandler(protocol);
                checkedWithFactory = true;
            }

            // Try java protocol handler
            if (handler == null) {
                String packagePrefixList = null;

                packagePrefixList
                    = java.security.AccessController.doPrivileged(
                    new sun.security.action.GetPropertyAction(
                        protocolPathProp,""));
                if (packagePrefixList != "") {
                    packagePrefixList += "|";
                }

                // REMIND: decide whether to allow the "null" class prefix
                // or not.
                packagePrefixList += "sun.net.www.protocol";

                StringTokenizer packagePrefixIter =
                    new StringTokenizer(packagePrefixList, "|");

                while (handler == null &&
                       packagePrefixIter.hasMoreTokens()) {

                    String packagePrefix =
                      packagePrefixIter.nextToken().trim();
                    try {
                        String clsName = packagePrefix + "." + protocol +
                          ".Handler";
                        Class<?> cls = null;
                        try {
                            cls = Class.forName(clsName);
                        } catch (ClassNotFoundException e) {
                            ClassLoader cl = ClassLoader.getSystemClassLoader();
                            if (cl != null) {
                                cls = cl.loadClass(clsName);
                            }
                        }
                        if (cls != null) {
                            handler  =
                              (URLStreamHandler)cls.newInstance();
                        }
                    } catch (Exception e) {
                        // any number of exceptions can get thrown here
                    }
                }
            }

            synchronized (streamHandlerLock) {

                URLStreamHandler handler2 = null;

                // Check again with hashtable just in case another
                // thread created a handler since we last checked
                handler2 = handlers.get(protocol);

                if (handler2 != null) {
                    return handler2;
                }

                // Check with factory if another thread set a
                // factory since our last check
                if (!checkedWithFactory && factory != null) {
                    handler2 = factory.createURLStreamHandler(protocol);
                }

                if (handler2 != null) {
                    // The handler from the factory must be given more
                    // importance. Discard the default handler that
                    // this thread created.
                    handler = handler2;
                }

                // Insert this handler into the hashtable
                if (handler != null) {
                    handlers.put(protocol, handler);
                }

            }
        }

        return handler;

    }

```

