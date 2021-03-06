# 001-Redis基础数据结构总结

[TOC]

## Redis基础数据结构表格

| 对象         | 对象 type 属性值 | type 命令输出 | 底层可能的存储结构                                           | object encoding                 |
| ------------ | ---------------- | ------------- | ------------------------------------------------------------ | ------------------------------- |
| 字符串对象   | OBJ_STRING       | "string"      | OBJ_ENCODING_INT <br />OBJ_ENCODING_EMBSTR <br />OBJ_ENCODING_RAW | int <br />embstr <br />raw      |
| 列表对象     | OBJ_LIST         | "list"        | OBJ_ENCODING_QUICKLIST                                       | quicklist                       |
| 哈希对象     | OBJ_HASH         | "hash"        | OBJ_ENCODING_ZIPLIST<br /> OBJ_ENCODING_HT                   | ziplist <br />hashtable         |
| 集合对象     | OBJ_SET          | "set"         | OBJ_ENCODING_INTSET <br />OBJ_ENCODING_HT                    | intset <br />hashtable          |
| 有序集合对象 | OBJ_ZSET         | "zset"        | OBJ_ENCODING_ZIPLIST <br />OBJ_ENCODING_SKIPLIST             | ziplist<br /> skiplist(包含 ht) |

## Redis数据结构编码转换

| 对象   | 条件           | 原始编码 | 升级条件             | 升级编码 |
| ------ | -------------- | -------- | -------------------- | -------- |
| 字符串 | 如果是数字类型 | int      | -                    | -        |
|        | 如果是字符类型 | raw      | 超过44字节或者被修改 | embstr   |

| 对象           | 条件 | 原始编码  | 升级条件 | 升级编码 |
| -------------- | ---- | --------- | -------- | -------- |
| 列表对象(List) | -    | quickList | -        | -        |

| 对象           | 条件                                                 | 原始编码  | 升级条件 | 升级编码  |
| -------------- | ---------------------------------------------------- | --------- | -------- | --------- |
| 哈希对象(Hash) | 键和值的长度小于64byte<br />且Key-value不能超过512个 | quickList | 超过     | hashtable |

| 对象          | 条件                          | 原始编码 | 升级条件 | 升级编码  |
| ------------- | ----------------------------- | -------- | -------- | --------- |
| 集合对象(Set) | 元素都是整数类型, 且小于512个 | intset   | 超过     | hashtable |

| 对象               | 条件                                                | 原始编码 | 升级条件 | 升级编码 |
| ------------------ | --------------------------------------------------- | -------- | -------- | -------- |
| 有序集合对象(ZSet) | 元素数量不超过128个且任何一个member的长度小于64字节 | zipList  | 超过     | skipList |

