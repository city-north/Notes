# [QA]MySQL主从复制的具体原理是什么

- 主服务器把数据更新记录到二进制日志里

- 从服务器通过 IO thread 向主库发起 binlog 请求
- 主服务器通过 IO dump thread 把二进制日志传递给从库
- 从库通过 IO thread 记录到自己的relaylog 中继日志中
- 从库通过 SQL thread 应用中继中 SQL 的内容



