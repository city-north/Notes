# 构造Socket

## 构造方法

```
public Socket() 
public Socket(Proxy proxy)
protected Socket(SocketImpl impl)
public Socket(String host, int port)
public Socket(InetAddress address, int port) throws IOException
public Socket(String host, int port, InetAddress localAddr,int localPort) throws IOException 
```

除了第一个无参的构造方法,其他的构造方法都会尝试与服务器建立连接

- 如果连接成功,就会返回Socket对象
- 如果失败,就会抛出IOException

