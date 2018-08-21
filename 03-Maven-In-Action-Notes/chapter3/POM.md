[返回目录](../README.md)

## POM文件与代码隔离

在没有实际Java代码的情况下，我们也可以定义一个Maven项目的POM,这体现了Maven的一大优点，它能让项目对象模型最大程度上地与实际代码相独立，我们称之为**解耦**，或者正交性。

这在很大程度上避免了Java代码和POM代码之间的互相影响。比如当项目需要升级版本时候，只需要修改POM,而不需要修改Java代码；而在POM稳定之后，日常的Java代码开发工作基本不涉及POM的修改。

## 编写POM

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>
    <groupId>com.juvenxu.nvnbook</groupId>
    <artifactId>hello-world</artifactId>
    <packaging>pom</packaging>
    <version>1.0-RELEASE</version>
    <name> Maven Hello World Project</name>
</project>
```

1. **groupId**定义了项目属于哪个组，往往和公司和所在组织有关，如com.googlecode.myapp
2. **artifactId**定义了当前Maven项目在组中的唯一ID,可以为不同的项目分配子项目，如myapp-util、myapp-domain、myapp-web等。
3. **version**定义了版本号。**SNAPSHOT**意思是快照，意味着该项目还在开发中，是不稳定的版本。随着项目的发展，version会不断更新，如升级为2.0-SNAPSHOT
4. **name**声明了一个对用户更为友好的项目名称，非必须，但是推荐添加，方便交流。
5. **packaging**是该maven项目的打包方式，pom=父工程，jar=打包jar\(**默认**\)，war=打包war



