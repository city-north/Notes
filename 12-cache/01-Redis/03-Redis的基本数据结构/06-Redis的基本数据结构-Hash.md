# Hash

Redis hash 是一个 string 类型的 field 和 value 的映射表，hash 特别适合用于存储对象。

Redis 中每个 hash 可以存储 232 - 1 键值对（40多亿）。

```
127.0.0.1:6379>  HMSET runoobkey name "redis tutorial" description "redis basic commands for caching" likes 20 visitors 23000
OK
127.0.0.1:6379>  HGETALL runoobkey
1) "name"
2) "redis tutorial"
3) "description"
4) "redis basic commands for caching"
5) "likes"
6) "20"
7) "visitors"
8) "23000"
```



| 序号 | 命令         | 描述                                                     |                                         |
| ---- | ------------ | -------------------------------------------------------- | --------------------------------------- |
| 1    | hdel         | 删除一个或多个哈希表字段                                 | [hdel.md](hash/hdel.md)                 |
| 2    | hexists      | 查看哈希表 key 中，指定的字段是否存在。                  | [hexists.md](hash/hexists.md)           |
| 3    | hget         | 获取存储在哈希表中指定字段的值。                         | [hget.md](hash/hget.md)                 |
| 4    | hgetall      | 获取在哈希表中指定 key 的所有字段和值                    | [hgetall.md](hash/hgetall.md)           |
| 5    | hincrby      | 为哈希表 key 中的指定字段的整数值加上增量 increment 。   | [hincrby.md](hash/hincrby.md)           |
| 6    | hincrbyfloat | 为哈希表 key 中的指定字段的浮点数值加上增量 increment 。 | [hincrbyfloat.md](hash/hincrbyfloat.md) |
| 7    | hkeys        | 获取所有哈希表中的字段                                   | [hkeys.md](hash/hkeys.md)               |
| 8    | hlen         | 获取哈希表中字段的数量                                   | [hlen.md](hash/hlen.md)                 |
| 9    | hmget        | 获取所有给定字段的值                                     | [hmget.md](hash/hmget.md)               |
| 10   | hmset        | 同时将多个 field-value (域-值)对设置到哈希表 key 中。    | [hmset.md](hash/hmset.md)               |
| 11   | hset         | 将哈希表 key 中的字段 field 的值设为 value 。            | [hset.md](hash/hset.md)                 |
| 12   | hsetnx       | 只有在字段 field 不存在时，设置哈希表字段的值。          | [hsetnx.md](hash/hsetnx.md)             |
| 13   | hvals        | 获取哈希表中所有值                                       | [hvals.md](hash/hvals.md)               |
| 14   | hscan        | 迭代哈希表中的键值对。                                   | [hscan.md](hash/hscan.md)               |

