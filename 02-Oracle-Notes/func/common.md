[返回目录](/README.md)

# 通用函数

适用于任何数值类型，同时也适用于空值

* 常用的通用函数
  * NVL\(expr1,expr2\) 空值处理
  * NVL2\(expr1,expr2,expr3\) 
  * NVL3\(expr1,expr2\)
  * COALESCE\(expr1,expr2,...\)

## 空值处理nvl

范例：查询所有的雇员的年薪

```
select ename,sal*12 +comm from emp
   	ENAME	SAL*12+COMM
1	SMITH	
2	ALLEN	19500
3	WARD	15500
4	JONES	
5	MARTIN	16400
6	BLAKE	
7	CLARK	
8	SCOTT	
9	KING	
10	TURNER	18000
11	ADAMS	

```

有的员工奖金为null，所以加起来年薪也为null

使用nvl处理：

```
select ename,nvl(comm,0),sal*12 +nvl(comm,0) from emp
   	ENAME	NVL(COMM,0)	SAL*12+NVL(COMM,0)
1	SMITH	0	9600
2	ALLEN	300	19500
3	WARD	500	15500
4	JONES	0	35700
5	MARTIN	1400	16400
6	BLAKE	0	34200
7	CLARK	0	29400
8	SCOTT	0	36000
9	KING	0	60000
10	TURNER	0	18000
11	ADAMS	0	13200

```



