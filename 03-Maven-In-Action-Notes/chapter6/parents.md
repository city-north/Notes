# 一个父文件结构解析



## 头

```
<modelVersion>4.0.0</modelVersion>
<groupId>hbi</groupId>
<artifactId>HbiParent</artifactId>
<packaging>pom</packaging>
<version>1.0-SNAPSHOT</version>
```

* **modelVersion** 指定了当前Maven模型的版本号，对于Maven2和Maven3来说，它只能是4.0.0 
* **groupId** 公司名或是组织名 
* **artifactId** Maven构建的项目名 
* **packaging** 项目打包的类型，可以使jar、war、rar、ear、pom，默认是jar 
* **version**  版本号 

### properties

定义后文用到的属性

```
    <properties>
        <!--<hap.version>2.1.4-Release</hap.version>-->
        <hap.version>3.5.0-RELEASE</hap.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <org.slf4j-version>1.7.21</org.slf4j-version>
        <log4j.version>2.4.1</log4j.version>
        <disruptor.version>3.2.0</disruptor.version>
        <skipLiquibaseRun>true</skipLiquibaseRun>
        <groovy.version>2.4.1</groovy.version>
        <gmaven.version>1.5</gmaven.version>
        <skipTests>true</skipTests>
    </properties>
```

### distributionManagement

```
    <distributionManagement>
        <repository>
            <id>releases</id>
            <url>http://nexus.saas.hand-china.com/content/repositories/rdc</url>
        </repository>

        <snapshotRepository>
            <id>snapshots</id>
            <name>Internal Snapshots</name>
            <url>http://nexus.saas.hand-china.com/content/repositories/rdcsnapshot</url>
        </snapshotRepository>
    </distributionManagement>
```



- **distributionManagement** 负责管理构件的发布 
- **repository** 发布releases版本仓库地址
- **snapshotRepository**  发布快照版本仓库地址



### pluginRepositories

```
    <pluginRepositories>
        <pluginRepository>
            <id>RDC</id>
            <name>RDC Repository</name>
            <url>http://nexus.saas.hand-china.com/content/repositories/rdc</url>
        </pluginRepository>
        <pluginRepository>
            <id>RDC snapshot</id>
            <name>RDC snapshot Repository</name>
            <url>http://nexus.saas.hand-china.com/content/repositories/rdcsnapshot</url>
        </pluginRepository>
        <pluginRepository>
            <id>RDC thirdparty</id>
            <name>RDC thirdparty Repository</name>
            <url>http://nexus.saas.hand-china.com/content/repositories/thirdparty</url>
        </pluginRepository>
    </pluginRepositories>
```

- **pluginRepository**私服配置 





### 完整

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>hbi</groupId>
    <artifactId>HbiParent</artifactId>
    <packaging>pom</packaging>
    <version>1.0-SNAPSHOT</version>
    <properties>
        <!--<hap.version>2.1.4-Release</hap.version>-->
        <hap.version>3.5.0-RELEASE</hap.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <org.slf4j-version>1.7.21</org.slf4j-version>
        <log4j.version>2.4.1</log4j.version>
        <disruptor.version>3.2.0</disruptor.version>
        <skipLiquibaseRun>true</skipLiquibaseRun>
        <groovy.version>2.4.1</groovy.version>
        <gmaven.version>1.5</gmaven.version>
        <skipTests>true</skipTests>
    </properties>
    <distributionManagement>
        <repository>
            <id>releases</id>
            <url>http://nexus.saas.hand-china.com/content/repositories/rdc</url>
        </repository>

        <snapshotRepository>
            <id>snapshots</id>
            <name>Internal Snapshots</name>
            <url>http://nexus.saas.hand-china.com/content/repositories/rdcsnapshot</url>
        </snapshotRepository>
    </distributionManagement>
    <repositories>
        <repository>
            <id>RDC</id>
            <name>RDC Repository</name>
            <url>http://nexus.saas.hand-china.com/content/repositories/rdc</url>
        </repository>
        <repository>
            <id>RDC snapshot</id>
            <name>RDC snapshot Repository</name>
            <url>http://nexus.saas.hand-china.com/content/repositories/rdcsnapshot</url>
        </repository>
        <repository>
            <id>RDC thirdparty</id>
            <name>RDC thirdparty Repository</name>
            <url>http://nexus.saas.hand-china.com/content/repositories/thirdparty</url>
        </repository>
    </repositories>
    <pluginRepositories>
        <pluginRepository>
            <id>RDC</id>
            <name>RDC Repository</name>
            <url>http://nexus.saas.hand-china.com/content/repositories/rdc</url>
        </pluginRepository>
        <pluginRepository>
            <id>RDC snapshot</id>
            <name>RDC snapshot Repository</name>
            <url>http://nexus.saas.hand-china.com/content/repositories/rdcsnapshot</url>
        </pluginRepository>
        <pluginRepository>
            <id>RDC thirdparty</id>
            <name>RDC thirdparty Repository</name>
            <url>http://nexus.saas.hand-china.com/content/repositories/thirdparty</url>
        </pluginRepository>
    </pluginRepositories>
  <modules>
    <module>core</module>
    <module>core-db</module>
  </modules>
</project>
```