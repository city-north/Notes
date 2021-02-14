# 060-Spring-Validator的救赎

[TOC]

## 为什么

直接使用Validator API 会非常复杂,除了我们要校验的对象以外,我们还要传递Errors对象

Errors对象有多重选型,有时候我们也不好区分

## 怎么救赎

Spring Validation 和 Bean Validator 还有 Hibernate Validator 进行适配

- 核心组件-org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
- 依赖 Bean Validation -JSR-330 or JSR-349
- Bean方法参数校验 - org.springframework.validation.beanvalidation.MethodValidationPostProcessor ,基于AOP ， 去校验方法参数中哪些数据或者哪些参数需要北校验

## 实例代码

#### 第一步,添加 LocalValidatorFactoryBean 的实例

注册到MethodValidationPostProcessor,打开注解驱动

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans
        xmlns="http://www.springframework.org/schema/beans"
        xmlns:context="http://www.springframework.org/schema/context"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd">

    <context:annotation-config/>

    <context:component-scan base-package="org.geekbang.thinking.in.spring.validation"/>

    <bean id="validator" class="org.springframework.validation.beanvalidation.LocalValidatorFactoryBean">
    </bean>

  <bean class="org.springframework.validation.beanvalidation.MethodValidationPostProcessor">
        <property name="validator" ref="validator"/>
    </bean>

</beans>
```

### 第二步,书写校验器

```java
@Component
@Validated
class UserProcessor {

  public void process(@Valid User user) {
    System.out.println(user);
  }
}

class User {

  @NotNull
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "User{" +
      "name='" + name + '\'' +
      '}';
  }
}
```

### 第三步,自动AOP校验

```java
/**
 * Spring Bean Validation 整合示例
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @see Validator
 * @see LocalValidatorFactoryBean
 * @since
 */
public class SpringBeanValidationDemo {

    public static void main(String[] args) {
        // 配置 XML 配置文件
        // 启动 Spring 应用上下文
        ConfigurableApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:/META-INF/bean-validation-context.xml");

//        Validator validator = applicationContext.getBean(Validator.class);
//        System.out.println(validator instanceof LocalValidatorFactoryBean);

        UserProcessor userProcessor = applicationContext.getBean(UserProcessor.class);
        userProcessor.process(new User());

        // 关闭应用上下文
        applicationContext.close();
    }


}
```

我们可以通过setValidationMessageSource方法设置消息源

## 源码

- 入口:注册所有PostProcessor

```java
org.springframework.context.support.AbstractApplicationContext#refresh

	@Override
	public void refresh() throws BeansException, IllegalStateException {
		synchronized (this.startupShutdownMonitor) {
				//....
				
				// Register bean processors that intercept bean creation.
				registerBeanPostProcessors(beanFactory);
				
				....
			
	}
```

初始化我们注册的 MethodValidationPostProcessor

```java
@SuppressWarnings("serial")
public class MethodValidationPostProcessor extends AbstractBeanFactoryAwareAdvisingPostProcessor
		implements InitializingBean {

	private Class<? extends Annotation> validatedAnnotationType = Validated.class;

	@Nullable
	private Validator validator;
	
  //---- 关注点, 拦截的AOP 是Validated.class标注的Bean
	@Override
	public void afterPropertiesSet() {
		Pointcut pointcut = new AnnotationMatchingPointcut(this.validatedAnnotationType, true);
		this.advisor = new DefaultPointcutAdvisor(pointcut, createMethodValidationAdvice(this.validator));
	}

	/**
	 * Create AOP advice for method validation purposes, to be applied
	 * with a pointcut for the specified 'validated' annotation.
	 * @param validator the JSR-303 Validator to delegate to
	 * @return the interceptor to use (typically, but not necessarily,
	 * a {@link MethodValidationInterceptor} or subclass thereof)
	 * @since 4.2
	 */
	protected Advice createMethodValidationAdvice(@Nullable Validator validator) {
		return (validator != null ? new MethodValidationInterceptor(validator) : new MethodValidationInterceptor());
	}

}


```

具体切面逻辑

```java
public class MethodValidationInterceptor implements MethodInterceptor {

	private final Validator validator;
	@Override
	@SuppressWarnings("unchecked")
	public Object invoke(MethodInvocation invocation) throws Throwable {
		// Avoid Validator invocation on FactoryBean.getObjectType/isSingleton
		if (isFactoryBeanMetadataMethod(invocation.getMethod())) {
			return invocation.proceed();
		}

		Class<?>[] groups = determineValidationGroups(invocation);

		// Standard Bean Validation 1.1 API
		ExecutableValidator execVal = this.validator.forExecutables();
		Method methodToValidate = invocation.getMethod();
		Set<ConstraintViolation<Object>> result;

		try {
      //校验参数
			result = execVal.validateParameters(
					invocation.getThis(), methodToValidate, invocation.getArguments(), groups);
		}
		catch (IllegalArgumentException ex) {
			// Probably a generic type mismatch between interface and impl as reported in SPR-12237 / HV-1011
			// Let's try to find the bridged method on the implementation class...
			methodToValidate = BridgeMethodResolver.findBridgedMethod(
					ClassUtils.getMostSpecificMethod(invocation.getMethod(), invocation.getThis().getClass()));
			result = execVal.validateParameters(
					invocation.getThis(), methodToValidate, invocation.getArguments(), groups);
		}
		if (!result.isEmpty()) {
			throw new ConstraintViolationException(result);
		}

		Object returnValue = invocation.proceed();
		//校验返回值
		result = execVal.validateReturnValue(invocation.getThis(), methodToValidate, returnValue, groups);
		if (!result.isEmpty()) {
			throw new ConstraintViolationException(result);
		}

		return returnValue;
	}
}

```

