[返回目录](/README.md)

# 运行时注入

1. 注入的外部的值
2. 使用Spring表达式语言进行装配

将专辑的名字装配到BlankDisc bean的构造器或者title中：

```
@Bean
public CompactDisc sgtPeppers(){
    return new BlankDisc("mytittle","Beatles");
}
```

```
<bean id="sgtPeppers" 
      class="soundsystem.BlankDisc" 
      c:_title="XXX" 
      c:_artist="XXX" />
```

以上使用的是硬解码的方式，但是有些时候，我们想让值在运行时再确定。

Spring提供了两种在运行时求值得方法：

* 属性占位符
* Spring表达式语句

## 注入外部的值

在Spring中，处理外部值得最简单方式就是声明属性源并通过Spring的Environment来检索属性。

例子：使用外部的属性来装配BlankDisc bean.

```
package com.soundsystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:/com/soundsystem/app.properties")
public class EnvironmentConfig {

  @Autowired
  Environment env;

  @Bean
  public BlankDisc blankDisc() {
    return new BlankDisc(
        env.getProperty("disc.title"),
        env.getProperty("disc.artist"));
  }

}
```

引入了一个名字为app.properties的文件：

```
disc.title=Sgt. Peppers Lonely Hearts Club Band
disc.artist=The Beatles
```

`@PropertySource`注解引用了类路径中一个名为app.properties的文件，这个属性文件会加载到Spring的Environment中，稍后可以从这里检索属性。同时，在blankDisc方法中，会创建一个新的BlankDisc，它的构造器参数时从属性文件中获取的，用过getProperty\(\)实现。

如果想在获取属性前，查看是否存在某个属性，可以使用getPropertyAsClass\(\)方法:

```
boolean titleExists = env.containsProperty("disc.title");
```

如果想将属性解析为类的话：

```
Class<CompactDisc> adClass = env.getPropertyAsClass("diss.class",CompactDisc.class);
```

查询哪些profile处于激活状态：

* String\[\] getActiveProfiles\(\):返回激活profile名称的数组;
* String\[\] getDefaultProfiles\(\):返回默认profile名称的属组
* boolean acceptsProfiles\(String... profiles\):如果environment支持给定profile的话，就返回true.

### 总结：

直接从Environment中检索属性是非常方便的，尤其是在Java配置中装配bean的时候，但是，Spring也提供了通过通配符装配属性的方法，这些占位符的值源于一个属性源。

## 解析属性占位符

为了能够使用占位符，我们必须配置一个`PropertyPlaceholderConfigurer`bean 或者`PropertySourcesPlaceholderConfigurer`bean。从Spring 3.1开始，推荐使用`PropertySourcesPlaceholderConfigurer`bean,因为它能基于Spring Environment 以及其属性源来解析占位符。

如：

```
@Bean
public static PropertySourcesPlaceholderConfigurer placeholderConfigurer(){
    return new PropertySourcesPlaceholderConfigurer();
}
```

在Spring装配时，占位符的形式为：

```
@Value("#{configProperties['key']}")
//或者
@Value("${key}")
```

### @Value\("\#{configProperties\['key'\]}"\)使用

Spirng配置文件

```
<bean id="configProperties" class="org.springframework.beans.factory.config.PropertiesFactoryBean">
    <property name="locations">
        <list>
            <value>classpath:value.properties</value>
        </list>
    </property>
</bean>
```

或者

```
<util:properties id="configProperties" location="classpath:value.properties"></util:properties>
```

使用

```
@Component
public class ValueDemo {
    @Value("#{configProperties['key']}")
    private String value;

    public String getValue() {
        return value;
    }
}
```

### @Value\("${key}"\)

配置文件

```
<bean id="configProperties" class="org.springframework.beans.factory.config.PropertiesFactoryBean">
    <property name="locations">
        <list>
            <value>classpath:value.properties</value>
        </list>
    </property>
</bean>
<bean id="propertyConfigurer" class="org.springframework.beans.factory.config.PreferencesPlaceholderConfigurer">
    <property name="properties" ref="configProperties"/>
</bean>
```

```
<bean id="appProperty"
          class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
    <property name="locations">
        <array>
            <value>classpath:value.properties</value>
        </array>
    </property>
</bean>
```

使用：

```
@Component
public class ValueDemo {
    @Value("${key}")
    private String value;

    public String getValue() {
        return value;
    }
}
```

# 使用Spring表达式语言进行装配

Spring表达式语言（Spring Expression Language,SpEL）,能够以强大和简洁的方式将值装配到bean属性和构造器参数中。

SpEL特性：

* 使用bean的ID来引用bean
* 调用方法和访问对象的属性
* 对值进行算术，关系和逻辑运算
* 正则表达式匹配
* 集合操作

### SpEL例子

SpEL表达式要放到“\#{...}”中，

属性占位符需要放到“${...}”之中。

#### 表面字面值

浮点数：

```
#{3.14159
```

科学计数法：下面的值为\#{98700}

```
#{9.87E4}
```

用来计算String类的字面值：如下面的值为\#{false}

```
#{'Hello'}
```

#### 引用bean、属性和方法

假设要引用sgtPreppers的artist属性：

```
#{sgtPreppers.arti}
```

调用bena上的方法，假设有个bean，id 是userService:

```
#{userService.selectAll()}
```

对于被调用方法的返回值来说，我们同样可以调用它的方法：如果selectAll方法返回一个String:

```
#{userService.selectAll().toUpperCase()}
```

为了避免出现空指针异常，我们可以使用`?.`运算符。如果前面的结果为null，将不会调用toUpperCase方法。

```
#{userSerivce.selectAll()?.toUpperCase()}
```

#### 在表达式中使用类型

通过T\(\)调用得到0和1之间的随机数

```
T(java.lang.Math).random()
```

获取当前时间

`T(System.currentTimeMillis())`



[返回目录](/README.md)

