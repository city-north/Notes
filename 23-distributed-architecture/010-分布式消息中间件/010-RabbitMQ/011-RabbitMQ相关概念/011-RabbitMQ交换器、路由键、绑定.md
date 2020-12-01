# 011-RabbitMQ交换器、路由键、绑定

- Exchange交换器
- RoutingKey路由键
- Binding绑定

## Exchange交换器

交换器，生产者将消息发送到Exchange，由交换器将消息路由到一个或者多个队列中，

如果路由不到

- 返回给生产者
- 直接丢弃

## RoutingKey路由键

路由键，生产者将消息发给交换器的时候，一般会指定一个RoutingKey，用来指定这个消息的路由规则，而这个RoutingKey需要与交换器类型和绑定键（BindingKey）联合使用才能最终生效

在交换器类型和绑定键（BingingKey）固定的情况下，生产者可以在发送消息给交换器时，通过指定RoutingKey来决定消息流向哪里

## Binding绑定

RabbitMQ中通过绑定将交换器与队列关联起来，在绑定的时候一般会指定一个绑定键（BingingKey)，这样RabbitMQ就知道如何正确的将消息路由到队列了

生产者发送消息给交换器Exchange时，需要一个RoutingKey,

- 当BindingKey 和RoutingKey相匹配时，消息会被路由到队列中
- 并不是所有类型的交换器都根据BindingKey,比如fanout类型的交换器就会无视BindingKey，而是将消息路由到所有绑定到该交换器队列中

形象的比喻

- 交换器Exchange相当于投递包裹的邮箱
- RoutingKey相当于填写在包裹上的地址
- BindingKey相当于包裹的目的地
  - 当RoutingKey和BindingKey匹配时，投递
  - 当不匹配时，退回或丢弃

```java
//声明一个 direct类型的交换器
channel.exchangeDeclare(EXCHANGE_NAME, "direct", true, false, null);
//声明一个队列
channel.queueDeclare(QUEUE_NAME, true, false, false, null);
//使用ROUTING_KEY 去绑定交换器和队列
channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

String message = "HelloWorld";
channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, 
                  MessageProperties.PERSISTENT_TEXT_PLAIN,
                  message.getBytes());
```

- 在direct类型交换器下， RoutingKey和BindingKey要完全一致才能使用

## 交换器类型

RabbitMQ常用的交换器类型有

- fanout
- direct
- topic
- headers

#### fanout

会把所有发送到该交换器的消息路由到所有与该交换器绑定的队列中

#### direct

direct类型的交换器路由规则也很简单，它会把消息路由到那些BindingKey和RoutingKey完全匹配的队列中

#### topic

#### headers



## 