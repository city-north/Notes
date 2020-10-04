# Docker存出或者导出镜像

## 存出语法
```
docker [images] save 
```
- `-o` 导出镜像到指定的文件中
- `-output`


### 例子
```
//提交更改
sudo docker commit cb6eb1d64a6d hand/documentserver:1.8

//存出
sudo docker save -o /images/hand-documentserver-1.8.tar hand/documentserver:1.8
```

