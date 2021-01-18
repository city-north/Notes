# 001-Java中的变量

[TOC]

## 简介

- **System.getenv() 返回的是系统级别的环境变量, 例如可以直接获取环境变量名为 JAVA_HOME 的环境变量**

- **System.getProperties()返回的是给对应的JVM设置的属性值, 它可以同过运行 *java -D* 来进行改变**

  ​	设置方式为(这里设置file.encoding和os.name两个属性)：

  ```java
  java -Dfile.encoding=utf-8 -Dos.name=windows7
  ```

## System.getenv()

返回的是系统级别的环境变量, 例如可以直接获取环境变量名为`JAVA_HOME` 的环境变量; 

```java
System.getenv();
```

#### 输出示例

```java
PATH -> /Library/Frameworks/Python.framework/Versions/3.9/bin:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin
SHELL -> /bin/zsh
PAGER -> less
LSCOLORS -> Gxfxcxdxbxegedabagacad
OLDPWD -> /
USER -> ec
VERSIONER_PYTHON_PREFER_32_BIT -> no
ZSH -> /Users/ec/.oh-my-zsh
TMPDIR -> /var/folders/c1/d1d38kss0kz34cd738qv3vx40000gn/T/
SSH_AUTH_SOCK -> /private/tmp/com.apple.launchd.ScTJlXVNp1/Listeners
XPC_FLAGS -> 0x0
VERSIONER_PYTHON_VERSION -> 2.7
__CF_USER_TEXT_ENCODING -> 0x1F5:0x0:0x0
Apple_PubSub_Socket_Render -> /private/tmp/com.apple.launchd.ad6qNkZKTn/Render
LOGNAME -> ec
LESS -> -R
JAVA_MAIN_CLASS_3961 -> cn.eccto.study.springframework.env.JavaEnvironmentDemo
LC_CTYPE -> 
PWD -> /Users/ec/study/Notes/02-spring-framework-documentation/00-code/note-spring-framework
XPC_SERVICE_NAME -> com.jetbrains.intellij.74376
HOME -> /Users/ec
```

## System.getProperties()

返回的是给对应的 JVM 设置的属性值, 它可以同过运行 *java -D* 来进行改变。

