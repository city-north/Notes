# Redis持久化机制

Redis 速度快，很大一部分原因是因为它所有的数据都存储在内存中。如果断电或者 宕机，都会导致内存中的数据丢失。为了实现重启后数据不丢失，Redis 提供了两种持久化的方案，

-  [一种是 RDB 快照 (Redis DataBase)](05-RDB持久化.md) 
-  [一种是 AOF (Append Only File)](06-AOF持久化.md) 

