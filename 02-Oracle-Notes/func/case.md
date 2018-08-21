[返回目录](/README.md)

# 条件表达式

* CASE表达式：SQL99的语法，类似于BASIC,比较繁琐
* DECODE函数：Oracle自己的语法，类似Java，比较简洁

## CASE表达式

## ![](/assets/import32.png)

## DECODE函数

![](/assets/import33.png)

## 条件表达式实例

根据10号部门的员工的工资，显示税率

```
select ename,sal,
       decode (TRUNC(sal/2000,0),
              0,0.00,
              1,0.09,
              2,0.20,
              3,0.30,
              4,0.40,
              5,0.42,
              6,0.44,
                0.55) TAX_RATE
from emp
where deptno = 10;

       ENAME    SAL    TAX_RATE
1    CLARK    2450.00    0.09
2    KING    5000.00    0.2
3    MILLER    1300.00    0
```



