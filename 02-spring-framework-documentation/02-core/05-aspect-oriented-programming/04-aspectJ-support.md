# ` @AspectJ`支持

## 启用`@AspectJ`

#### 注解启用

- 添加`@Configuration`,

- 添加`@EnableAspectJAutoProxy`

```java
@Configuration
@EnableAspectJAutoProxy
public class AppConfig {

}
```

#### xml 配置启用

```xml
<aop:aspectj-autoproxy/>
```

## 声明一个切面

```xml
<bean id="myAspect" class="org.xyz.NotVeryUsefulAspect">
    <!-- configure properties of the aspect here -->
</bean>
```

#### 使用注解标注

```java
package org.xyz;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class NotVeryUsefulAspect {

}
```

### 声明一个切点

使用注解`@Pointcut`声明切点表达式

```java
@Pointcut("execution(* transfer(..))") // the pointcut expression
private void anyOldTransfer() {} // the pointcut signature

```

### 支持的切点表达式

- `execution`: For matching method execution join points. This is the primary pointcut designator to use when working with Spring AOP.
- `within`: Limits matching to join points within certain types (the execution of a method declared within a matching type when using Spring AOP).
- `this`: Limits matching to join points (the execution of methods when using Spring AOP) where the bean reference (Spring AOP proxy) is an instance of the given type.
- `target`: Limits matching to join points (the execution of methods when using Spring AOP) where the target object (application object being proxied) is an instance of the given type.
- `args`: Limits matching to join points (the execution of methods when using Spring AOP) where the arguments are instances of the given types.
- `@target`: Limits matching to join points (the execution of methods when using Spring AOP) where the class of the executing object has an annotation of the given type.
- `@args`: Limits matching to join points (the execution of methods when using Spring AOP) where the runtime type of the actual arguments passed have annotations of the given types.
- `@within`: Limits matching to join points within types that have the given annotation (the execution of methods declared in types with the given annotation when using Spring AOP).
- `@annotation`: Limits matching to join points where the subject of the join point (the method being executed in Spring AOP) has the given annotation.

#### 组合切点表达式

```java
@Pointcut("execution(public * (..))")
private void anyPublicOperation() {} 

@Pointcut("within(com.xyz.someapp.trading..)")
private void inTrading() {} 

@Pointcut("anyPublicOperation() && inTrading()")
private void tradingOperation() {} 
```

- `anyPublicOperation` 如果方法执行切入点表示任何public方法的执行，则匹配
- `inTrading`如果方法执行在交易模块中，则匹配。
- `tradingOperation` 如果方法执行代表交易模块中的任何公共方法，则匹配。

#### 共享公共的切入点定义

在使用企业应用程序时，开发人员通常希望从几个方面引用应用程序的模块和特定的操作集。我们建议定义一个“系统架构”方面，它可以捕获用于此目的的公共切入点表达式。这样一个方面通常类似于下面的例子:

```java
package com.xyz.someapp;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class SystemArchitecture {

    /
     * A join point is in the web layer if the method is defined
     * in a type in the com.xyz.someapp.web package or any sub-package
     * under that.
     /
    @Pointcut("within(com.xyz.someapp.web..)")
    public void inWebLayer() {}

    /
     * A join point is in the service layer if the method is defined
     * in a type in the com.xyz.someapp.service package or any sub-package
     * under that.
     /
    @Pointcut("within(com.xyz.someapp.service..)")
    public void inServiceLayer() {}

    /
     * A join point is in the data access layer if the method is defined
     * in a type in the com.xyz.someapp.dao package or any sub-package
     * under that.
     /
    @Pointcut("within(com.xyz.someapp.dao..)")
    public void inDataAccessLayer() {}

    /
     * A business service is the execution of any method defined on a service
     * interface. This definition assumes that interfaces are placed in the
     * "service" package, and that implementation types are in sub-packages.
     *
     * If you group service interfaces by functional area (for example,
     * in packages com.xyz.someapp.abc.service and com.xyz.someapp.def.service) then
     * the pointcut expression "execution(* com.xyz.someapp..service..(..))"
     * could be used instead.
     *
     * Alternatively, you can write the expression using the 'bean'
     * PCD, like so "bean(Service)". (This assumes that you have
     * named your Spring service beans in a consistent fashion.)
     */
    @Pointcut("execution( com.xyz.someapp..service..(..))")
    public void businessService() {}

    /*
     * A data access operation is the execution of any method defined on a
     * dao interface. This definition assumes that interfaces are placed in the
     * "dao" package, and that implementation types are in sub-packages.
     */
    @Pointcut("execution( com.xyz.someapp.dao..(..))")
    public void dataAccessOperation() {}

}
```

您可以在任何需要切入点表达式的地方引用在这样一个方面中定义的切入点。例如，要使服务层具有事务性，可以编写以下代码:

