[返回目录](/README.md)

## 条件查询和排序

* 使用where语句过滤
* 比较运算符

![](/assets/import11.png)

* 其他比较运算符

![](/assets/import12.png)

* 逻辑运算符

![](/assets/import13.png)

## 排序中的空值问题

当排序有可能存在null时，就会产生问题，我们可以用nulls first，nulls last 来指定null值显示的位置

```
  select  from emp order by comm desc nulls last
  SQL> /

     EMPNO ENAME    JOB              MGR HIREDATE         SAL       COMM     DEPTNO                                                                                                                     
---------- -------- --------- ---------- -------------- ----- ---------- ----------                                                                                                                     
      7654 MARTIN   SALESMAN        7698 28-9月 -81      1250       1400         30                                                                                                                     
      7521 WARD     SALESMAN        7698 22-2月 -81      1250        500         30                                                                                                                     
      7499 ALLEN    SALESMAN        7698 20-2月 -81      1600        300         30                                                                                                                     
      7844 TURNER   SALESMAN        7698 08-9月 -81      1500          0         30                                                                                                                     
      7788 SCOTT    ANALYST         7566 19-4月 -87      3000                    20                                                                                                                     
      7839 KING     PRESIDENT            17-11月-81      5000                    10                                                                                                                     
      7876 ADAMS    CLERK           7788 23-5月 -87      1100                    20                                                                                                                     
      7900 JAMES    CLERK           7698 03-12月-81       950                    30                                                                                                                     
      7902 FORD     ANALYST         7566 03-12月-81      3000                    20                                                                                                                     
      7934 MILLER   CLERK           7782 23-1月 -82      1300                    10                                                                                                                     
      7698 BLAKE    MANAGER         7839 01-5月 -81      2850                    30                                                                                                                     
      7566 JONES    MANAGER         7839 02-4月 -81      2975                    20                                                                                                                     
      7369 SMITH    CLERK           7902 17-12月-80       800                    20                                                                                                                     
      7782 CLARK    MANAGER         7839 09-6月 -81      2450                    10                                                                                                                     

已选择 14 行。
```



