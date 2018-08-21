[返回目录](/README.md)

# 导入和导出容器

## 导出容器

导出容器是指导出一个已经创建的容器到一个文件，不管此时这个容器是否处于运行状态，可以使用docker export命令

语法：

```
sudo export CONTAINERID
```

例子：

将ce5542...容器导出到test\_for\_run.tar文件

```
sudo docker export ce5 >test_for_run.tar
```

## 导入容器

导出的文件又可以使用命令导入，称为镜像

```
cat test_for_run.tar | sudo docker import -test/ubantu:v1.0
```

## docker load 和 docker import的区别

* import快照文件将**丢弃**所有历史记录和元数据信息（快照状态）
* load镜像存储文件将保存完成记录
* 从镜像快照文件导入时可以重新指定标签等元数据信息



