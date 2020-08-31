# MySQL忘记root密码解决方案

如果忘记了root密码或者用临时密码无法登录：

```
vim /etc/my.cnf
```

在配置文件中加一行skip-grant-tables

```
[mysqld]
skip-grant-tables
```

重启数据库服务

```
service mysqld restart
```

然后使用mysql命令登录，使用以下密码修改密码。

```
mysql> update user set authentication_string=password('123456')  where Host='localhost' and User='root';
```

修改以后，在配置文件中去掉skip-grant-tables，重启数据库服务。
再使用 mysql -uroot -p123456登录。修改密码安全限制和授权远程访问依然要做。

