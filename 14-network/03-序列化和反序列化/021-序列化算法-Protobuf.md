# ProtoBuf

> https://developers.google.com/protocol-buffers

## Protocol buffers特点

Protocol Buffers(简称 Protobuf) 是一种高效,轻便,易用的结构化数据存储格式,它与平台无关,与语言无关,拓展性强,广泛用于通讯协议和数据存储等领域

数据存储格式使用最多的是

-  JSON
- XML

因为他们可读性强

## 为什么我们需要 protobuf

1. Protobuf 具有 JSON/xml 格式的所有优点,且易于拓展
2. Protobuf 解析速度非常快
   - 是 xml 的 20-100 倍
   - 是 JSON 的 3-5 倍
3. 序列化数据非常紧凑

> 与 xml 比大概体积是 xml 的 10%-30%

## Protobuf 序列化的原理

protobuf 的基本应用

使用 protobuf 开发的一般步骤是

1. 配置开发环境，安装 protocol compiler 代码编译器
2. 编写.proto 文件，定义序列化对象的数据结构
3. 基于编写的.proto 文件，使用 protocol compiler 编译器生成对应的序列化/反序列化工具类
4. 基于自动生成的代码，编写自己的序列化应用