```xml
<aop:config>
    <aop:advisor
        pointcut="com.xyz.someapp.SystemArchitecture.businessService()"
        advice-ref="tx-advice"/>
</aop:config>

<tx:advice id="tx-advice">
    <tx:attributes>
        <tx:method name="*" propagation="REQUIRED"/>
    </tx:attributes>
</tx:advice>
```

## 实例

- 所有 public 方法

  ```
  execution(public * *(..))
  ```

- 所有方法名由`set`开头的

  ```
  execution(* set*(..))
  ```

-  `AccountService` interface中的所有方法

  ```
  execution(* com.xyz.service.AccountService.*(..))
  ```

-  `service` package中定义的所有方法

  ```
  execution(* com.xyz.service..(..))
  ```

- service 包和其子包下的所有方法

  ```
  execution(* com.xyz.service...(..))
  ```

- 服务包内的任何连接点(仅在Spring AOP中执行方法):

  ```
  within(com.xyz.service.*)
  ```

- 服务包或其子包中的任何连接点(仅在Spring AOP中执行方法):

  ```
  within(com.xyz.service..*)
  ```

- 任何连接点(仅在Spring AOP中执行方法)，其中代理实现了`AccountService`接口:

  ```
  this(com.xyz.service.AccountService)
  ```

- 目标对象实现`AccountService`接口的任何连接点(仅在Spring AOP中执行方法)

  ```
  target(com.xyz.service.AccountService)
  ```

- 任何连接点(只在Spring AOP中执行方法)，它只接受一个参数，并且在运行时传递的参数是`Serializable`:

  ```
  args(java.io.Serializable)
  ```

- 任何连接点(仅在Spring AOP中执行方法)，其中目标对象的声明类型有一个`@Transactional`注释:

  ```
  @within(org.springframework.transaction.annotation.Transactional)
  ```

- 任何接受单个参数的连接点(仅在Spring AOP中执行方法)，其中传递的参数的运行时类型有` @ classification `注释:

  ```
  @args(com.xyz.security.Classified)
  ```

- 在名为`tradeService`的Spring bean上的任何连接点(仅在Spring AOP中执行方法):

- ```
  bean(tradeService)
  ```

- 在名称与通配符表达式`*Service`匹配的Spring bean上的任何连接点(仅在Spring AOP中执行方法)

  ```
  bean(*Service)
  ```

## 声明通知

### 前置通知

```java
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class BeforeExample {

    @Before("com.xyz.myapp.SystemArchitecture.dataAccessOperation()")
    public void doAccessCheck() {
        // ...
    }

}
```

如果我们使用一个插入的切入点表达式，我们可以将前面的例子重写为下面的例子:

```java
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class BeforeExample {

    @Before("execution(* com.xyz.myapp.dao..(..))")
    public void doAccessCheck() {
        // ...
    }

}
```

### 返回通知

```java
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.AfterReturning;

@Aspect
public class AfterReturningExample {

    @AfterReturning("com.xyz.myapp.SystemArchitecture.dataAccessOperation()")
    public void doAccessCheck() {
        // ...
    }

}
```

有时，您需要在advice主体中访问返回的实际值。您可以使用`@ afterreturn`的形式绑定返回值来获得访问权限，如下面的例子所示:

```java
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.AfterReturning;

@Aspect
public class AfterReturningExample {

    @AfterReturning(
        pointcut="com.xyz.myapp.SystemArchitecture.dataAccessOperation()",
        returning="retVal")
    public void doAccessCheck(Object retVal) {
        // ...
    }

}
```

### 异常通知

```java
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.AfterThrowing;

@Aspect
public class AfterThrowingExample {

    @AfterThrowing("com.xyz.myapp.SystemArchitecture.dataAccessOperation()")
    public void doRecoveryActions() {
        // ...
    }

}
```

获取异常作为参数进行处理

```kava
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.AfterThrowing;

@Aspect
public class AfterThrowingExample {

    @AfterThrowing(
        pointcut="com.xyz.myapp.SystemArchitecture.dataAccessOperation()",
        throwing="ex")
    public void doRecoveryActions(DataAccessException ex) {
        // ...
    }

}
```

## 后置通知

```java
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.After;

@Aspect
public class AfterFinallyExample {

    @After("com.xyz.myapp.SystemArchitecture.dataAccessOperation()")
    public void doReleaseLock() {
        // ...
    }

}
```

## 环绕通知

```java
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.ProceedingJoinPoint;

@Aspect
public class AroundExample {

    @Around("com.xyz.myapp.SystemArchitecture.businessService()")
    public Object doBasicProfiling(ProceedingJoinPoint pjp) throws Throwable {
        // start stopwatch
        Object retVal = pjp.proceed();
        // stop stopwatch
        return retVal;
    }

}
```

