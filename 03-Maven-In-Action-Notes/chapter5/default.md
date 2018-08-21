## 默认（或生成）生命周期

这是 Maven 主要的生命周期，用于构建应用程序。它有以下 23 个阶段。

| 生命周期阶段 | 描述 |
| :--- | :--- |
| validate | 验证项目是否正确，并且所有必要的信息可用于完成构建过程 |
| initialize | 建立初始化状态，例如设置属性 |
| generate-sources | 产生任何的源代码包含在编译阶段 |
| process-sources | 处理源代码，例如，过滤器值 |
| generate-resources | 包含在包中产生的资源 |
| process-resources | 复制和处理资源到目标目录，准备打包阶段 |
| compile | 编译该项目的源代码 |
| process-classes | 从编译生成的文件提交处理，例如：Java类的字节码增强/优化 |
| generate-test-sources | 生成任何测试的源代码包含在编译阶段 |
| process-test-sources | 处理测试源代码，例如，过滤器任何值 |
| test-compile | 编译测试源代码到测试目标目录 |
| process-test-classes | 处理测试代码文件编译生成的文件 |
| test | 运行测试使用合适的单元测试框架（JUnit） |
| prepare-package | 执行必要的任何操作的实际打包之前准备一个包 |
| package | 提取编译后的代码，并在其分发格式打包，如JAR，WAR或EAR文件 |
| pre-integration-test | 完成执行集成测试之前所需操作。例如，设置所需的环境 |
| integration-test | 处理并在必要时部署软件包到集成测试可以运行的环境 |
| pre-integration-test | 完成集成测试已全部执行后所需操作。例如，清理环境 |
| verify | 运行任何检查，验证包是有效的，符合质量审核规定 |
| install | 将包安装到本地存储库，它可以用作当地其他项目的依赖 |
| deploy | 复制最终的包到远程仓库与其他开发者和项目共享 |

有涉及到Maven 生命周期值得一提几个重要概念：

* 当一个阶段是通过 Maven命令调用，例如：mvn compile，只有阶段到达并包括这个阶段才会被执行。

* 不同的 Maven 目标绑定到 Maven生命周期的不同阶段这是这取决于包类型\(JAR/WAR/EAR\)。 

在下面的示例中，将附加 Maven 的 antrun 插件：运行目标构建生命周期的几个阶段。这将使我们能够回显的信息显示生命周期的各个阶段。  
我们已经更新了在 C:\MVN\ 项目文件夹中的 pom.xml 文件。

现在，打开命令控制台，进入包含 pom.xml 并执行以下 mvn 命令。

```
C:\MVN\project>mvn compile
```

\[INFO\] Scanning for projects...

```
[INFO] ------------------------------------------------------------------
[INFO] Building Unnamed - com.companyname.projectgroup:project:jar:1.0
[INFO]    task-segment: [compile]
[INFO] ------------------------------------------------------------------
[INFO] [antrun:run {execution: id.validate}]
[INFO] Executing tasks
     [echo] validate phase
[INFO] Executed tasks
[INFO] [resources:resources {execution: default-resources}]
[WARNING] Using platform encoding (Cp1252 actually) to copy filtered resources,
i.e. build is platform dependent!
[INFO] skip non existing resourceDirectory C:\MVN\project\src\main\resources
[INFO] [compiler:compile {execution: default-compile}]
[INFO] Nothing to compile - all classes are up to date
[INFO] [antrun:run {execution: id.compile}]
[INFO] Executing tasks
     [echo] compile phase
[INFO] Executed tasks
[INFO] ------------------------------------------------------------------
[INFO] BUILD SUCCESSFUL
[INFO] ------------------------------------------------------------------
[INFO] Total time: 2 seconds
[INFO] Finished at: Sat Jul 07 20:18:25 IST 2012
[INFO] Final Memory: 7M/64M
[INFO] ------------------------------------------------------------------

```



