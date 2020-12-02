# exchangeDeclare方法详解

- [方法参数](#方法参数)
- [声明一个共享队列](#声明一个共享队列)

这个方法的返回值是Exchange.DeclareOK，用来标识成功声明了一个交换器。
各个参数详细说明如下所述。

## 方法参数

| 序号 | 参数       | 解释                                                         |
| ---- | ---------- | ------------------------------------------------------------ |
| 1    | exchange   | 交换器的名称                                                 |
| 2    | type       | 交换器的类型，常见的如fanout、direct、topic                  |
| 3    | durable    | 设置是否持久化。durable设置为true表示持久化，反之是非持久化。持久化可以将交换器存盘，在服务器重启的时候不会丢失相关信息。 |
| 4    | autoDelete | 设置是否自动删除。autoDelete设置为true则表示自动删除。自动删除的前提是至少有一个队列或者交换器与这个交换器绑定，之后所有与这个交换器绑定的队列或者交换器都与此解绑。注意不能错误地把这个参数理解为：“当与此交换器连接的客户端都断开时，RabbitMQ会自动删除本交换器”。 |
| 5    | internal   | 设置是否是内置的。如果设置为true，则表示是内置的交换器，客户端程序无法直接发送消息到这个交换器中，只能通过交换器路由到交换器这种方式。 |
| 6    | argument   | 其他一些结构化参数，比如alternate-exchange                   |

exchangeDeclare的其他重载方法如下：

```java
Exchange.DeclareOk exchangeDeclare(String exchange, String type) throws IOException;
Exchange.DeclareOk exchangeDeclare(String exchange, String type, boolean durable) throws IOException;
Exchange.DeclareOk exchangeDeclare(String exchange, String type, boolean durable, boolean autoDelete, Map<String,Object>;arguments) throws IOException;
```

与此对应的，将第二个参数String type换成BuiltInExchangeType type对应的几个重载方法（不常用）：

```java
Exchange.DeclareOk exchangeDeclare(String exchange,BuiltinExchangeType type) throws IOException;
Exchange.DeclareOk exchangeDeclare(String exchange,BuiltinExchangeType type,boolean durable) throws IOException;
Exchange.DeclareOk exchangeDeclare(String exchange,BuiltinExchangeType type,boolean durable,boolean autoDelete,Map<String,Object>) throws IOException;
Exchange.DeclareOk exchangeDeclare(String exchange,BuiltinExchangeType type,boolean durable,boolean autoDelete,boolean internal,Map<String,Object>arguments) throws IOException;
```

与exchangeDeclare师出同门的还有几个方法，比如exchangeDeclareNoWait方法，具体定义如下（当然也有BuiltExchangeType版的，这里就不展开了）

```java
void exchangeDeclareNoWait(String exchange,
													 String type,
													 boolean durable,
													 boolean autoDelete,
													 boolean internal,
													 Map<String,Object> arguments
) throws IOException;
```

这个exchangeDeclareNoWait比exchangeDeclare多设置了一个nowait参数，这个nowait参数指的是AMQP中Exchange.Declare命令的参数，意思是不需要服务器返回，注意这个方法的返回值是void，而普通的exchangeDeclare方法的返回值是Exchange.DeclareOk，意思是在客户端声明了一个交换器之后，需要等待服务器的返回（服务器会返回Exchange.Declare-Ok这个AMQP命令）。

针对“exchangeDeclareNoWait不需要服务器任何返回值”这一点，考虑这样一种情况，在声明完一个交换器之后（实际服务器还并未完成交换器的创建），那么此时客户端紧接着使用这个交换器，必然会发生异常。如果没有特殊的缘由和应用场景，并不建议使用这个方法。

这里还有师出同门的另一个方法exchangeDeclarePassive，这个方法的定义如下：

```java
ExchangeDeclareOk exchangeDeclarePassive(String name) throws IOException
```

这个方法在实际应用过程中还是非常有用的，它主要用来检测相应的交换器是否存在。如果存在则正常返回；如果不存在则抛出异常：404 channel exception，同时Channel也会被关闭。

有声明创建交换器的方法，当然也有删除交换器的方法。相应的方法如下：

```java
Exchange.DeleteOk exchangeDelete(String exchange) throws IOException;
void exchangeDeleteNoWait(String exchange,boolean ifUnused) throws IOException;
Exchange.DeleteOk exchangeDelete(String exchange,boolean ifUnused) throws IOException;
```


其中exchange表示交换器的名称，而ifUnused用来设置是否在交换器没有被使用的情况下删除。

如果isUnused设置为true，则只有在此交换器没有被使用的情况下才会被删除；如果设置false，则无论如何这个交换器都要被删除。


