# 资源作为依赖

如果bean本身要通过某种动态过程来确定和提供资源路径,对于bean来说，使用ResourceLoader接口来加载资源可能是有意义的。

例如，考虑加载某种类型的模板，其中所需的特定资源取决于用户的角色。如果资源是静态的，那么完全消除ResourceLoader接口的使用是有意义的，让bean公开它需要的资源属性，并期望将它们注入到bean中。

注入这些属性非常简单，因为所有应用程序上下文都注册并使用一个特殊的JavaBeans `PropertyEditor`，它可以将字符串路径转换为资源对象。因此，如果`myBean`有一个`Resource`类型的模板属性，它可以使用该资源的简单字符串进行配置，如下面的示例所示:

```xml
<bean id="myBean" class="...">
    <property name="template" value="some/resource/path/myTemplate.txt"/>
</bean>
```

注意这个资源没有前缀,因此,由于应用程序上下文本身将用作`ResourceLoader`，所以根据上下文的确切类型，通过`ClassPathResource`、`FileSystemResource`或`ServletContextResource`加载资源本身。

当然你也可以指定:

- 使用`ClassPathResource`

```xml
<property name="template" value="classpath:some/resource/path/myTemplate.txt">
```

- 使用`UrlResource`

```xml
<property name="template" value="file:///some/resource/path/myTemplate.txt"/>
```

