# Jinfo 配置信息工具

Jinfo 试试查看和调整虚拟机各项参数,使用 jps 命令的-v 参数可以查看虚拟机启动时显示指定的参数列表

如果你想知道未被显示指定的参数的系统默认值,可以使用 

- `jinfo -flag`

- `jinfo -sysprops` :  把虚拟机进程的 `System.getProperties()`

## 代码实例

![image-20200612231341900](assets/image-20200612231341900.png)