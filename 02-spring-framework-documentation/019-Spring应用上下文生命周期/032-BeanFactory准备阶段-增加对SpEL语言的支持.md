# 032-BeanFactory准备阶段-增加对SpEL语言的支持

Spring表达式语言全称为Spring Expression Language，缩写为SpEL，类似于Struts 2x中使用的OGNL表达式语言，能在运行时构建复杂表达式、存取对象图属性、对象方法调用等，并且能与Spring功能完美整合，比如能用来配置bean定义。SpEL是单独模块，只依赖于core模块，不依赖于其他模块，可以单独使用。
SpEL使用#{…}作为定界符，所有在大框号中的字符都将被认为是SpEL，使用格式如下：

```xml
<bean id="saxophone" value="com.xxx.xxx.Xxx"/>  
<bean >  
     <property name="instrument" value="#{saxophone}"/>  
<bean/>
```


相当于：

```
<bean id="saxophone" value="com.xxx.xxx.Xxx"/>  
<bean >  
     <property name="instrument" ref="saxophone"/>  
<bean/>
```

当然，上面只是列举了其中最简单的使用方式，SpEL功能非常强大，使用好可以大大提高开发效率，这里只为唤起读者的记忆来帮助我们理解源码，有兴趣的读者可以进一步深入研究。

在源码中通过代码

```java
beanFactory.setBeanExpressionResolver(new StandardBeanExpression- Resolver())
```

注册语言解析器，就可以对SpEL进行解析了，那么在注册解析器后Spring又是在什么时候调用这个解析器进行解析呢？

之前我们讲解过Spring在bean进行初始化的时候会有属性填充的一步，而在这一步中Spring会调用AbstractAutowireCapableBeanFactory类的applyPropertyValues函数来完成功能。

就在这个函数中，会通过构造BeanDefinitionValueResolver类型实例valueResolver来进行属性值的解析。同时，也是在这个步骤中一般通过AbstractBeanFactory中的evaluateBeanDefinitionString方法去完成SpEL的解析。

```java
protected Object evaluateBeanDefinitionString(String value, BeanDefinition beanDefinition) {
         if (this.beanExpressionResolver == null) {
             return value;
         }
         Scope scope = (beanDefinition != null ? getRegisteredScope(beanDefinition.getScope()) : null);
         return this.beanExpressionResolver.evaluate(value, new BeanExpressionContext(this, scope));
}
```

当调用这个方法时会判断是否存在语言解析器，如果存在则调用语言解析器的方法进行解析，解析的过程是在Spring的expression的包内，这里不做过多解释。我们通过查看对evaluateBeanDefinitionString方法的调用层次可以看出，应用语言解析器的调用主要是在解析依赖注入bean的时候，以及在完成bean的初始化和属性获取后进行属性填充的时候。

## 编程方式使用SpEL表达式了

```java
public static void main(String[] args) {
  ExpressionParser expressionParser = new SpelExpressionParser();
  Expression exp = expressionParser.parseExpression("'Hello World'.concat('!')");
  final Object value = (String)exp.getValue();
  System.out.println(value);//Hello World!
}
```

#### 