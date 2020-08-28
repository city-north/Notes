# 中继日志

主从复制中,从服务器上一个很重要的文件

从服务器 IO 线程将服务器的 binlog 读取过来并记录到salve 服务器的 relay log 中, 然后slave服务器上的 SQL 线程会读取relaylog 里面的内容并应用