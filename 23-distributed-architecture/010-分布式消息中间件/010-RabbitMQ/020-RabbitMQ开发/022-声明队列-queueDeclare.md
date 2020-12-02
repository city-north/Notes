# 022-声明队列-queueDeclare

queueDeclare相对于exchangeDeclare方法而言，重载方法的个数就少很多，它只有两个重载方法：

```java
Queue.DeclareOk queueDeclare() throws IOException;
Queue.DeclareOk queueDeclare(String queue,boolean durable,boolean exclusive,boolean autoDelete,Map<String,Object>arguments) throws IOException;
```

不带任何参数的queueDeclare方法默认创建一个由RabbitMQ命名的（类似这种amq.gen-LhQz1gv3GhDOv8PIDabOXA名称，这种队列也称之为匿名队列）、排他的、自动删除的、非持久化的队列。

方法的参数详细说明如下所述。

|      | 参数       | 队列                                                         |
| ---- | ---------- | ------------------------------------------------------------ |
| 1    | queue      | 队列的名称                                                   |
| 2    | durable    | 设置是否持久化。为true则设置队列为持久化。持久化的队列会存盘，在服务器重启的时候可以保证不丢失相关信息。 |
| 3    | exclusive  | 设置是否排他。为true则设置队列为排他的。如果一个队列被声明为排他队列，该队列仅对首次声明它的连接可见，并在连接断开时自动删除。这里需要注意三点：排他队列是基于连接（Connection）可见的，同一个连接的不同信道（Channel）是可以同时访问同一连接创建的排他队列；“首次”是指如果一个连接已经声明了一个排他队列，其他连接是不允许建立同名的排他队列的，这个与普通队列不同；即使该队列是持久化的，一旦连接关闭或者客户端退出，该排他队列都会被自动删除，这种队列适用于一个客户端同时发送和读取消息的应用场景。 |
| 4    | autoDelete | 设置是否自动删除。为true则设置队列为自动删除。自动删除的前提是：至少有一个消费者连接到这个队列，之后所有与这个队列连接的消费者都断开时，才会自动删除。不能把这个参数错误地理解为：“当连接到此队列的所有客户端断开时，这个队列自动删除”，因为生产者客户端创建这个队列，或者没有消费者客户端与这个队列连接时，都不会自动删除这个队列。 |
| 5    | arguments  | 设置队列的其他一些参数，如x-message-ttl、x-expires、x-max-length、x-max-length-bytes、x-dead-letter-exchange、x-dead-letter-routing-key、x-max-priority等。 |

#### 注意要点

生产者和消费者都能够使用queueDeclare来声明一个队列，但是如果消费者在同一个信道上订阅了另一个队列，就无法再声明队列了。必须先取消订阅，然后将信道置为“传输”模式，之后才能声明队列。

对应于exchangeDeclareNoWait方法，这里也有一个queueDeclareNoWait方法：

```java
    void queueDeclareNoWait(String queue, boolean durable, boolean exclusive, boolean autoDelete,
                            Map<String, Object> arguments) throws IOException;
```



在调用完queueDeclareNoWait方法之后，紧接着使用声明的队列时有可能会发生异常情况。
同样这里还有一个queueDeclarePassive的方法，也比较常用。这个方法用来检测相应的队列是否存在。如果存在则正常返回，如果不存在则抛出异常：404 channel exception，同时Channel也会被关闭。方法定义如下：

```java
Queue.DeclareOk queueDeclarePassive(String queue) throws IOException;
```

与交换器对应，关于队列也有删除的相应方法：

```java
Queue.DeleteOk queueDelete(String queue) throws IOException;
Queue.DeleteOk queueDelete(String queue,boolean ifUnused,boolean ifEmpty) throws IOException;
void queueDeleteNoWait(String queue,boolean ifUnused,boolean ifEmpty) throws IOException;
```

- ifEmpty设置为true表示在队列为空（队列里面没有任何消息堆积）的情况下才能够删除。

与队列相关的还有一个有意思的方法——queuePurge，区别于queueDelete，这个方法用来清空队列中的内容，而不删除队列本身，具体定义如下：

```java
    Queue.PurgeOk queuePurge(String queue) throws IOException;
```



















如果要在应用中共享一个队列，可以做如下声明

```java
//声明交换器
channel.exchangeDeclare(exchangeName, "direct", true);
//声明队列
channel.queueDecleare(queueName, true, false, false, null);
//队列绑定
channel.queueBind(queueName, exchangeName, routingKey);
```

这里的队列被声明为持久化的、非排他的、非自动删除的，而且也被分配另一个确定的已知的名称（由客户端分配而非RabbitMQ自动生成）。

注意：Channel的API方法都是可以重载的，比如exchangeDeclare、queueDeclare。根据参数不同，可以有不同的重载形式，根据自身的需要进行调用。
生产者和消费者都可以声明一个交换器或者队列。

如果尝试声明一个已经存在的交换器或者队列，只要声明的参数完全匹配现存的交换器或者队列，RabbitMQ就可以什么都不做，并成功返回。如果声明的参数不匹配则会抛出异常。

