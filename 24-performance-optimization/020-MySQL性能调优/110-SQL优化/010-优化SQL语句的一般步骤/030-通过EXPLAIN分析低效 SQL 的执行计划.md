# 030-通过EXPLAIN分析低效 SQL 的执行计划

[TOC]

## 简介

[030-重新认识explain.md](../../../../13-persistence/01-MySQL/10-优化/010-SQL与索引的优化/030-重新认识explain.md)  

[040-explain详解.md](../../../../13-persistence/01-MySQL/10-优化/010-SQL与索引的优化/040-explain详解.md) 

## Explain

通过以上步骤查询到效率低的 SQL 语句后，可以通过 EXPLAIN 或者 DESC 命令获取MySQL如何执行SELECT语句的信息，包括在SELECT语句执行过程中表如何连接和连接的顺序，

比如想统计某个email为租赁电影拷贝所支付的总金额，需要关联客户表customer和付款表payment，并且对付款金额amount字段做求和（sum）操作，相应SQL的执行计划如下：

```sql
EXPLAIN SELECT
	sum( amount ) 
FROM
	customer a,
	payment b 
WHERE
	1 = 1 
	AND a.customer_id = b.customer_id 
	AND a.email = 'JANE.BENNETT@sakilacustomer.org' 
```

| id   | select\_type | table | partitions | type | possible\_keys        | key                   | key\_len | ref                   | rows | filtered | Extra       |
| :--- | :----------- | :---- | :--------- | :--- | :-------------------- | :-------------------- | :------- | :-------------------- | :--- | :------- | :---------- |
| 1    | SIMPLE       | a     | NULL       | ALL  | PRIMARY               | NULL                  | NULL     | NULL                  | 599  | 10       | Using where |
| 1    | SIMPLE       | b     | NULL       | ref  | idx\_fk\_customer\_id | idx\_fk\_customer\_id | 2        | sakila.a.customer\_id | 26   | 100      | NULL        |

- select_type：表示SELECT的类型，常见的取值有
  - SIMPLE（简单表，即不使用表连接或者子查询）、
  - PRIMARY（主查询，即外层的查询）、
  - UNION（UNION中的第二个或者后面的查询语句）、
  - SUBQUERY（子查询中的第一个SELECT）等。
- table：输出结果集的表。
- type：表示MySQL在表中找到所需行的方式，或者叫访问类型，

```
ALL -> 性能最差, 全表扫描, MySQL 遍历全表来找到匹配的行
index -> 索引全扫描, MySQL 遍历全表来找到匹配的行
range -> 索引的范围扫描, 常见于<; <=; >; >=; between 等操作符
ref -> 使用非唯一索引扫描或者唯一索引的前缀扫描, 返回匹配某个单独值的记录
eq_ref -> 类似于 ref, 区别在于使用的索引是唯一索引, 对于每个索引的键值, 表中只有一条记录匹配, 简单来说, 就是多表链接中, 使用 primary key 或者 unique index 作为关联条件
const/system -> 单表中最多一个匹配行, 查询起来非常迅速, 所以这个匹配行中的其他列可以被优化器在当前查询中当做常量来处理, 例如, 根据主键 **primary key** 或者唯一索引 **unique index** 进行的查询
```

### Type=ALL

全表扫描, MySQL 通过遍历全表来找到匹配的行

```sql
explain select * from film where rating >9
```

| id   | select\_type | table | partitions | type | possible\_keys | key  | key\_len | ref  | rows | filtered | Extra       |
| :--- | :----------- | :---- | :--------- | :--- | :------------- | :--- | :------- | :--- | :--- | :------- | :---------- |
| 1    | SIMPLE       | film  | NULL       | ALL  | NULL           | NULL | NULL     | NULL | 1000 | 33.33    | Using where |

### Type=index

type=index，索引全扫描，MySQL遍历整个索引来查询匹配的行：

```sql
explain select title from film
```

