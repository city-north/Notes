[返回目录](/README.md)

# 处理自动化装配的歧义性

如果不仅有一个bean能够匹配结果的话，这种歧义性会阻碍我们使用@Autowired注解标注了setDessert\(\)方法

```
@Autowired
public void setDessert(Dessert dessert){
    this.dessert = dessert;
}
```

假设有三个实现类实现了Dessert接口

```
@Component
public class Cake implements Dessert {...}

@Component
public class Cookie implements Dessert {...}

@Compoment
public class IceCream implements Dessert {...}
```

三个实现都使用了@Compoment注解，在组件扫描的时候，能够发现他们并将其创建为Spring应用上下文里面的bean，当Spring试图自动装配的时候setDessert\(\)中的Dessert参数时。它并没有唯一，无歧义的可选值。

Spring会抛出异常

```
org.springframework.beans.factory.NoUniqueBeanDefinitionException:
    No qualifying bean of type [com.desserteator.Dessert] is definded:
    expected single matching bean but found3: cake .cookies iceCream
```

如何解决

## 标示首选的bean

在声明bean的时候，通过其中一个可选的bean设置为首选（primary）bean能够避免自动装配时的歧义性。

```
@Component
@Primary
public class IceCream implements Dessert {...}
```

如果是XML

```
<bean id = "iceCream" class= "com.desserteater.IceCream" primary="true"/>
```

## 限制自动装配的bean

设置首选bean的局限性在于@Primary无法将可选方案的范围限定到唯一一个无歧义性的选项中。

@Qualifier注解是使用限定符的主要方式。它可以使用@Autowired和Inject协同使用，在注入的时候指定想要注入进去的是哪个bean

```
@Autowired
@Qualifier{"iceCream"}
public void setDessert(Dessert dessert){
    this.dessert = dessert;
}
```

### 创建自定义的限定符

使用@Component和@Bean组合使用

```
@Component
@Qualifier("cold")
public class IceCream implements Dessert{...}
```

```
@Bean
@Qualifier("cold")
public Dessert iceCream(){
    return new IceCream;
}
```

使用自定义的限定符解析



