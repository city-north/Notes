# HashSet

HashSet底层维护了一个HashMap.基于HashMap实现,这个HashMap的value是一个空的Object，不在赘述.

## 值得注意的是

- HashSet初始容量是16，如果初始化时指定容量，太大会浪费空间和时间，太小由于扩容操作会进行复制等一些列操作，效率很低。
- 负载因子`0.75f`,建议使用默认

