[返回Oracle安装](./oracle11g.md)

# 交换空间（Swap Space）

## 交换空间与内存之间的关系

| Available RAM | Swap Space Required |
| :--- | :--- |
| Between 1 GB and 2 GB | 1.5 times the size of the RAM |
| Between 2 GB and 16 GB | Equal to the size of the RAM |
| More than 16 GB | 16 GB |

## 查看交换空间

```
grep SwapTotal /proc/meminfo
```

## 修改交换空间

* 检查分区情况

```
free -m
```

* 增加交换分为文件，count等于要增加的大小，例子为2G

```
dd if=/dev/zero of=/home/swap bs=1024 count=2048000
```

* 设置交换文件

```
mkswap /home/swap
```

* 立即启用交换分区文件

```
swapon /home/swap
```

* 如果要在引导时自动启用，则编辑 /etc/fstab 文件，添加行

```
/home/swap swap swap defaults 0 0
```





[返回Oracle安装](/software/oracle11g.md)

