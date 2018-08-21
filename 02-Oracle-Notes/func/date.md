[返回目录](/README.md)

# 日期函数

* Oracle中的日期：

Oracle中的日期数据实际含有两个值：日期和时间，默认的日期格式为：DD-MON-RR

* 日期的数学运算

  * 在日期上加上或者减去一个数字结果仍然为日期
  * 两个日期相减返回日期之间相差的天数，可以用数字除24

* 日期函数

![](/assets/import25.png)

* 日期的四舍五入

![](/assets/import26.png)

* 日期函数示例

1.查询雇员进入公司的周数

```
select ename,round((sysdate -hiredate)/7) from emp
-- 查询雇员进入公司的天数（sysdate - 入职日期）
-- 天数/7 =周数
        ENAME    ROUND((SYSDATE-HIREDATE)/7)
1    SMITH    1956
2    ALLEN    1947
3    WARD    1946
4    JONES    1941
5    MARTIN    1915
6    BLAKE    1937
7    CLARK    1931
8    SCOTT    1625
9    KING    1908
10    TURNER    1918
11    ADAMS    1621
```

2.获得两个时间段中的月数：MONTHS\_BETWEEN

```
select ename,round(months_between(sysdate ,hiredate)) months from emp
```

3.获得几个月后的日期：ADD\_MONTHS\(\)

查询所有雇员进入公司的月数

```
select ename,round(months_between(sysdate,hiredate))from emp

       ENAME    ROUND(MONTHS_BETWEEN(SYSDATE,H
1    SMITH    450
2    ALLEN    448
3    WARD    448
4    JONES    446
5    MARTIN    441
6    BLAKE    445
7    CLARK    444
8    SCOTT    374
9    KING    439
10    TURNER    441
11    ADAMS    373
```

3.获得几个月后的日期：ADD\_MONTHS\(\)

范例：求出三个月后的日期

```
select add_months(sysdate,3) from dual
       ADD_MONTHS(SYSDATE,3)
1    2018-9-13 10:26:30
```

## 