| id   | select\_type | table | partitions | type  | possible\_keys | key        | key\_len | ref  | rows | filtered | Extra       |
| :--- | :----------- | :---- | :--------- | :---- | :------------- | :--------- | :------- | :--- | :--- | :------- | :---------- |
| 1    | SIMPLE       | film  | NULL       | index | NULL           | idx\_title | 514      | NULL | 1000 | 100      | Using index |

- key length = 514字节

这一列显示了mysql在索引里使用的字节数，通过这个值可以算出具体使用了索引中的哪些列。 

举例来说，film_actor的联合索引 idx_film_actor_id 由 film_id 和 actor_id 两个int列组成，并且每个int是4字节。通过结果中的key_len=4可推断出查询使用了第一个列：film_id列来执行索引查找。

### type=range

索引范围扫描，常见于<、<=、>、>=、between等操作符：

```sql
explain select * from payment where customer_id >= 300 and customer_id <= 350
```

| id   | select\_type | table   | partitions | type  | possible\_keys        | key                   | key\_len | ref  | rows | filtered | Extra                 |
| :--- | :----------- | :------ | :--------- | :---- | :-------------------- | :-------------------- | :------- | :--- | :--- | :------- | :-------------------- |
| 1    | SIMPLE       | payment | NULL       | range | idx\_fk\_customer\_id | idx\_fk\_customer\_id | 2        | NULL | 1350 | 100      | Using index condition |

filtered : 查找到所需记录占总扫描记录数的比例。100%

### type=ref

使用非唯一索引扫描或唯一索引的前缀扫描，返回匹配某个单独值的记录行，例如：

```sql
explain select * from payment where customer_id =350
```

| id   | select\_type | table   | partitions | type | possible\_keys        | key                   | key\_len | ref   | rows | filtered | Extra |
| :--- | :----------- | :------ | :--------- | :--- | :-------------------- | :-------------------- | :------- | :---- | :--- | :------- | :---- |
| 1    | SIMPLE       | payment | NULL       | ref  | idx\_fk\_customer\_id | idx\_fk\_customer\_id | 2        | const | 23   | 100      | NULL  |

- select\_type 代表是简单查询
- table 代表表
- type 代表使用了非唯一索引
- possible\_keys  idx\_fk\_customer\_id
- key\_len 索引长度是 2 字节(smallint unsigned )
- ref 使用索引的时候, 匹配的是常量350
- filtered 查找到所需记录占总扫描记录数的比例, 这个比例越高越好

索引idx_fk_customer_id是非唯一索引，查询条件为等值查询条件customer_id=35(ref = const)，所以扫描索引的类型为ref。ref还经常出现在join操作中：

```sql
explain
select b.*, a.*
from payment a,
     customer b
where a.customer_id = b.customer_id -- customer_id 是一个普通的索引
```

| id   | select\_type | table | partitions | type | possible\_keys        | key                   | key\_len | ref                   | rows | filtered | Extra |
| :--- | :----------- | :---- | :--------- | :--- | :-------------------- | :-------------------- | :------- | :-------------------- | :--- | :------- | :---- |
| 1    | SIMPLE       | b     | NULL       | ALL  | PRIMARY               | NULL                  | NULL     | NULL                  | 599  | 100      | NULL  |
| 1    | SIMPLE       | a     | NULL       | ref  | idx\_fk\_customer\_id | idx\_fk\_customer\_id | 2        | sakila.b.customer\_id | 26   | 100      | NULL  |

### type=eq_ref

类似ref，区别就在使用的索引是唯一索引，对于每个索引键值，表中只有一条记录匹配；简单来说，就是多表连接中使用 primary key或者 unique index作为关联条件。

```sql
explain
select *
from film a,
     film_text b
where a.film_id = b.film_id  -- film_id 是主键, 所以 type 类型是 eq_ref
```

| id   | select\_type | table | partitions | type    | possible\_keys | key     | key\_len | ref               | rows | filtered | Extra       |
| :--- | :----------- | :---- | :--------- | :------ | :------------- | :------ | :------- | :---------------- | :--- | :------- | :---------- |
| 1    | SIMPLE       | b     | NULL       | ALL     | PRIMARY        | NULL    | NULL     | NULL              | 1000 | 100      | NULL        |
| 1    | SIMPLE       | a     | NULL       | eq\_ref | PRIMARY        | PRIMARY | 2        | sakila.b.film\_id | 1    | 100      | Using where |

