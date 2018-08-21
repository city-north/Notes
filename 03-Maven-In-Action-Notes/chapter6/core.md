[返回目录](/README.md)

# core解析

## build中的节点

```
<build>
        <finalName>core</finalName>
        <resources>
                ...
        </resources>
        <plugins>
                ...
        </plugins>
    </build>
```

* resources 是指项目资源目录

* plugins指定插件

## resource节点

```
<resources>
    <resource>
        <directory>src/main/java/</directory>
        <filtering>true</filtering>
        <includes>
            <include>**/*.groovy</include>
            <include>**/*.xml</include>
        </includes>
    </resource>
    <resource>
        <directory>src/main/resources</directory>
        <filtering>true</filtering>
        <includes>
            <include>**/*.xml</include>
        </includes>
    </resource>
    <resource>
        <directory>src/main/resources/profiles/${profile.env}</directory>
        <filtering>true</filtering>
        <includes>
            <include>**/*.properties</include>
            <include>**/*.xml</include>
        </includes>
    </resource>
</resources>
```

* Filtering是Maven Resources Plugin的一个功能，作用是使用系统属性或者项目属性的值替换资源文件（\*.properties，\*.xml）中的${}符号的值。
* 比如系统属性中有一个“user.name=foobar”,那么资源文件中的${user.name}符号会在Maven编译时候自动替换为“foobar”。

```
<?xml version="1.0" encoding="UTF-8"?>
<!--suppress MavenModelInspection -->
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    <parent>
        <artifactId>HbiParent</artifactId>
        <groupId>hbi</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>core</artifactId>
    <packaging>war</packaging>
    <name>core</name>
    <url>http://maven.apache.org</url>
    <dependencies>
        <!-- core db依赖 -->
        <dependency>
            <groupId>hbi</groupId>
            <artifactId>core-db</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>

    </dependencies>
    <build>
        <finalName>core</finalName>
        <resources>
            <resource>
                <directory>src/main/java/</directory>
                <filtering>true</filtering>
                <includes>
                    <include>**/*.groovy</include>
                    <include>**/*.xml</include>
                </includes>
            </resource>
            <resource>
                <directory>src/main/resources</directory>
                <filtering>true</filtering>
                <includes>
                    <include>**/*.xml</include>
                </includes>
            </resource>
            <resource>
                <directory>src/main/resources/profiles/${profile.env}</directory>
                <filtering>true</filtering>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
            </resource>

        </resources>
        <plugins>
            <plugin>
                <groupId>org.liquibase</groupId>
                <artifactId>liquibase-maven-plugin</artifactId>
                <version>3.4.2</version>
                <configuration>
                    <skip>${skipLiquibaseRun}</skip>
                </configuration>
                <dependencies>
                    <dependency>
                        <groupId>hbi</groupId>
                        <artifactId>core-db</artifactId>
                        <version>1.0-SNAPSHOT</version>
                    </dependency>
                </dependencies>
                <executions>
                    <execution>
                        <id>clearCheckSums</id>
                        <phase>process-resources</phase>
                        <configuration>
                            <driver>${db.driver}</driver>
                            <url>${db.url}</url>
                            <username>${db.user}</username>
                            <password>${db.password}</password>
                            <promptOnNonLocalDatabase>false</promptOnNonLocalDatabase>
                        </configuration>
                        <goals>
                            <goal>clearCheckSums</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>update</id>
                        <phase>process-resources</phase>
                        <configuration>
                            <changeLogFile>hbi/core/db/liquibase.groovy</changeLogFile>
                            <driver>${db.driver}</driver>
                            <url>${db.url}</url>
                            <username>${db.user}</username>
                            <password>${db.password}</password>
                            <promptOnNonLocalDatabase>false</promptOnNonLocalDatabase>
                        </configuration>
                        <goals>
                            <goal>update</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <version>2.6</version>
                <configuration>
                    <attachClasses>true</attachClasses>
                    <overlays>
                        <overlay>
                            <groupId>com.hand</groupId>
                            <artifactId>hap</artifactId>
                        </overlay>

                        <!-- 合并core war包 -->
                        <!--<overlay>
                            <groupId>com.hand</groupId>
                            <artifactId>hap-core</artifactId>
                        </overlay>-->

                        <!-- 合并服务注册 war包 -->
                  <!--      <overlay>
                            <groupId>com.hand</groupId>
                            <artifactId>hap-gateway</artifactId>
                        </overlay>-->

                        <!-- 合并oauth2 war包 -->
                      <!--  <overlay>
                            <groupId>com.hand</groupId>
                            <artifactId>hap-oauth2</artifactId>
                        </overlay>-->

                        <!-- 合并接口管理 war包 -->
                      <!--  <overlay>
                            <groupId>com.hand</groupId>
                            <artifactId>hap-interface</artifactId>
                        </overlay>-->

                        <!-- 合并计划任务 war包 -->
                      <!--  <overlay>
                            <groupId>com.hand</groupId>
                            <artifactId>hap-job</artifactId>
                        </overlay>-->

                        <!-- 合并邮件 war包 -->
                     <!--   <overlay>
                            <groupId>com.hand</groupId>
                            <artifactId>hap-mail</artifactId>
                        </overlay>
-->
                        <!-- 合并task war包 -->
                     <!--   <overlay>
                            <groupId>com.hand</groupId>
                            <artifactId>hap-task</artifactId>
                        </overlay>-->

                        <!-- 合并工作流 war包 -->
                       <!-- <overlay>
                            <groupId>com.hand</groupId>
                            <artifactId>hap-workflow</artifactId>
                        </overlay>-->

                        <!-- 合并报表 war包 -->
                    <!--    <overlay>
                            <groupId>com.hand</groupId>
                            <artifactId>hap-report</artifactId>
                        </overlay>-->
                    </overlays>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.eclipse.jetty</groupId>
                <artifactId>jetty-maven-plugin</artifactId>
                <version>9.2.11.v20150529</version>
                <configuration>
                    <webAppConfig>
                        <allowDuplicateFragmentNames>true</allowDuplicateFragmentNames>
                    </webAppConfig>
                </configuration>
            </plugin>
        </plugins>
    </build>
    <profiles>
        <profile>
            <id>dev</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <properties>
                <profile.env>dev</profile.env>
            </properties>
        </profile>
        <profile>
            <id>sit</id>
            <activation />
            <properties>
                <profile.env>sit</profile.env>
            </properties>
        </profile>
        <profile>
            <id>uat</id>
            <activation />
            <properties>
                <profile.env>uat</profile.env>
            </properties>
        </profile>
    </profiles>
</project>
```



