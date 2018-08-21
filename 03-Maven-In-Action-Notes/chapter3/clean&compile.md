[返回目录](../README.md)

## 编译命令

当使用Mavn进行编译时，在项目根目录下运行命令：

    mvn clean compile 


clean 告诉Maven清理输出目录 target/

compile告诉Maven编译项目主代码

## 执行过程

查看输出：

![](/pics/3.2.1.PNG)

1. maven首先执行了clean:clean任务，删除target/目录。默认情况下，Maven构建的所有输出都在target/目录中；
2. 执行resources:resources任务，将系统资源打包至target/resources目录下。
3. 最后执行compile:compile任务，将主代码编译至target/classes目录。

**clean:clean对应着Maven插件以及clean插件的clean目标。**

**compiler:compiler是compiler插件的compiler目标。**

## 打包了那些代码

项目主代码和测试代码不同，项目的主代码会被打包到最终的构件中，如jar。而测试代码只在运行测试时用到，不会被打包。

默认情况下，Maven假设项目的主代码位于 **src/main/java**目录下。无需额外的配置，Maven会自动搜寻该目录找到项目主代码。

