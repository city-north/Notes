# Spring Java8 默认方法支持

> 版权声明
>
> 此篇文章参考于[logicbig](https://www.logicbig.com/),引用其文字/代码/图片仅用于学习笔记,不用于商业用途

Spring4.2 之后,可以使用 Java8 默认方法进行配置,例如`@PostConstruct`

## 代码实例

#### 一个 bean的接口

```java
package com.logicbig.example;

import javax.annotation.PostConstruct;

public interface IMyBean {
  @PostConstruct
  default void init() {
      System.out.println("post construct: "+this.getClass().getSimpleName());
  }
}
```

#### 这个 bean 的实现类

```java
package com.logicbig.example;

public class MyBean implements IMyBean {
  public void showMessage(String msg) {
      System.out.println(msg);
  }
}
```

#### 配置类

```java
package com.logicbig.example;

import org.springframework.context.annotation.Bean;

public interface IMyConfig {
  @Bean
  default MyBean myBean() {
      return new MyBean();
  }
}
```

#### 实现类

```java
package com.logicbig.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfig implements IMyConfig {
  public static void main(String[] args) {
      ApplicationContext context =
              new AnnotationConfigApplicationContext(MyConfig.class);
      MyBean bean = context.getBean(MyBean.class);
      bean.showMessage("a test message");
  }
}
```

#### 输出

如果是 4.1.9版本之前,会报错

```
Exception in thread "main" org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type [com.logicbig.example.MyBean] is defined
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.getBean(DefaultListableBeanFactory.java:371)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.getBean(DefaultListableBeanFactory.java:331)
	at org.springframework.context.support.AbstractApplicationContext.getBean(AbstractApplicationContext.java:975)
	at com.logicbig.example.MyConfig.main(MyConfig.java:13)

```

