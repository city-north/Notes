# BeanDefinition定义SpringBean

---

[TOC]

## 什么是BeanDefinition

BeanDefinition是Spring对Bean定义的逻辑抽象,子接口或者实现:

- GenericBeanDefinition:通用型BeanDefinition
- RootBeanDefinition:无Parent的BeanDefinition或者合并后的BeanDefinition
- AnnotatedBeanDefinition:注解标注的BeanDefinition

## 接口源码

#### 静态属性

```java
public interface BeanDefinition extends AttributeAccessor, BeanMetadataElement {
  	// 单例
    String SCOPE_SINGLETON = ConfigurableBeanFactory.SCOPE_SINGLETON;
    // 原型
  	String SCOPE_PROTOTYPE = ConfigurableBeanFactory.SCOPE_PROTOTYPE;
    
  	int ROLE_APPLICATION = 0;
    int ROLE_SUPPORT = 1;
    int ROLE_INFRASTRUCTURE = 2;
}
```

#### 设置双亲

```java
//设置父定义名称
void setParentName(@Nullable String parentName);

@Nullable
String getParentName();
```

#### 设置Class属性

```java
void setBeanClassName(@Nullable String beanClassName);

@Nullable
String getBeanClassName();
```

#### 设置构造器参数

```java
ConstructorArgumentValues getConstructorArgumentValues();

default boolean hasConstructorArgumentValues() {
  return !getConstructorArgumentValues().isEmpty();
}
```

#### 获取属性参数

```java
MutablePropertyValues getPropertyValues();

default boolean hasPropertyValues() {
  return !getPropertyValues().isEmpty();
}
```

#### 设置Scope/单例/原型/抽象

```java
void setScope(@Nullable String scope);
@Nullable

String getScope();

boolean isSingleton();
/**
	 * @since 3.0
	 */
boolean isPrototype();

boolean isAbstract();
```

#### 获取描述

```java
@Nullable
String getDescription();

@Nullable
String getResourceDescription();
```

#### 获取角色

```java
int getRole();

@Nullable
BeanDefinition getOriginatingBeanDefinition();
```

#### 设置懒加载

```java
void setLazyInit(boolean lazyInit);

boolean isLazyInit();
```

#### 设置依赖Bean

```java
void setDependsOn(@Nullable String... dependsOn);

@Nullable
String[] getDependsOn();
```

#### 是否自动注入

```java
void setAutowireCandidate(boolean autowireCandidate);

boolean isAutowireCandidate();
```

#### 设置是否是主Bean

```java
void setPrimary(boolean primary);

boolean isPrimary();
```

#### 设置FactoryBean

```java
void setFactoryBeanName(@Nullable String factoryBeanName);
@Nullable
String getFactoryBeanName();

setFactoryMethodName(@Nullable String factoryMethodName);
@Nullable
String getFactoryMethodName();
```







![image-20201116213208976](../../assets/image-20201116213208976.png)

BeanDefinition 是Spring Framework 中定义Bean的配置元信息接口,包括:

- Bean的类名
- Bean行为配置元素, 如作用域, 自动绑定的模式, 生命周期的回调等
- 其他Bean的引用, 又可以称为(Collaborators)或者依赖 (Dependencies)
- 配置设置 , 比如Bean属性(Properties)

## BeanDefinition元信息

| 属性                    | 说明                                           |
| ----------------------- | ---------------------------------------------- |
| Class                   | Bean全类名,必须是具体的类,不能用抽象类或者接口 |
| Name                    | Bean的名称或者ID                               |
| Scope                   | Bean的作用域                                   |
| Constructor aguments    | Bean构造器参数(用于依赖注入)                   |
| Properties              | Bean属性设置(用于依赖注入)                     |
| Autowiring mode         | Bean 自动绑定模式 , 如 通过名称 byName         |
| Lazy initalization mode | Bean 延迟初始化模式(延迟和非延迟)              |
| Initialization method   | Bean 初始化回调方法名称                        |
| Destruction method      | Bean 销毁回调方法名称                          |

## 使用BeanDefinitionBuilder来定义一个BeanDefinition

```java
public class BeanDefinitionBuilderExample {

    public static void main(String[] args) {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
                .rootBeanDefinition(MyBean.class)
                .addPropertyValue("str", "hello")
                .setScope(ConfigurableBeanFactory.SCOPE_PROTOTYPE);
        factory.registerBeanDefinition("myBean", beanDefinitionBuilder.getBeanDefinition());

        MyBean bean = factory.getBean(MyBean.class);
        bean.doSomething();
    }

    public static class MyBean {
        private String str;

        public void setStr(String str) {
            this.str = str;
        }

        public void doSomething() {
            System.out.println("from MyBean " + str);
        }
    }
}
```

## SpringCloud中OpenFeign的形式

org.springframework.cloud.openfeign.FeignClientsRegistrar#registerFeignClient

```java
	private void registerFeignClient(BeanDefinitionRegistry registry,
			AnnotationMetadata annotationMetadata, Map<String, Object> attributes) {
		String className = annotationMetadata.getClassName();
		BeanDefinitionBuilder definition = BeanDefinitionBuilder
				.genericBeanDefinition(FeignClientFactoryBean.class);
		validate(attributes);
		definition.addPropertyValue("url", getUrl(attributes));
		definition.addPropertyValue("path", getPath(attributes));
		String name = getName(attributes);
		definition.addPropertyValue("name", name);
		definition.addPropertyValue("type", className);
		definition.addPropertyValue("decode404", attributes.get("decode404"));
		definition.addPropertyValue("fallback", attributes.get("fallback"));
		definition.addPropertyValue("fallbackFactory", attributes.get("fallbackFactory"));
		definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

		String alias = name + "FeignClient";
		AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();

		boolean primary = (Boolean)attributes.get("primary"); // has a default, won't be null

		beanDefinition.setPrimary(primary);

		String qualifier = getQualifier(attributes);
		if (StringUtils.hasText(qualifier)) {
			alias = qualifier;
		}

		BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, className,
				new String[] { alias });
		BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
	}
```