```java
sun.cpu.isalist -> 
sun.io.unicode.encoding -> UnicodeBig
sun.cpu.endian -> little
java.vendor.url.bug -> http://bugreport.sun.com/bugreport/
file.separator -> /
java.vendor -> Oracle Corporation
sun.boot.class.path -> /Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/jre/lib/resources.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/jre/lib/rt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/jre/lib/sunrsasign.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/jre/lib/jsse.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/jre/lib/jce.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/jre/lib/charsets.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/jre/lib/jfr.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/jre/classes
java.ext.dirs -> /Users/ec/Library/Java/Extensions:/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/jre/lib/ext:/Library/Java/Extensions:/Network/Library/Java/Extensions:/System/Library/Java/Extensions:/usr/lib/java
java.version -> 1.8.0_201
java.vm.info -> mixed mode
awt.toolkit -> sun.lwawt.macosx.LWCToolkit
user.language -> en
java.specification.vendor -> Oracle Corporation
sun.java.command -> cn.eccto.study.springframework.env.JavaEnvironmentDemo
java.home -> /Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/jre
sun.arch.data.model -> 64
java.vm.specification.version -> 1.8
java.class.path -> /Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/jre/lib/charsets.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/jre/lib/deploy.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/jre/lib/ext/cldrdata.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/jre/lib/ext/dnsns.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/jre/lib/ext/jaccess.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/jre/lib/ext/jfxrt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/jre/lib/ext/localedata.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/jre/lib/ext/nashorn.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/jre/lib/ext/sunec.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/jre/lib/ext/sunjce_provider.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/jre/lib/ext/sunpkcs11.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/jre/lib/ext/zipfs.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/jre/lib/javaws.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/jre/lib/jce.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/jre/lib/jfr.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/jre/lib/jfxswt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/jre/lib/jsse.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/jre/lib/management-agent.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/jre/lib/plugin.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/jre/lib/resources.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/jre/lib/rt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/lib/ant-javafx.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/lib/dt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/lib/javafx-mx.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/lib/jconsole.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/lib/packager.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/lib/sa-jdi.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/lib/tools.jar:/Users/ec/study/Notes/02-spring-framework-documentation/00-code/note-spring-framework/target/classes:/Users/ec/.m2/repository/org/springframework/spring-core/5.1.2.RELEASE/spring-core-5.1.2.RELEASE.jar:/Users/ec/.m2/repository/org/springframework/spring-jcl/5.1.2.RELEASE/spring-jcl-5.1.2.RELEASE.jar:/Users/ec/.m2/repository/org/springframework/spring-context/5.1.2.RELEASE/spring-context-5.1.2.RELEASE.jar:/Users/ec/.m2/repository/org/springframework/spring-aop/5.1.2.RELEASE/spring-aop-5.1.2.RELEASE.jar:/Users/ec/.m2/repository/org/springframework/spring-beans/5.1.2.RELEASE/spring-beans-5.1.2.RELEASE.jar:/Users/ec/.m2/repository/org/springframework/spring-expression/5.1.2.RELEASE/spring-expression-5.1.2.RELEASE.jar:/Users/ec/.m2/repository/joda-time/joda-time/2.10/joda-time-2.10.jar:/Users/ec/.m2/repository/javax/validation/validation-api/2.0.1.Final/validation-api-2.0.1.Final.jar:/Users/ec/.m2/repository/org/hibernate/validator/hibernate-validator/6.1.0.Final/hibernate-validator-6.1.0.Final.jar:/Users/ec/.m2/repository/jakarta/validation/jakarta.validation-api/2.0.2/jakarta.validation-api-2.0.2.jar:/Users/ec/.m2/repository/org/jboss/logging/jboss-logging/3.3.2.Final/jboss-logging-3.3.2.Final.jar:/Users/ec/.m2/repository/com/fasterxml/classmate/1.3.4/classmate-1.3.4.jar:/Users/ec/.m2/repository/javax/inject/javax.inject/1/javax.inject-1.jar:/Users/ec/.m2/repository/org/glassfish/javax.el/3.0.1-b09/javax.el-3.0.1-b09.jar:/Users/ec/.m2/repository/org/springframework/spring-test/5.1.2.RELEASE/spring-test-5.1.2.RELEASE.jar:/Users/ec/.m2/repository/org/springframework/spring-aspects/5.1.2.RELEASE/spring-aspects-5.1.2.RELEASE.jar:/Users/ec/.m2/repository/org/aspectj/aspectjweaver/1.9.2/aspectjweaver-1.9.2.jar:/Users/ec/.m2/repository/org/slf4j/slf4j-log4j12/1.8.0-alpha2/slf4j-log4j12-1.8.0-alpha2.jar:/Users/ec/.m2/repository/org/slf4j/slf4j-api/1.8.0-alpha2/slf4j-api-1.8.0-alpha2.jar:/Users/ec/.m2/repository/log4j/log4j/1.2.17/log4j-1.2.17.jar:/Users/ec/.m2/repository/org/projectlombok/lombok/1.18.12/lombok-1.18.12.jar:/Users/ec/.m2/repository/org/slf4j/jcl-over-slf4j/1.8.0-alpha2/jcl-over-slf4j-1.8.0-alpha2.jar:/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar
user.name -> ec
file.encoding -> UTF-8
java.specification.version -> 1.8
java.awt.printerjob -> sun.lwawt.macosx.CPrinterJob
user.timezone -> 
user.home -> /Users/ec
os.version -> 10.14.5
sun.management.compiler -> HotSpot 64-Bit Tiered Compilers
java.specification.name -> Java Platform API Specification
java.class.version -> 52.0
java.library.path -> /Users/ec/Library/Java/Extensions:/Library/Java/Extensions:/Network/Library/Java/Extensions:/System/Library/Java/Extensions:/usr/lib/java:.
sun.jnu.encoding -> UTF-8
os.name -> Mac OS X
java.vm.specification.vendor -> Oracle Corporation
java.io.tmpdir -> /var/folders/c1/d1d38kss0kz34cd738qv3vx40000gn/T/
line.separator -> 

java.endorsed.dirs -> /Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/jre/lib/endorsed
os.arch -> x86_64
java.awt.graphicsenv -> sun.awt.CGraphicsEnvironment
java.runtime.version -> 1.8.0_201-b09
java.vm.specification.name -> Java Virtual Machine Specification
user.dir -> /Users/ec/study/Notes/02-spring-framework-documentation/00-code/note-spring-framework
user.country -> CN
sun.java.launcher -> SUN_STANDARD
sun.os.patch.level -> unknown
java.vm.name -> Java HotSpot(TM) 64-Bit Server VM
file.encoding.pkg -> sun.io
path.separator -> :
java.vm.vendor -> Oracle Corporation
java.vendor.url -> http://java.oracle.com/
gopherProxySet -> false
sun.boot.library.path -> /Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/jre/lib
java.vm.version -> 25.201-b09
java.runtime.name -> Java(TM) SE Runtime Environment
```

