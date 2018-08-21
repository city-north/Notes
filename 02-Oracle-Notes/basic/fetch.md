[返回目录](/README.md)

# 分页

* 12c以下使用嵌套语句
* 12c或以上可以使用Fetch子句

## 12C以下

* 第一种，在查询的第二层通过ROWNUM&lt;= 来控制最大值，在查询的最外层控制最小值

```
SELECT * FROM  
(  
SELECT A.*, ROWNUM RN  
FROM (SELECT * FROM TABLE_NAME) A  
WHERE ROWNUM <= 40  
)  
WHERE RN >= 21
```

其中最内层的查询SELECT \* FROM TABLE\_NAME表示不进行翻页的原始数据查询。

```
ROWNUM <= 40    结束记录，第40条记录
WHERE RN >= 21  开始记录，第21条记录
```

* 第二种方式：去掉查询第二层的WHERE ROWNUM&lt;=40语句，在查询的最外层控制分层的最小值和最大值，查询语句如下：

```
SELECT * FROM  
(  
SELECT A.*, ROWNUM RN  
FROM (SELECT * FROM TABLE_NAME) A  
)  
WHERE RN BETWEEN 21 AND 40
```

* 区别：
  * 第一种效率高

```
//page是页数，rows是显示行数
int page=2;
int rows=5;                            
 List<Articles> list=a.select(page*rows+1，(page-1)*rows);
 //  sql语句：  
 select * from
 (
   select a.*,rownum rn 
   from (select * from t_articles) a 
   where rownum < 11
 ) 
 where rn>5

 //第一个参数，对应着第一个rownum<11,第二个参数对应着rn>5
```

## Oracle Fetch 子句

**产品表（products）和库存表（inventories）**![](/assets/import18.png)获得最高的前5个产品：

## MySQL

```
SELECT
   product_name,
   quantity
FROM
    inventories
INNER JOIN products
    USING(product_id)
ORDER BY
    quantity DESC 
LIMIT 5;
```

## Oracle

12c及以上：

```
SELECT
    product_name,
    quantity
FROM
    inventories
INNER JOIN products
        USING(product_id)
ORDER BY
    quantity DESC 
FETCH NEXT 5 ROWS ONLY;
```

* 行限制语句:

```
FETCH NEXT 5 ROWS ONLY
```

_Oracle 11g_及以下：

```
SELECT
    product_name, quantity
FROM
    inventories
INNER JOIN products
        USING(product_id)
WHERE  rownum<=5
ORDER BY quantity DESC;
```

## Oracle Fetch子句

语法：

```
[ OFFSET offset ROWS]
 FETCH  NEXT [  row_count | percent PERCENT  ] ROWS  [ ONLY | WITH TIES ]
```

### offset 子句

offset子句指定在行限制开始之前要跳过的行数。offset子句是可选的，如果跳过它，则偏移量为0，行限制从第一行开始计算。

偏移量必须是一个数字或者一个表达式，其值为一个数字。偏移量遵守以下规则：

* 如果偏移量是负值，则视为0
* 如果偏移量为NULL,或者大于查询返回的行数，则不返回任何行。
* 如果偏移量包含一个分数，则分数部分被截获。

### Fetch子句

fetch子句指定要返回的行数或者百分比

为了语义清晰地目的，您可以使用关键字row而不是rows,first 而不是next。

例如：一下子句的行为和产生的结果相同：

```
FETCH NEXT 1 ROWS
FETCH FIRST 1 ROW
```

## ONLY\|WITH TIES选项

仅返回FETCH NEXT （或者FIRST）后的行数或者行数的百分比。

with ties返回与最后一行相同的排序键。请注意如果使用with ties ，则必须在查询中指定一个ORDER BY子句。如果不这样做，查询将不会返回额外的行。

## 例子

* #### 1. 获取前N行记录的示例 {#h4-1-n-}

以下语句返回库存量最高的前`10`个产品：

```
-- 以下查询语句仅能在Oracle 12c以上版本执行
SELECT
    product_name,
    quantity
FROM
    inventories
INNER JOIN products
        USING(product_id)
ORDER BY
    quantity DESC 
FETCH NEXT 5 ROWS ONLY;
```

![](/assets/import19.png)

* #### 2. WITH TIES示例 {#h4-2-with-ties-}

```
-- 以下查询语句仅能在Oracle 12c以上版本执行
SELECT
 product_name,
 quantity
FROM
 inventories
INNER JOIN products
 USING(product_id)
ORDER BY
 quantity DESC 
FETCH NEXT 10 ROWS WITH TIES;
```

![](/assets/import21.png)即使查询请求了10行数据，因为它具有with ties 选项，查询还是返回了另外两行。请注意，这两个附加行在quantity列的值与第10行quantity列的值相同。

#### 3. 以百分比限制返回行的示例 {#h4-3-}

以下查询返回库存量最高的前`1％`的产品：

```
-- 以下查询语句仅能在Oracle 12c以上版本执行
SELECT
    product_name,
    quantity
FROM
    inventories
INNER JOIN products
        USING(product_id)
ORDER BY
    quantity DESC 
FETCH FIRST 1 PERCENT ROWS ONLY;
```

![](/assets/import23.png)

库存\(`inventories`\)表总共有`1112`行，因此，`1112`中的`1％`是`11.1`，四舍五入为`12`\(行\)。

#### 4. OFFSET示例 {#h4-4-offset-}

以下查询将跳过库存量最高的前`10`个产品，并返回接下来的`10`个产品：

```
-- 以下查询语句仅能在Oracle 12c以上版本执行
SELECT
 product_name,
 quantity
FROM
 inventories
INNER JOIN products
 USING(product_id)
ORDER BY
 quantity DESC 
OFFSET 10 ROWS 
FETCH NEXT 10 ROWS ONLY;

注意：这个功能可以用于分页的实现
```

![](/assets/import24.png)

