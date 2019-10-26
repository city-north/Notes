# Dependencies 依赖

## Inversion of Control 控制反转

控制反转是一个组装对象的过程

- 通过注入的方式将对象需要的参数注入到对象
- 通过直接构造类或者服务定位器来控制依赖想的实例化或者位置

这么做:

- 代码简洁,bean 的控制交给容器
- 将对象与它的依赖解耦,对象不需要再去寻找他的依赖,也不需要知道这些依赖的位置信息
- 测试更方便,尤其是依赖接口或者抽象类,可以在单元测试中模拟实现类

## 依赖注入

可以通过以下方式注入依赖

- 基于构造方法的依赖注入
- 基于 Setter 方法的依赖注入

### 基于构造方法的依赖注入

下面代码,只能够通过构造器注入的方式进行注入

```java
public class SimpleMovieLister {

    // the SimpleMovieLister has a dependency on a MovieFinder
    private MovieFinder movieFinder;

    // a constructor so that the Spring container can inject a MovieFinder
    public SimpleMovieLister(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

    // business logic that actually uses the injected MovieFinder is omitted...
}
```

#### 构造参数的解析

- 根据参数类型进行解析
- 如果类型模糊,根据定义时的顺序进行构造

```java
package x.y;

public class ThingOne {

    public ThingOne(ThingTwo thingTwo, ThingThree thingThree) {
        // ...
    }
}
```

可以看到上面构造器的两个形参类型不一样,你不需要指定构造参数的顺序

```xml
<beans>
    <bean id="beanOne" class="x.y.ThingOne">
        <constructor-arg ref="beanTwo"/>
        <constructor-arg ref="beanThree"/>
    </bean>

    <bean id="beanTwo" class="x.y.ThingTwo"/>

    <bean id="beanThree" class="x.y.ThingThree"/>
</beans>
```

如果是基础数据类型,直接在`value`标签内指定即可

```java
package examples;

public class ExampleBean {

    // Number of years to calculate the Ultimate Answer
    private int years;

    // The Answer to Life, the Universe, and Everything
    private String ultimateAnswer;

    public ExampleBean(int years, String ultimateAnswer) {
        this.years = years;
        this.ultimateAnswer = ultimateAnswer;
    }
}
```

直接指定两个参数:

```xml
<bean id="exampleBean" class="examples.ExampleBean">
    <constructor-arg type="int" value="7500000"/>
    <constructor-arg type="java.lang.String" value="42"/>
</bean>
```

#### 指定构造参数的顺序匹配

从0 开始:

```xml
<bean id="exampleBean" class="examples.ExampleBean">
    <constructor-arg index="0" value="7500000"/>
    <constructor-arg index="1" value="42"/>
</bean>
```

#### 指定构造参数的名字匹配

```xml
<bean id="exampleBean" class="examples.ExampleBean">
    <constructor-arg name="years" value="7500000"/>
    <constructor-arg name="ultimateAnswer" value="42"/>
</bean>
```

#### 值得注意的是

值得注意的是,如果你想要这个功能开箱即用,你的代码的 debug flag 必须启用的情况下进行编译,以便 Spring 可以从构造函数中查找参数名,如果不能或者不希望使用 debug 标签编译代码,可以使用`@ConstructorProperties` JDK 注释显式的命名构造函数参数

```java
package examples;

public class ExampleBean {

    // Fields omitted

    @ConstructorProperties({"years", "ultimateAnswer"})
    public ExampleBean(int years, String ultimateAnswer) {
        this.years = years;
        this.ultimateAnswer = ultimateAnswer;
    }
}
```



### 基于 Setter 方法的依赖注入

- 使用无参构造函数或者静态方法构造
- 调用 set 方法进行依赖注入

```java
public class SimpleMovieLister {

    // the SimpleMovieLister has a dependency on the MovieFinder
    private MovieFinder movieFinder;

    // a setter method so that the Spring container can inject a MovieFinder
    public void setMovieFinder(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

    // business logic that actually uses the injected MovieFinder is omitted...
}
```

### 使用构造方法注入还是使用 setter 方法注入

对必输参数的校验

- 构造方法注入: 原生态支持,完美支持
- 可以使用`@Required`注解设置在 setter 方法上

Spring 团队推荐使用构造注入

- 可以确保组件的不可变对象和确保依赖不为`null`
- 构造注入总返回一个完全初始化的对象
- 如果一个类构造参数过多,尝试考虑进行重构

setter 注入可以使用场景

- 对一些可选的依赖进行注入,如果不是可选的,那么注入的依赖出现的地方都得进行非空判断
- setter注入的一个好处是，setter方法使该类对象可以在以后进行重新配置或重新注入

### 依赖确认流程(Dependency Resolution Process)

容器确定一个依赖的流程如下:

- 根据配置文件元数据创建`ApplicationContext`,元数据可以是 xml,java code,或者是注解

- 对于每一个 bean,他们的依赖将使用properties ,构造器参数或者是静态工厂方法的形式表达,Spring 容器会将这些依赖提供给这个 bean

- 每个属性的构造方法时一个 set 或者是容器其他 bean

- 每个属性或者是构造器参数会被转化为属性或者是构造器参数的实际类型,默认情况下,Spring会转化`int`,`long`,`String`,`boolean`等等

  

## 使用 `Depends-on`

如果一个 bean 使用了 `depends-on`引用到另外一个 bean,那么会强制先初始化`Depends-on`指定的 bean

```xml
<bean id="beanOne" class="ExampleBean" depends-on="manager"/>
<bean id="manager" class="ManagerBean" />
```

可以一次指定多个 bean 来 depend-on:

