# JPS 虚拟机进程状态工具

> jps  JVM process Status Tool

jps 命令格式

```
jps [options] [hostid]
```

jps 执行样例:

| 选项 | 作用                                                    |
| ---- | ------------------------------------------------------- |
| -q   | 只输出 LVMID ,省略主类的名称                            |
| -m   | 输出虚拟机进程启动时传递给主类 main() 函数的参数        |
| -l   | 输出主类的全名,如果进程执行的是 JAR 包, 则输出 JAR 路径 |
| -v   | 输出虚拟机进程启动时的 JVM 参数                         |

## 实例

```
$ jps -l
43979 sun.tools.jps.Jps
34300 org.jetbrains.idea.maven.server.RemoteMavenServer36
24381 org.jetbrains.jps.cmdline.Launcher
```

```
$ jps -v
42976 RemoteMavenServer36 -Djava.awt.headless=true -Dmaven.defaultProjectBuilder.disableGlobalModelCache=true -Didea.maven.embedder.version=3.6.1 -Xmx768m -Dmaven.ext.class.path=/Applications/IntelliJ IDEA.app/Contents/plugins/maven/lib/maven-event-listener.jar -Dfile.encoding=UTF-8
```



