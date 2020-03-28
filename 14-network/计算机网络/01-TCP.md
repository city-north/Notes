# TCP 协议

## 再复习分层模型

![image-20200328155305774](assets/image-20200328155305774.png)

## TCP 协议

TCP 协议属于传输层,提供可靠的字节流服务

**TCP 协议为了更容易传送大数据才把数据进行分割,而且 TCP 协议能够确定数据是否送达到对方**

所谓字节流服务,实际上就是为了方便传输,把大块的数据分割成报文段(segment) 为单位的数据包进行管理

#### 确保数据能够到达目标

使用 **三次握手**, 和 **四次挥手**

## 三次握手

![img](https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=3233953725,3136815440&fm=173&app=25&f=JPEG?w=580&h=708&s=FD843C720B1A764D52D554DA0000E0B1)

- 客户端发送 SYN (seq = x) 报文给服务端,进入 **SYN_SEND** 状态

- 服务端接收到 SYN 报文,回应一个 SYN(seq = y) 和 ACK(axk = x+1) 报文, 进入 **SYN_RECV** 状态

- 客户端收到服务器端的 SYN 报文,回应一个 ACK(ack = y+1)报文

- 客户端收到服务端的 SYN报文, 回应一个 ACK (ack = y+1) 报文,进入 Established 状态

  动态图

  <img src="assets/640.gif" alt="img" style="zoom:50%;" />

两个中间状态，**syn_sent**和**syn_rcvd**，这两个状态叫着「半打开」状态，就是向对方招手了，但是还没来得及看到对方的点头微笑。**syn_sent**是主动打开方的「半打开」状态，**syn_rcvd**是被动打开方的「半打开」状态。客户端是主动打开方，服务器是被动打开方。

- syn_sent: syn package has been sent
- syn_rcvd: syn package has been received

## TCP 数据传输

TCP 数据传输就是两个人隔空对话，差了一点距离，所以需要对方反复确认听见了自己的话。

- 连接建立
- Client 发送 data 
- Server 收到数据  发送 ack 给客户端
- Server 完成数据处理, 发送返回数据给客户端
- 客户端接收到数据,发送 ack 给客户端

<img src="assets/640-20200328195414887.gif" alt="img" style="zoom:50%;" />



### 为什么是三次握手,不是两次或者是四次

## TCP 四次挥手

客户端和服务端都可以主动断开连接

大致上分为四步

- 客户端发送终止标志位 fin=1 seq = u 到服务端, 客户端开始进入 fin-wait-1状态
- 服务端接收到 fin 消息后返回一个 ACK =1 , ack = u+1 , seq=v 的消息给客户端,表示接受请求 , 服务端进入 close-wait 状态
- 客户端收到消息后, 处于 fin_wait_2 状态
- 服务端发送一个终止标志位(fin = 1, ACK = 1,seq = w, ack = u+1)的消息给客户端,表示关闭链路前服务器需要向客户端发送的消息已经发送完毕,此时服务端进入 last_ack 状态
- 客户端接收到这个最终的 fin 消息, 发送一个 ACK=1,sql=u+1 ack = w+1的消息给服务端,此时客户端处于 TIME_WAIT 状态,等待计时器(2MSL)设置的时间后,客户端进入 close 状态

<img src="assets/640-20200328195712940.gif" alt="img" style="zoom:50%;" />

