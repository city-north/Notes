# Docker查看容器输出日志

要获取容器的输出信息,可以通过` docker [container] logs` 命令

包括:

- `-details` : 打印详细信息
- `-f`,`-follow` 持续保持输出
- `-since` : 输出从某个时间开始的日志
- `-tail` : 输出最近的若干日志
- `-t` ,`-timestamps` : 显示时间戳信息
- `-until`, :输出某个是新建之前的日志

## 实例

```
docker logs nginx
```