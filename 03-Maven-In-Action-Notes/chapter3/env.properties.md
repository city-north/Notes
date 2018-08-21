## 什么是构建配置文件

构建配置文件可以修改Maven在构建时的属性

开发\|测试\|生产，三种环境之间构建参数不同，需要进行切换，使用构件配置文件能够在编译的时候用不同的目标环境参数

## 配置文件类型

| 类型 | 定义位置 |
| :--- | :--- |
| Per Project | 在项目中定义的POM文件, pom.xml |
| Per User | 定义在 Maven 中的设置 XML 文件\(%USER\_HOME%/.m2/settings.xml\) |
| Global | 定义在 Maven 中的全局设置 xml 文件 \(%M2\_HOME%/conf/settings.xml\) |

## 激活方式

1. 明确使用命令从控制台输入。

2. 通过 Maven 设置。

3. 呈现/丢失的文件。

4. 基于环境变量（用户/系统变量）。

5. OS设置（例如，Windows系列）。

## 配置文件

_src/main/resources下_

| 文件名称 | 描述 |
| :--- | :--- |
| env.properties | 如果没有配置文件关联则使用默认配置 |
| env.test.properties | 当测试配置文件用于测试配置 |
| env.prod.properties | 生产配置时，prod信息被使用 |

## 通过系统环境变量配置文件

现在从 Maven 的 settings.xml 配置文件删除和更新 pom.xml 中提到的测试配置文件。profile 元素添加 activation 元素，如下所示。

当系统属性 “env” 的值设置为 “test” 时指定的测试配置文件将被触发。  
创建一个环境变量 “env”，并设置其值为 “test” 。

```
<profile>
   <id>test</id>
   <activation>
      <property>
         <name>env</name>
         <value>test</value>
      </property>
   </activation>
</profile>
```

打开命令控制台，进入到包含 pom.xml 文件的文件夹下并执行以下 mvn 命令。

```
C:\MVN\project>mvn test
```



## 更多方式

[https://www.yiibai.com/maven/maven\_build\_profiles.html](https://www.yiibai.com/maven/maven_build_profiles.html)



