# 使用`CommandLinePropertySource`获取应用参数

> 版权声明
>
> 此篇文章参考于[logicbig](https://www.logicbig.com/),引用其文字/代码/图片仅用于学习笔记,不用于商业用途

`CommandLinePropertySource` 是` PropertySource` 的子类,它由传递给Java应用程序的命令行参数支持。

![image-20191116215412298](assets/image-20191116215412298.png)

我们来看一看`CommandLinePropertySource`的两个实现类

- `SimpleCommandLinePropertySource`
-  `JOptCommandLinePropertySource`需要引入第三方 jar 包`jopt-simple`:一个解析命令行的 java 库

## 不使用Spring context 的情况下使用SimpleCommandLinePropertySource

我们可以在不使用Spring context 的情况下使用`SimpleCommandLinePropertySource`

```java
public class CmdSourceExample1 {

  public static void main(String[] args) {
      SimpleCommandLinePropertySource ps = new SimpleCommandLinePropertySource(args);
      Arrays.stream(ps.getPropertyNames()).forEach(s ->
              System.out.printf("%s => %s%n", s, ps.getProperty(s)));
  }
}
```

使用命令运行

```
# ec @ EricChens-Mac-mini in ~/study/Notes/00-code/02-spring-framework on git:master x [22:04:25] C:1
$ mvn -q clean compile exec:java -Dexec.mainClass="cn.eccto.study.springframework.tutorials.command.CmdSourceExample1" -Dexec.args="--myProp=testval1 --myProp2=testVal2"
myProp => testval1
myProp2 => testVal2

```

我们可以看出它输出了我们在运行命令时指定的两个参数

## 不使用Spring context 的情况下使用SimpleCommandLinePropertySource

```java
/**
 * 使用 SpringContext 的情况下使用{@link SimpleCommandLinePropertySource}
 *
 * @author EricChen 2019/11/16 22:06
 */
@Configuration
public class CmdSourceExample2 {

    @Bean
    public MyBean myBean1(){
        return new MyBean();
    }
    public static void main(String[] args) {

        PropertySource theSource = new SimpleCommandLinePropertySource(args);

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(CmdSourceExample2.class);

        context.getEnvironment().getPropertySources().addFirst(theSource);

        MyBean bean = context.getBean(MyBean.class);
        bean.doSomething();
    }


    public class MyBean {
        @Autowired
        private Environment environment;

        public void doSomething() {
            String value = environment.getProperty("myProp");
            System.out.println("the value of myProp: " + value);
        }
    }
}
```

输出

```java
# ec @ EricChens-Mac-mini in ~/study/Notes/00-code/02-spring-framework on git:master x [22:08:44] C:1
$ mvn -q clean compile exec:java -Dexec.mainClass="cn.eccto.study.springframework.tutorials.command.CmdSourceExample2" -Dexec.args="--myProp=testval1 --myProp2=testVal2"
the value of myProp: testval1

```

## 使用@Value 注解同时使用`SimpleCommandLinePropertySource`

```java
/**
 * 使用注解方式{@link Value} 注解
 *
 * @author EricChen 2019/11/16 22:12
 */
@Configuration
public class CmdSourceExample3 {

    @Bean
    public MyBean myBean(){
        return new MyBean();
    }

    public static void main(String[] args) {

        PropertySource theSource = new SimpleCommandLinePropertySource(args);

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext();

        context.getEnvironment().getPropertySources().addFirst(theSource);

        context.register(CmdSourceExample3.class);
        context.refresh();

        MyBean bean = context.getBean(MyBean.class);
        bean.doSomething();
    }

    public class MyBean {
        @Value("${myProp}")
        private String myPropValue;

        public void doSomething() {
            System.out.println("the value of myProp: " + myPropValue);
        }
    }
}

```

输出

```java
# ec @ EricChens-Mac-mini in ~/study/Notes/00-code/02-spring-framework on git:master x [22:17:12] 
$ mvn -q clean compile exec:java -Dexec.mainClass="cn.eccto.study.springframework.tutorials.command.CmdSourceExample3" -Dexec.args="--myProp=testval1 --myProp2=testVal2"
the value of myProp: testval1


```



