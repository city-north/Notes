[返回目录](/README.md)

# Linux文件基本属性

Linux系统是一种典型的多用户系统。

不同的用户处于不同的地位，拥有不同的权限。

```
[root@localhost ~]# ll
总用量 8
-rw-------. 1 root    root  1360 6月  10 07:05 anaconda-ks.cfg
drwxr-xr-x. 2 root    root     6 6月  10 08:45 conf
drwxr-xr-x. 6 polkitd input 4096 6月  11 00:11 data
drwxr-xr-x. 2 root    root     6 6月  10 08:45 logs
drwxr-xr-x. 5 root    root    42 6月  10 08:22 mysql

```

第一个字符代表：

* \[d\]代表目录
* \[-\]代表文件
* \[l\]代表连接文档
* \[b\]表示为装置文件里面的可供存储的接口设备（可随机存取装备）
* \[c\]表示为装置文件里面的串行端口设备，例如键盘，鼠标。

![](/assets/import01.png)

## 更改文件属性

### chgrp：更改文件属组

语法：

```
chgrp [-R] 属性名 文件名
```

参数选项

-R:递归更改文件属组，就是在更改某个目录文件的属组时，如果加上-R参数，那么该目录下的所有文件的属组都会更改。

### chown：更改文件属主，也可以同时更改文件属组

语法：

```
chown [-R] 属组名 文件名
chown [-R] 属主名：属组名 文件名
```

进入 /root 目录（~）将install.log的拥有者改为bin这个账号：

```
[
root@www 
~]
 cd 
~
[
root@www 
~]#
 chown bin install
.
log

[
root@www 
~]#
 ls 
-
l

-
rw
-
r
--
r
--
1
 bin  users 
68495
Jun
25
08
:
53
 install
.
log
```

将install.log的拥有者与群组改回为root：

```
[
root@www 
~]#
 chown root
:
root install
.
log

[
root@www 
~]#
 ls 
-
l

-
rw
-
r
--
r
--
1
 root root 
68495
Jun
25
08
:
53
 install
.
log
```

#### 3、chmod：更改文件9个属性

Linux文件属性有两种设置方法，一种是数字，一种是符号。

Linux文件的基本权限就有九个，分别是owner/group/others三种身份各有自己的read/write/execute权限。

先复习一下刚刚上面提到的数据：文件的权限字符为：『-rwxrwxrwx』， 这九个权限是三个三个一组的！其中，我们可以使用数字来代表各个权限，各权限的分数对照表如下：

* r:4
* w:2
* x:1

每种身份\(owner/group/others\)各自的三个权限\(r/w/x\)分数是需要累加的，例如当权限为： \[-rwxrwx---\] 分数则是：

* owner = rwx = 4+2+1 = 7
* group = rwx = 4+2+1 = 7
* others= --- = 0+0+0 = 0

所以等一下我们设定权限的变更时，该文件的权限数字就是770啦！变更权限的指令chmod的语法是这样的：

```
 chmod [-R] xyz 文件或目录
```

选项与参数：

* xyz : 就是刚刚提到的数字类型的权限属性，为 rwx 属性数值的相加。
* -R : 进行递归\(recursive\)的持续变更，亦即连同次目录下的所有文件都会变更

举例来说，如果要将.bashrc这个文件所有的权限都设定启用，那么命令如下：

```
[root@www ~]# ls -al .bashrc
-rw-r--r--  1 root root 395 Jul  4 11:45 .bashrc
[root@www ~]# chmod 777 .bashrc
[root@www ~]# ls -al .bashrc
-rwxrwxrwx  1 root root 395 Jul  4 11:45 .bashrc
```

那如果要将权限变成_-rwxr-xr--_呢？那么权限的分数就成为 \[4+2+1\]\[4+0+1\]\[4+0+0\]=754。

#### 符号类型改变文件权限

还有一个改变权限的方法呦！从之前的介绍中我们可以发现，基本上就九个权限分别是\(1\)user \(2\)group \(3\)others三种身份啦！ 那么我们就可以藉由u, g, o来代表三种身份的权限！

此外， a 则代表 all 亦即全部的身份！那么读写的权限就可以写成r, w, x！也就是可以使用底下的方式来看：



| chmod | u g o a | +\(加入\) -\(除去\) =\(设定\) | r w x | 文件或目录 |
| :--- | :--- | :--- | :--- | :--- |


如果我们需要将文件权限设置为**-rwxr-xr--**，可以使用chmod u=rwx,g=rx,o=r 文件名来设定:

```
#  touch test1    // 创建 test1 文件
# ls -al test1    // 查看 test1 默认权限
-rw-r--r-- 1 root root 0 Nov 15 10:32 test1
# chmod u=rwx,g=rx,o=r  test1    // 修改 test1 权限
# ls -al test1
-rwxr-xr-- 1 root root 0 Nov 15 10:32 test1
```

而如果是要将权限去掉而不改变其他已存在的权限呢？例如要拿掉全部人的可执行权限，则：

```
#  chmod  a-x test1
# ls -al test1
-rw-r--r-- 1 root root 0 Nov 15 10:32 test1
```

[返回目录](#)

