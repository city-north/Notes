# 010-LengthFieldBasedFrameDecoder

[TOC]

# 构造参数

```java
        final LengthFieldBasedFrameDecoder spliter =
                new LengthFieldBasedFrameDecoder(
                1024,//发送的数据包的最大长度,1024字节
                0,
                4,
                0,
                4);
```



```java
public LengthFieldBasedFrameDecoder{
            int maxFrameLength, //发送的数据包的最大长度
            int lengthFieldOffset,//长度字段偏移量
            int lengthFieldLength,//长度字段自己占用的字节数
            int lengthAdjustment,//长度字段的偏移量矫正
            int initialBytesToStrip//丢弃的起始字节数
}
```

- int maxFrameLength , 发送的数据包的最大长度
- lengthFieldOffset ,长度字段偏移量 如果为0代表长度字段是放在最前面的
- lengthFieldLength, 长度字段的长度为4字节, 即表示内容长度的值占用数据包的4个字节, 表示内容长度的值占用数据包的4个字节
- lengthAdjustment, 长度矫正, 公式为: 内容字段偏移量 - 长度字段偏移量 - 长度字段的字节数, 4-0-4= 0
- initialBytesToStrip , 丢弃的起始字节数, 表示获取到最终内容的Content的字节数据时, 抛弃最前面的4个字节的数据

## 官方实例中的7个例子

### 例子1不剥离header的场景

```java
lengthFieldOffset   = 0   // 长度偏移为0, 因为长度字段, 从第一个字节开始
lengthFieldLength   = 2   // 长度字段的长度为2个字节
lengthAdjustment    = 0		// 长度矫正,长度字段和Actual Content的校正
initialBytesToStrip = 0 (= do not strip header) // 不剥离header
  
   BEFORE DECODE (14 bytes)         AFTER DECODE (14 bytes)
   +--------+----------------+      +--------+----------------+
   | Length | Actual Content |----->| Length | Actual Content |
   | 0x000C | "HELLO, WORLD" |      | 0x000C | "HELLO, WORLD" |
   +--------+----------------+      +--------+----------------+
```

### 例子2剔除长度域

```java
lengthFieldOffset   = 0  // 长度偏移为0, 因为长度字段, 从第一个字节开始
lengthFieldLength   = 2  // 长度字段的长度为2个字节
lengthAdjustment    = 0
initialBytesToStrip = 2 (= the length of the Length field) //剥离header
  
   BEFORE DECODE (14 bytes)         AFTER DECODE (12 bytes)
   +--------+----------------+      +----------------+
   | Length | Actual Content |----->| Actual Content |
   | 0x000C | "HELLO, WORLD" |      | "HELLO, WORLD" |
   +--------+----------------+      +----------------+
  

  
  
```

### 例子3

lengthAdjustment 调整长度和length占的字节数相同, 这样就可以左移显示length

```java
lengthFieldOffset   =  0
lengthFieldLength   =  2
lengthAdjustment    = -2 (= the length of the Length field)
initialBytesToStrip =  0
  
   BEFORE DECODE (14 bytes)         AFTER DECODE (14 bytes)
   +--------+----------------+      +--------+----------------+
   | Length | Actual Content |----->| Length | Actual Content |
   | 0x000E | "HELLO, WORLD" |      | 0x000E | "HELLO, WORLD" |
   +--------+----------------+      +--------+----------------+
```

### 例子4

length长度为2

length长度为3字节

矫正0

剔除0

```java
   lengthFieldOffset   = 2 (= the length of Header 1)
   lengthFieldLength   = 3
   lengthAdjustment    = 0
   initialBytesToStrip = 0
  
   BEFORE DECODE (17 bytes)                      AFTER DECODE (17 bytes)
   +----------+----------+----------------+      +----------+----------+----------------+
   | Header 1 |  Length  | Actual Content |----->| Header 1 |  Length  | Actual Content |
   |  0xCAFE  | 0x00000C | "HELLO, WORLD" |      |  0xCAFE  | 0x00000C | "HELLO, WORLD" |
   +----------+----------+----------------+      +----------+----------+----------------+
```

### 例子5

length长度为3

校准为2

```
lengthFieldOffset   = 0
lengthFieldLength   = 3
lengthAdjustment    = 2 (= the length of Header 1)
initialBytesToStrip = 0
  
   BEFORE DECODE (17 bytes)                      AFTER DECODE (17 bytes)
   +----------+----------+----------------+      +----------+----------+----------------+
   |  Length  | Header 1 | Actual Content |----->|  Length  | Header 1 | Actual Content |
   | 0x00000C |  0xCAFE  | "HELLO, WORLD" |      | 0x00000C |  0xCAFE  | "HELLO, WORLD" |
   +----------+----------+----------------+      +----------+----------+----------------+
```

#### 例子6

因为length长度为2, 矫正为1 , 所以吧HDR2概括进去

```
   lengthFieldOffset   = 1 (= the length of HDR1)
   lengthFieldLength   = 2
   lengthAdjustment    = 1 (= the length of HDR2)
   initialBytesToStrip = 3 (= the length of HDR1 + LEN)
  
   BEFORE DECODE (16 bytes)                       AFTER DECODE (13 bytes)
   +------+--------+------+----------------+      +------+----------------+
   | HDR1 | Length | HDR2 | Actual Content |----->| HDR2 | Actual Content |
   | 0xCA | 0x000C | 0xFE | "HELLO, WORLD" |      | 0xFE | "HELLO, WORLD" |
   +------+--------+------+----------------+      +------+----------------+
```

#### 例子7

长度为2, 矫正为-3, 回到最开始, 然后在剔除3个, 那么抵消掉

```
   lengthFieldOffset   =  1
   lengthFieldLength   =  2
   lengthAdjustment    = -3 (= the length of HDR1 + LEN, negative)
   initialBytesToStrip =  3
  
   BEFORE DECODE (16 bytes)                       AFTER DECODE (13 bytes)
   +------+--------+------+----------------+      +------+----------------+
   | HDR1 | Length | HDR2 | Actual Content |----->| HDR2 | Actual Content |
   | 0xCA | 0x0010 | 0xFE | "HELLO, WORLD" |      | 0xFE | "HELLO, WORLD" |
   +------+--------+------+----------------+      +------+----------------+
```

```
帧头	帧类型				数据长度	数据内容	
0xEF	1字节			2个字节	N个字节	
```

