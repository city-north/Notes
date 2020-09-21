# explain详解

https://mp.weixin.qq.com/s/8Gvw-JhNjZDZWCB3ic8gsw

### 查询执行计划

```sql
-- 查询 mysql 课程的老师手机号 
EXPLAIN
SELECT tc.phone
FROM teacher_contact tc
WHERE tcid = (
    SELECT tcid
    FROM teacher t
    WHERE t.tid = (
        SELECT c.tid
        FROM course c
        WHERE c.cname = 'mysql')
);
```

#### 执行结果

| id   | select\_type | table | type | possible\_keys | key  | ref  | rows | filtered | Extra       |
| :--- | :----------- | :---- | :--- | :------------- | :--- | :--- | :--- | :------- | :---------- |
| 1    | PRIMARY      | tc    | ALL  | NULL           | NULL | NULL | 3    | 33.33    | Using where |
| 2    | SUBQUERY     | t     | ALL  | NULL           | NULL | NULL | 3    | 33.33    | Using where |
| 3    | SUBQUERY     | c     | ALL  | NULL           | NULL | NULL | 4    | 25       | Using where |

## 每个参数的含义

| 序号 | 列                              |                                                              |
| ---- | ------------------------------- | ------------------------------------------------------------ |
| 1    | [id](#id)                       | 不同从大到小，相同从上到下                                   |
| 2    | [select_type](#select_type)     |                                                              |
| 3    | [table](#table)                 |                                                              |
| 4    | partitions                      |                                                              |
| 5    | [type](#type)                   |                                                              |
| 6    | [possible_keys](#possible_keys) | 可能用到的索引  , 如果是 NULL 就代表没有用到索引             |
| 7    | [key](#key)                     | 实际用到的索引  , 如果是 NULL 就代表没有用到索引             |
| 8    | [key_len](#key_len)             | 索引的长度(使用的字节数)。跟索引字段的类型、长度有关。       |
| 9    | [ref](#ref)                     | 使用哪个列或者常数和索引一起从表中筛选数据。                 |
| 10   | [rows](#rows)                   | MySQL 认为扫描多少行才能返回请求的数据，是一个预估值。一般来说行数越少越好。 |
| 11   | filtered                        | 这个字段表示存储引擎返回的数据在 server 层过滤后，剩下多少满足查询的记录数量的比例，它是一个百分比。 |
| 12   | [Extra](#Extra)                 | 执行计划给出的额外的信息说明。                               |

## id

两种情况

- id 值相同时，表的查询顺序是从上往下顺序执行

  > 例如这次查询的 id 都是 1，查询 的顺序是 teacher t(3 条)——course c(4 条)——teacher_contact tc(3 条)。

- 既有相同也有不同 , ID 不同的先大后小，ID 相同的从上往下

#### 实例

```sql
-- 查询课程ID为2，或者联系表ID为3的老师 EXPLAIN
EXPLAIN SELECT t.tname, c.cname, tc.phone
FROM teacher t,
     course c,
     teacher_contact tc
WHERE t.tid = c.tid
  AND t.tcid = tc.tcid
  AND (c.cid = 2
    OR tc.tcid = 3);
```

#### 查询结果

| id   | select\_type | table | type | rows | filtered | Extra                                                |
| :--- | :----------- | :---- | :--- | :--- | :------- | :--------------------------------------------------- |
| 1    | SIMPLE       | t     | ALL  | 3    | 100      | NULL                                                 |
| 1    | SIMPLE       | c     | ALL  | 4    | 25       | Using where; Using join buffer \(Block Nested Loop\) |
| 1    | SIMPLE       | tc    | ALL  | 3    | 33.33    | Using where; Using join buffer \(Block Nested Loop\) |

当 teacher 表插入 3 条数据后

```sql
INSERT INTO `teacher` VALUES (4, 'james', 4);
INSERT INTO `teacher` VALUES (5, 'tom', 5);
INSERT INTO `teacher` VALUES (6, 'seven', 6);
-- (备份)恢复语句
DELETE FROM teacher where tid in (4,5,6); COMMIT;
```

#### 查询结果

| id   | select\_type | table | type | rows | filtered | Extra                                                |
| :--- | :----------- | :---- | :--- | :--- | :------- | :--------------------------------------------------- |
| 1    | SIMPLE       | tc    | ALL  | 3    | 100      | NULL                                                 |
| 1    | SIMPLE       | t     | ALL  | 6    | 16.67    | Using where; Using join buffer \(Block Nested Loop\) |
| 1    | SIMPLE       | c     | ALL  | 4    | 25       | Using where; Using join buffer \(Block Nested Loop\) |

#### 为什么插入 3 条数据以后,查询结果变了呢?

这个是由笛卡尔积决定的。(小表驱动大表的思想)

> 举例:假如有 a、b、c 三张表，分别有 2、3、4 条数据，如果做三张表的联合查询， 当查询顺序是 a→b→c 的时候，它的笛卡尔积是:2*3*4=6*4=24。如果查询顺序是 c →b→a，它的笛卡尔积是 4*3*2=12*2=24。
> 因为 MySQL 要把查询的结果，包括中间结果和最终结果都保存到内存，所以 MySQL 会优先选择中间结果数据量比较小的顺序进行查询。所以最终联表查询的顺序是 a→b→ c。这个就是为什么 teacher 表插入数据以后查询顺序会发生变化。

## select_type

| select_type  | 解释                                                         |
| ------------ | ------------------------------------------------------------ |
| SIMPLE       | 简单查询，不包含子查询，不包含关联查询 union                 |
| PRIMARY      | 子查询 SQL 语句中的主查询，也就是最外面的那层查询            |
| SUBQUERY     | 子查询中所有的内层查询都是 SUBQUERY 类型的                   |
| DERIVED      | 衍生查询，表示在得到最终查询结果之前会用到临时表             |
| UNION        | 用到了 UNION 查询                                            |
| UNION RESULT | 主要是显示哪些表之间存在 UNION 查询。<union2,3>代表 id=2 和 id=3 的查询 存在 UNION |

## type

#### 常用的有

| table           | 解释                                                         |
| --------------- | ------------------------------------------------------------ |
| system          | system 是 const 的一种特例，只有一行满足条件。例如 : 只有一条数据的系统表 |
| const           | 主键索引或者唯一索引，只能查到一条数据的 SQL。               |
| eq_ref          | 通常出现在多表的 join 查询，表示对于前表的每一个结果，都只能匹配到后表的一行结果。<br />一般是唯一性索引的查询(UNIQUE 或 PRIMARY KEY)。<br />eq_ref 是除 const 之外最好的访问类型。 |
| ref             | 查询用到了非唯一性索引，或者关联操作只使用了索引的最左前缀。 |
| [range](#range) | 主键索引范围扫描。做优化尽量达到                             |
| [index](#index) | Full Index Scan，查询全部索引中的数据(比不走索引要快)        |
| [all](#all)     | Full Table Scan，如果没有索引或者没有用到索引，type 就是 ALL。代表全表扫描 |

#### 不常用的有

| table           | 解释 |
| --------------- | ---- |
| fulltext        |      |
| ref_or_null     |      |
| index_merger    |      |
| unique_subquery |      |
| index_subquery  |      |

#### 小结:

一般来说，需要保证查询至少达到 range 级别，最好能达到 ref。 ALL(全表扫描)和 index(查询全部索引)都是需要优化的。

#### range

```sql
explain select * from actor where id > 1;
```

| id   | select\_type | table | partitions | type  | possible\_keys | key     | key\_len | ref  | rows | filtered | Extra       |
| :--- | :----------- | :---- | :--------- | :---- | :------------- | :------ | :------- | :--- | :--- | :------- | :---------- |
| 1    | SIMPLE       | actor | NULL       | range | PRIMARY        | PRIMARY | 4        | NULL | 2    | 100      | Using where |

#### index

扫描全表索引，这通常比ALL快一些。（index是从索引中读取的，而all是从硬盘中读取）

前提是需要表里面的所有字段都是索引。

如果所有字段都是索引，那么我们可以借助对于B+Tree的理解，读取数据直接从叶子节点读取key，不再去读取对应的value，提高效率。

```
explain select id from film;
```

| id   | select\_type | table | partitions | type  | possible\_keys | key       | key\_len | ref  | rows | filtered | Extra       |
| :--- | :----------- | :---- | :--------- | :---- | :------------- | :-------- | :------- | :--- | :--- | :------- | :---------- |
| 1    | SIMPLE       | film  | NULL       | index | NULL           | idx\_name | 33       | NULL | 3    | 100      | Using index |

#### all

即全表扫描，意味着mysql需要从头到尾去查找所需要的行。通常情况下这需要增加索引来进行优化了

```
explain select * from actor;
```

| id   | select\_type | table | partitions | type | possible\_keys | key  | key\_len | ref  | rows | filtered | Extra |
| :--- | :----------- | :---- | :--------- | :--- | :------------- | :--- | :------- | :--- | :--- | :------- | :---- |
| 1    | SIMPLE       | actor | NULL       | ALL  | NULL           | NULL | NULL     | NULL | 3    | 100      | NULL  |

## possible_keys

这一列的含义是显示查询可能使用哪些索引来查找。 

explain 时可能出现 **possible_keys** 有列，而 key 显示 NULL 的情况，这种情况是因为表中数据不多，**MySQL** 认为索引对此查询帮助不大，选择了全表查询。 如果该列是NULL，则没有相关的索引。

在这种情况下，可以通过检查 where 子句看是否可以创造一个适当的索引来提高查询性能，然后用 explain 查看效果。

## key

可能用到的索引和实际用到的索引。如果是 NULL 就代表没有用到索引。

possible_key 可以有一个或者多个，可能用到索引不代表一定用到索引。 

反过来，possible_key 为空，key 可能有值吗?

是有可能的(这里是覆盖索引的情况)。

如果通过分析发现没有用到索引，就要检查 SQL 或者创建索引。

如果想强制mysql使用或忽视possible_keys列中的索引，在查询中使用 force index、ignore index。

## key_len列

这一列显示了mysql在索引里使用的字节数，通过这个值可以算出具体使用了索引中的哪些列。 

举例来说，film_actor的联合索引 idx_film_actor_id 由 film_id 和 actor_id 两个int列组成，并且每个int是4字节。通过结果中的key_len=4可推断出查询使用了第一个列：film_id列来执行索引查找。

```sql
explain select * from film_actor where film_id = 2;
```

## rows

这一列是mysql估计要读取并检测的行数，注意这个不是结果集里的行数。

## ref列

这一列显示了在key列记录的索引中，表查找值所用到的列或常量，常见的有：const（常量），字段名（例：film.id）

## Extra

执行计划给出的额外的信息说明。

- using index

  > 用到了覆盖索引，不需要回表。
  > EXPLAIN SELECT tid FROM teacher ;

- using where

  > 使用了 where 过滤，表示存储引擎返回的记录并不是所有的都满足查询条件，需要 在 server 层进行过滤(跟是否使用索引没有关系)。
  > EXPLAIN select * from user_innodb where phone ='13866667777';

- using index condition(索引条件下推)

- using filesort 需要优化

  > 不能使用索引来排序，用到了额外的排序(跟磁盘或文件没有关系)。 (复合索引的前提)
  >
  > - 如果order by的列上有索引，那么就可以利用索引的有序性进行排序；
  >
  > - 如果没有索引，那么就是file_sort，可以理解为外部排序，就是把select的数据,按照order by的列 ，在内存中进行一次排序，然后返回结果。

- using temporary

  > 用到了临时表。例如(以下不是全部的情况):
  >
  > 1. distinct 非索引列
  >    EXPLAIN select DISTINCT(tid) from teacher t; 
  >
  > 2. group by 非索引列
  >
  >    EXPLAIN select tname from teacher group by tname;
  >
  > 3. 使用 join 的时候，group 任意列
  >
  >    EXPLAIN select t.tid from teacher t join course c on t.tid = c.tid group by t.tid; 需要优化，例如创建复合索引。