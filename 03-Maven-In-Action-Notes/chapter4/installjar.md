## 手动安装jar包到本地仓库

命令：

```
mvn install:install-file -Dfile=c:\kaptcha-{version}.jar -DgroupId=com.google.code -DartifactId=kaptcha -Dversion={version} -Dpackaging=jar
```

* -Dfile：jar所在位置以及jar包名称
* -DgroupId：需要安装jar包的组织ID
* -DartifactId：定义了当前Maven项目在组中的唯一ID
* -Dversion： jar包版本

## 例子:

```
D:\>mvn install:install-file -Dfile=c:\kaptcha-2.3.jar -DgroupId=com.google.code 
-DartifactId=kaptcha -Dversion=2.3 -Dpackaging=jar
[INFO] Scanning for projects...
[INFO] Searching repository for plugin with prefix: 'install'.
[INFO] ------------------------------------------------------------------------
[INFO] Building Maven Default Project
[INFO]    task-segment: [install:install-file] (aggregator-style)
[INFO] ------------------------------------------------------------------------
[INFO] [install:install-file]
[INFO] Installing c:\kaptcha-2.3.jar to 
D:\maven_repo\com\google\code\kaptcha\2.3\kaptcha-2.3.jar
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESSFUL
[INFO] ------------------------------------------------------------------------
[INFO] Total time: < 1 second
[INFO] Finished at: Tue May 12 13:41:42 SGT 2014
[INFO] Final Memory: 3M/6M
[INFO] ------------------------------------------------------------------------
```

## pom.xml

```
<dependency>
      <groupId>com.google.code</groupId>
      <artifactId>kaptcha</artifactId>
      <version>2.3</version>
 </dependency>
```



