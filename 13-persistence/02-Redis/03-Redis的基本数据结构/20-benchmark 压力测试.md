# benchMark 压力测试

#### 测试一个普通的 set 操作

```
$ redis-benchmark -t set -q     
SET: 135318.00 requests per second
```

大概 13w QPS

#### 管道内并行请求数量

```
$ redis-benchmark -t set -P 2 -q
SET: 258397.94 requests per second
```

QPS 达到了 25 万

P 参数代表单个管道内并行请求数量

```
$ redis-benchmark -t set -P 3 -q
SET: 377358.50 requests per second
```

如果再提高 P 参数, QPS 并不会一直增长,因为 CPU 处理能力达到了瓶颈,Redis 单线程 CPU 消耗已经达到了 100%

