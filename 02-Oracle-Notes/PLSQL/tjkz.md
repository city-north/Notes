[返回目录](/README.md)

# 条件控制语句

![](/assets/import37.png)

```
rem PL/SQL Developer Test Script

set feedback off
set autoprint off

rem Execute PL/SQL Block
-- 打印1~10
declare 
  -- 定义变量
  pnum number := 1;
begin
  loop
    --退休条件
    exit when pnum > 10;

    --打印
    dbms_output.put_line(pnum);
    --加一
    pnum := pnum + 1;
  end loop;
end;
/
```

if:

```
rem PL/SQL Developer Test Script

set feedback off
set autoprint off

rem Execute PL/SQL Block
-- 判断用户从键盘输入的数字

--接受键盘输入
--变量num：是一个地址值，在该地址上保存了输入的值
accept num prompt '请输入一个数字';

declare 
  --定义变量保存输入 的数字
  pnum number := &num;
begin
  if pnum = 0 then dbms_output.put_line('您输入的是0');
     elsif pnum = 1 then dbms_output.put_line('您输入的是1');
     elsif pnum = 2 then dbms_output.put_line('您输入的是2');
     else dbms_output.put_line('其他数字');
  end if;
end;
/
```