### type=const/system

单表中最多有一个匹配行，查询起来非常迅速，所以这个匹配行中的其他列的值可以被优化器在当前查询中当作常量来处理，例如，根据主键 `primary key`或者唯一索引 `unique index`进行的查询。

构造一个查询：

```
alter table customer drop index idx_email;
alter table customer add unique index uk_email (email); -- 将 email 的索引转换成唯一索引
```

```
EXPLAIN SELECT
	* 
FROM
	( SELECT * FROM customer WHERE email = 'AARON.SELBY@sakilacustomer.org' ) a
```

| id   | select\_type | table    | partitions | type  | possible\_keys | key       | key\_len | ref   | rows | filtered | Extra |
| :--- | :----------- | :------- | :--------- | :---- | :------------- | :-------- | :------- | :---- | :--- | :------- | :---- |
| 1    | SIMPLE       | customer | NULL       | const | uk\_email      | uk\_email | 203      | const | 1    | 100      | NULL  |

- 通过唯一索引uk_email访问的时候，类型type为const；

- 而从我们构造的仅有一条记录的a表中检索时，类型type就为system。

### type=NULL

MySQL不用访问表或者索引，直接就能够得到结果，例如：

```
explain
select 1
from dual
where 1
```

| id   | select\_type | table | partitions | type | possible\_keys | key  | key\_len | ref  | rows | filtered | Extra          |
| :--- | :----------- | :---- | :--------- | :--- | :------------- | :--- | :------- | :--- | :--- | :------- | :------------- |
| 1    | SIMPLE       | NULL  | NULL       | NULL | NULL           | NULL | NULL     | NULL | NULL | NULL     | No tables used |

类型type还有其他值，如

- ref_or_null（与ref类似，区别在于条件中包含对NULL的查询）、
- index_merge（索引合并优化）、
- unique_subquery（in 的后面是一个查询主键字段的子查询）、
- index_subquery（与 unique_subquery 类似，区别在于 in 的后面是查询非唯一索引字段的子查询）等。

## 其他列

- possible_keys：表示查询时可能使用的索引。
- key：表示实际使用的索引。
- key_len：使用到索引字段的长度。
- rows：扫描行的数量。
- Extra：执行情况的说明和描述，包含不适合在其他列中显示但是对执行计划非常重要的额外信息。

## explain extended

```sql
explain extended
select sum(amount)
from customer a,
     payment b
where 1 = 1
  and a.customer_id = b.customer_id
  and email = 'JANE.BENNETT@sakilacustomer.org'
```

```
[2022-03-05 19:49:37] [HY000][1681] 'EXTENDED' is deprecated and will be removed in a future release.
[2022-03-05 19:49:37] [HY000][1003] /* select#1 */ select sum(`sakila`.`b`.`amount`) AS `sum(amount)` from `sakila`.`customer` `a` join `sakila`.`payment` `b` where ((`sakila`.`b`.`customer_id` = '77') and ('JANE.BENNETT@sakilacustomer.org' = 'JANE.BENNETT@sakilacustomer.org'))
[2022-03-05 19:49:37] 2 rows retrieved starting from 1 in 25 ms (execution: 7 ms, fetching: 18 ms)
```

从输出中国可以看到, Deprecated 了

```sql
select sum(`sakila`.`b`.`amount`) AS `sum(amount)`
from `sakila`.`customer` `a`
         join `sakila`.`payment` `b`
where ((`sakila`.`b`.`customer_id` = ''77 '') and (''JANE.BENNETT@sakilacustomer.org'' = ''JANE.BENNETT@sakilacustomer.org''))
```

explain extended输出结果中多了 filtered字段，同时从warning的message字段能够看到优化器自动去除了1=1恒成立的条件，也就是说优化器在改写SQL时会自动去掉恒成立的条件。在遇到复杂的SQL时，我们可以利用 explain extended的结果来迅速地获取一个更清晰易读的SQL。

