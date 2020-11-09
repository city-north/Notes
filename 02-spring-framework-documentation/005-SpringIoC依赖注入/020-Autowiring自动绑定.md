# 020-Autowiring自动绑定

- [为什么Spring要引入Autowring自动绑定](#为什么Spring要引入Autowring自动绑定)
- [自动绑定的类型](#自动绑定的类型)
- 各种自动绑定模式的使用场景是什么
- [自动绑定的限制与不足](#自动绑定的限制与不足)

## 为什么Spring要引入Autowring自动绑定

Spring容器可以自动绑定bean和它的合作者（不一定是bean,也有可能是一些基础类型）

优势

- 自动绑定可以显著地减少属性和构造器参数 的设定 

  > 例如当我们使用autowire="byName"，那么我们就不用写这个属性，自动根据类型去注入

- 配置的对象会根据bean的更改而自动更改（引用的是地址）

## 自动绑定的类型

设置方式 ： autowire =""

| 模式                | 说明                                                         |
| ------------------- | ------------------------------------------------------------ |
| no                  | 默认值，未激活Autowiring,需要手动指定以来注入对象            |
| byName              | 根据被注入属性的名称作为Bean名称进行依赖查找，并将都西昂设置到该属性 |
| byType              | 根据被注入属性的类型作为依赖类型进行查找，并将对象设置到该属性 |
| constructor         | 特殊的byType类型，用于构造器类型                             |
| AUTOWIRE_AUTODETECT | 不推荐使用。3.0之前的                                        |

## 四种绑定类型

- type

```xml
    <bean class="org.geekbang.thinking.in.spring.ioc.dependency.injection.UserHolder"
          autowire=""> 
        <property name= "user" ref="superUser" /> 替换成 autowiring 模式 -->
    </bean>
```

## 自动绑定的限制与不足

- 设计上的缺陷： `properties`和`constructor-arg`中显示的以来，你无法自动装配一些简单类型，例如基础类型，String，Class或者这些类型的数组
- 自动绑定相对手动绑定会缺乏准确度，Spring会非常小心的避免歧义，但是有时候还是会出错，比如我们定义了一个User和SuperUser，当我们在Bean中定义一个User属性的时候，byName有可能会产生歧义
- 自动绑定的信息很确保在classpath下存在

- Multiple bean definitions within the container may match the type specified by the setter method or constructor argument to be autowired. For arrays, collections, or `Map` instances, this is not necessarily a problem. However, for dependencies that expect a single value, this ambiguity is not arbitrarily resolved. If no unique bean definition is available, an exception is thrown.

In the latter scenario, you have several options:

- 放弃autowiring，使用手动绑定
- 设置bean的primary属性
- 使用注解定义