[返回根目录](/README.md)

[返回目录](../README.md)

# 使用应用上下文

Spring自带了许多类型的应用上下文，常用的：

| 上下文 | 备注 |
| :--- | :--- |
| AnnotationConfigApplicationContext | 从一个或多个基于Java的配置类中加载Spring应用上下文 |
| AnnotationConfigWebApplicationContext | 从一个或多个基于Java的配置类中加载SpringWeb应用上下文 |
| ClassPathXmlApplicationContext | 从类路径下的一个或多个XML配置文件中加载上下文定义，把应用上下文的定义文件作为资源类 |
| FileSystemXmlapplicationcontext | 从文件系统下的一个或者多个XML配置恩建中加载上下文定义 |
| XmlWebApplicationContext | 从Web应用下的一个或多个XML配置文件中加载上下文定义 |

## 从文件中读取上下文

FileSystemXmlapplicationcontext



```
    ApplicationContext context 
    =new FileSystemXmlApplicationContext( "META-INF/spring/knight.xml");
```

ClassPathXmlApplicationContext

```
ApplicationContext context = new ClassPathXmlApplicationContext("knight.xml")
```

FileSystemXmlapplicationcontext和ClassPathXmlApplicationContext的区别：

FileSystemXmlapplicationcontext在指定的文件系统路径下查找文件

ClassPathXmlApplicationContext是在鄋的类路径（包括Jar文件）下查找knight.xml

## 从Java配置中加载应用上下文

```
ApplicationContext context = new AnnotationConfigApplicationContext(
com.springinaction.knights.config.KnightConfig.class)
```

加载完上下文后，使用上下文的getBean\(\)方法从Spring容器中获取bean


[返回根目录](/README.md)

[返回目录](../README.md)
