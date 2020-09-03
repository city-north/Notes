# Docker 删除和清理镜像

## 删除语法
```
docker rmi IMAGE[IMAGE...] 其中 IMAGE可以为标签或者是 ID
```
- `-f `或者`-force`:强制删除镜像,即使有容器依赖他
- `-no-prune`:不要清理未带标签的父镜像

### 实例
```
docker rmi nginx:lastest
```

## 清理语法
```
docker images prune
```
- `-a`,`-all`删除所有无用镜像,不光是临时镜像
- `filter` filter: 只清理符合给定过滤器的镜像
- `-f` `-force`:强制删镜像,而不进行提示操作

### 例子
清理临时的遗留镜像文件层,最后会提示释放的存储空间
```
docker image prune -f 
Total reclainmed space 1.4GB
```