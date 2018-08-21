[返回目录](../README.md)

## 检查Java环境
~~~
echo %JAVA_HOME%
java -version
~~~
没有Java环境先安装Java环境

## 本地安装Maven

1. 系统变量中新建一个变量:
变量名：M2_HOME
变量值为Maven安装目录：例如:D：\bin\apache-maven-3.0
2. 系统变量path末尾添加%M2_HOME%\bin;

## 为什么配置path
当我们在cmd输入命令时,Windows首先会在当前目录中寻找可执行文件或脚本，如果没有找到，Windows会接着遍历环境遍历Path中定义的路径。

由于将%M2_HOME%\bin添加到了path中，而这里%M2_HOME%实际上引用了前面定义的另一个变量（Maven安装目录）。因此Windows在执行目录为D:\bin\apache-maven-3.0\bin，而mvn执行脚本的位置就在这里。

## 检测Maven是否安装完成
~~~
echo %M2_HOME%
mvn -v
~~~