~~~
<mirror>
        <id>nexus-aliyun</id>
        <mirrorOf>central</mirrorOf>
        <name>Nexus aliyun</name>
<url>http://maven.aliyun.com/nexus/content/groups/public</url>
</mirror>
~~~

- mirrorOf:匹配远程仓库的名称，*为所有远程：

```
        <mirrorOf>*</mirrorOf>  
```

-  匹配所有远程仓库，使用localhost的除外，使用file//协议的除外，也就是说匹配所有不在本机上地远程仓库：


```
        <mirrorOf>external:*</mirrorOf>  
```

- 匹配仓库repo1和repo2，使用逗号分隔:

```
        <mirrorOf>repo1,repo2</mirrorOf>  
```

-   匹配所有远程仓库，除了repo1，使用感叹号将仓库从匹配中排除。

```
        <mirrorOf>*，！repo1</mirrorOf>  
```

  