```xml
<bean id="beanOne" class="ExampleBean" depends-on="manager,accountDao">
    <property name="manager" ref="manager" />
</bean>

<bean id="manager" class="ManagerBean" />
<bean id="accountDao" class="x.y.jdbc.JdbcAccountDao" />
```

在进行对象的销毁是,先销毁 bean,再销毁depends-on 的 bean 

## 懒加载 bean

默认情况下,`ApplicationContext`的实现类都是立即创建并配置单例的 bean,如果想让他延迟加载,使用`lazy-init`标签

```xml
<bean id="lazy" class="com.something.ExpensiveToCreateBean" lazy-init="true"/>
<bean name="not.lazy" class="com.something.AnotherBean"/>
```

下面的例子将所有 bean 都设置成懒加载

```xml
<beans default-lazy-init="true">
    <!-- no beans will be pre-instantiated... -->
</beans>	
```

## 自动装载合作者(Autowiring Collaborators )

在`<bean/>`标签下设置`autowire`属性来设置装载的模式

| Mode          | Explanation                                                  |
| :------------ | :----------------------------------------------------------- |
| `no`          | (Default) No autowiring. Bean references must be defined by `ref` elements. Changing the default setting is not recommended for larger deployments, because specifying collaborators explicitly gives greater control and clarity. To some extent, it documents the structure of a system. |
| `byName`      | Autowiring by property name. Spring looks for a bean with the same name as the property that needs to be autowired. For example, if a bean definition is set to autowire by name and it contains a `master` property (that is, it has a `setMaster(..)` method), Spring looks for a bean definition named `master` and uses it to set the property. |
| `byType`      | Lets a property be autowired if exactly one bean of the property type exists in the container. If more than one exists, a fatal exception is thrown, which indicates that you may not use `byType` autowiring for that bean. If there are no matching beans, nothing happens (the property is not set). |
| `constructor` | Analogous to `byType` but applies to constructor arguments. If there is not exactly one bean of the constructor argument type in the container, a fatal error is raised. |

### 自动装载的局限性与缺点

- 无法自动装配基本数据类型,如`int`,也无法自动装载`Strings`或者`Classes`,和简单属性的数组,这是由于涉及的限制

- 如果使用了构造器注入或者setter注入，那么将覆盖自动装配的依赖关系。
- 优先考虑使用显式的装配来进行更精确的依赖注入而不是使用自动装配

### 排除自动注入的bean

在一个 bean 基础上,你可以排除他进行自动注入,xml 配置:`<bean/>`标签下的`autowire-candidate`属性设置为 `false`,这样容器会排除这个 bean ,包括使用`@Autowired`

### 方法注入

在大多数场景下,容器中的多数 bean 都是单例的,如果一个单例的 bean 需要依赖另一个单例的 bean 或者是非单例的 bean,典型的做法是把 bean 设置成一个属性

然而如果 bean 的生命周期不同,就会有问题:

如果单例 beanA 需要使用一个非单例的 beanB,容器只创建 beanA 一次仅仅只有一次机会对属性进行初始化,这样的话 beanA 不能每一次都获取到一个新的 beanB 的实例

解决办法是使用`ApplicationContextAware`获取`ApplicationContext`,并用`ApplicationContext`来获取 beanB

```java
// a class that uses a stateful Command-style class to perform some processing
package fiona.apple;

// Spring-API imports
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class CommandManager implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public Object process(Map commandState) {
        // grab a new instance of the appropriate Command
        Command command = createCommand();
        // set the state on the (hopefully brand new) Command instance
        command.setState(commandState);
        return command.execute();
    }

    protected Command createCommand() {
        // notice the Spring API dependency!
        return this.applicationContext.getBean("command", Command.class);
    }

    public void setApplicationContext(
            ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
```

#### 查找方法注入(Lookup Method Injection)

首先写一个抽象方法,这个抽象方法会被 Spring 容器调用并返回一个动态的 Bean

下面是例子:

```java
abstract class CommandManager {

    public Object process(Object commandState) {
        // grab a new instance of the appropriate Command interface
        AsyncCommand command = createCommand();
        // set the state on the (hopefully brand new) Command instance
        command.setState(commandState);
        return command.execute();
    }

    // okay... but where is the implementation of this method?

    /**
     * 设置一个抽象方法
     * @return
     */
    protected abstract AsyncCommand createCommand();
}
```

实体类

```java
class AsyncCommand {
    Object state;

    public Object getState() {
        return state;
    }

    public void setState(Object state) {
        this.state = state;
    }

    public Object execute() {
        System.out.println("excuting->" + hashCode());
        return state;
    }
}
```



方法格式如下:

```java
<public|protected> [abstract] <return-type> theMethodName(no-arguments);
```

xml 配置文件如下,执行方法名以及 bean,可以看到 scope 为`prototype`,注意,使用`lookup-method`属性设置了方法以及返回的 bean

```xml
    <!-- a stateful bean deployed as a prototype (non-singleton) -->
    <bean id="myCommand" class="cn.eccto.study.spring_framework.look_up_method_inject.AsyncCommand" scope="prototype">
        <!-- inject dependencies here as required -->
    </bean>

    <!-- commandProcessor uses statefulCommandHelper -->
    <bean id="commandManager" class="cn.eccto.study.spring_framework.look_up_method_inject.CommandManager">
        <lookup-method name="createCommand" bean="myCommand"/>
    </bean>
```

测试类:

```java
class Test {
    private static Logger logger = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("look_up_method_inject/test_look_up_method.xml");
        CommandManager commandManager = classPathXmlApplicationContext.getBean("commandManager", CommandManager.class);
        commandManager.process("1");
        commandManager.process("2");
        commandManager.process("3");
        logger.debug("" + commandManager);
    }
}

```

输出结果:

```
excuting->390689829
excuting->252553541
excuting->1208203046
```

