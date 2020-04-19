# Stream

5.0 发布的特性,是一种 Redis 数据结构,是一个支持多播的可持久化消息队列

![image-20200419192728137](assets/image-20200419192728137.png)

- 每个Stream 都可以挂多个消费组 (Consumer Group),每个消费组会有个游标 last_delivered_id 在 Stream 数组在之上往前移动,标识当前消费组已经消费到哪条消息了
- 每个消费组都有一个Stream 内唯一的名称,消费组不会自动创建,需要单独使用指令 xgroup create 进行创建,需要指定从 Stream 的某个消息 ID 开始消费,这个 ID 用来初始化 last_delivered_id变量
- 同一个消费组可以挂接多个消费者(Consumer), 这些消费者是竞争关系,任何一个消费者读取到了消息都会使游标 last_delivered_id 往前移动,每个 消费者都有一个组内唯一的名称
- 消费者内部会有一个状态变量 pendIng_ids ,它记录了当前已经被客户端读取,但是爱没有 ack的消息
- 如果客户端没有 ack,这个变量里面的消息 ID 就会越来越多
- 一旦某个消息被 ack, 它就开始减少,这个 pending_ids 变量在 Redis 官方被称为 PEL, 也就是 Pending Entries List , 这是一个核心的数据结构,它用来保存客户端至少消费了消息一次,而不会在网络传输的中途丢失了而没被处理

## 消息 ID

消息 ID 的格式为

```
timetampInMillis-sequence, 例如 1527846880572-5
```

标识当前的消息在毫秒时间戳1527846880572 时候产生,并且是在毫秒内产生了 第 5 条数据

消息 ID 可以由客户端自己生成,也可以由客户端自己制定,但是形式必须是 整数-整数,而且后面加入的消息 ID 要大于前面的消息 ID

## 消息内容

键值对

## 增删改查

- xadd : 向 Stream 追加消息
- xdel : 从 Stream 中删除消息, 这里的删除仅仅是设置标志位,不影响消息总长度
- xrange : 获取 Stream 中的消息队列, 会自动过滤已经删除的消息
- xlen : 获取 Stream 消息长度
- del : 删除整个 Stream 消息队列中的所有消息

```
127.0.0.1:6379> xadd stream * name ericchen age 25
"1585692396423-0"
127.0.0.1:6379> xadd stream * name ericchen2 age 26
"1585692407487-0"
127.0.0.1:6379> xadd stream * name ericchen3 age 27
"1585692412571-0"
127.0.0.1:6379> xlen stream
(integer) 3
127.0.0.1:6379> xrange stream - +
1) 1) "1585692396423-0"
   2) 1) "name"
      2) "ericchen"
      3) "age"
      4) "25"
2) 1) "1585692407487-0"
   2) 1) "name"
      2) "ericchen2"
      3) "age"
      4) "26"
3) 1) "1585692412571-0"
   2) 1) "name"
      2) "ericchen3"
      3) "age"
      4) "27"
```

```
127.0.0.1:6379> xrange stream 1585692396423-0 +
1) 1) "1585692396423-0"
   2) 1) "name"
      2) "ericchen"
      3) "age"
      4) "25"
2) 1) "1585692407487-0"
   2) 1) "name"
      2) "ericchen2"
      3) "age"
      4) "26"
3) 1) "1585692412571-0"
   2) 1) "name"
      2) "ericchen3"
      3) "age"
      4) "27"
```



## 独立消费

Redis 单独定义了一个消费指令 xread  可以将 Stream 当成普通的消息队列 list 来使用

使用 xread 时,我们可以完全忽略消费组的存在,就好像 Stream 就是一个普通的列表一样

```
#从 Stream 头部读取两条消息
127.0.0.1:6379> xread  count 2 streams key 0-0
1) 1) "key"
   2) 1) 1) "1585692396423-0"
         2) 1) "name"
            2) "ericchen"
            3) "age"
            4) "25"
      2) 1) "1585692407487-0"
         2) 1) "name"
            2) "ericchen2"
            3) "age"
            4) "26"
```

```
#从尾部阻塞等待新消息到来,西面的指令会堵住,知道新消息到来
127.0.0.1:6379> xread block 0 count 1 streams stream $
1) 1) "stream"
   2) 1) 1) "1585693589429-0"
         2) 1) "name"
            2) "erichen"
            3) "age"
            4) "20"
(85.88s)  // 等待时长
```

客户端2

```
127.0.0.1:6379> xadd stream * name erichen age 20
"1585693589429-0"
```

客户端如果想要使用 xread 进行顺序消费,那么一定要记住当前消费到哪里了,也就是返回消息 ID, 下次继续调用 xread 的时候,将上次返回的最后一个消息 ID 作为参数传过去没救可以继续消费后续的消息

block 0 标识永远阻塞,知道消息到来 ; block 1000 标识阻塞 1s ,如果 1s 内没有任何消息到来,就返回 nil

## 创建消费组

创建消费组需要提供起始消息 ID 参数用来初始化 last_delivered_id 变量



![`](assets/image-20200419205004360.png)

```
27.0.0.1:6379> xadd streamkey * name ericchen age 25
"1585694442041-0"
127.0.0.1:6379> xgroup create streamkey cg1 0-1
OK
127.0.0.1:6379> xgroup create streamkey cg2 $
OK
127.0.0.1:6379> xinfo stream streamkey
 1) "length"
 2) (integer) 1
 3) "radix-tree-keys"
 4) (integer) 1
 5) "radix-tree-nodes"
 6) (integer) 2
 7) "groups"
 8) (integer) 2
 9) "last-generated-id"
10) "1585694442041-0"
11) "first-entry"
12) 1) "1585694442041-0"
    2) 1) "name"
       2) "ericchen"
       3) "age"
       4) "25"
13) "last-entry"
14) 1) "1585694442041-0"
    2) 1) "name"
       2) "ericchen"
       3) "age"
       4) "25"
```

### 消费

Stream 提供了 xreadgroup 指令可以进行消费组的组内消费,需要提供消费组的名称.消费者名称和其实消息 id

同 xread 一样,也有一个阻塞等待新消息的方法

读到消息之后,对应的消息 ID 就会进入消费者的 PEL 结构里,客户端处理完毕之后使用 xack 指令通知服务器,本条消息处理完毕,该消息 ID 就会从 PEL 中移除

## 

## Stream 消息太多怎么办

xdel 仅仅只是做了删除标志位

readis 提供了固定长度的 Stream 功能,在 xadd 的指令中提供一个定长长度的参数 maxlen ,就可以将老的消息干掉,确保链表不超过指定长度

```
xlen codehole
5
xadd codehole maxlen 3 * name xiaorui age 1
131232133232-0
xlen codehole
3
```

可以看到长度被砍掉了

#### 消息如果忘记 ack 回怎么样

Stream 的每个消费者结构中保存了正在处理的消息 ID列表 PEL ,如果消费者受到消息,处理完了但是没有回复 ack ,会导致 PEL 列表不断增长,如果有很多消费组的话可能会内存占用很多

#### PEL 如何避免消息丢失

当客户端读取 Stream 消息时,在 Redis服务器将消息给客户端的过程中 ,如果客户端突然断开连接,那么这个消息还没有被客户端受到就丢失了, 不过没关系,PEL里已经保存了发出去的消息 ID,等待客户端重新量上去之后,可以再次受到 PEL 中的消息 ID 列表,此时 xreadgroup 的其实消息 ID 必须是任意有效的消息 ID,一般将参数设为 0-0 .标识所有的 PEL 消息以及自 last_delivered_id 之后的新消息

