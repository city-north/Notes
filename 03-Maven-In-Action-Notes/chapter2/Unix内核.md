[返回目录](../README.md)

基于UNIX的系统（包括Linux、MacOS以及FreeBSD）上安装Maven

## 检查Java环境

```
echo $JAVA_HOME
java -version
```

## 下载、解压

```
tar -xvzf apache-maven-3.0-bin.tar.gz
```

现在已经创建好了一个Maven安装目录apache-maven-3.0。虽然直接使用该环境变量之后就能使用Maven了，这里的推荐做法是，在安装目录旁平行地创建一个符号链接，方便以后的升级。

后续用到时更新

