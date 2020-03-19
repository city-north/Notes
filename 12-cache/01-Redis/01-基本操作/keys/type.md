# Type

### 返回值

返回 key 的数据类型，数据类型有：

- none (key不存在)
- string (字符串)
- list (列表)
- set (集合)
- zset (有序集)
- hash (哈希表)



```
127.0.0.1:6379> set weather "sunny"
OK
127.0.0.1:6379> type weather
string
127.0.0.1:6379> lpush book_list "programming in scala"
(integer) 1
127.0.0.1:6379> type book_list
list
127.0.0.1:6379> sadd pat "dog"
(integer) 1
127.0.0.1:6379> type pat
```

