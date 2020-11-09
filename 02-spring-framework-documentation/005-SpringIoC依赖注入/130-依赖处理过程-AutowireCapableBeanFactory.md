# 依赖处理过程

#### 先入为主的核心类

- 入口 ： DefaultListableBeanFactory#resolveDependency
- 依赖描述符 : DependencyDescriptor
  - 自定义绑定候选对象处理器 ：AutowireCandidateReslover	

## AutowireCapableBeanFactory

自动注入功能的实现是由AutowireCapableBeanFactory这个接口来实现的

<img src="../../assets/image-20200919224648982.png" alt="image-20200919224648982" style="zoom:80%;" />

从图中可以看出，它直接拓展了BeanFactory， 其核心子类是ConfigurableListableBeanfactory, 主要实现了

- 创建bean
- 自动注入
- 初始化以及应用bean的后处理器

## 接口内的方法

```java
//根据类型创建Bean
AutowireCapableBeanFactory#createBean(java.lang.Class<T>)throws BeansException;
//创建Bean
AutowireCapableBeanFactory#createBean(java.lang.Class<?>, int, boolean) throws BeansException;
//依赖注入bean
AutowireCapableBeanFactory#autowireBean throws BeansException;
//配置bean
AutowireCapableBeanFactory#configureBean throws BeansException;
//依赖注入
AutowireCapableBeanFactory#autowire(Class<?> beanClass, int autowireMode, boolean dependencyCheck);
AutowireCapableBeanFactory#autowireBeanProperties(Object existingBean, int autowireMode, boolean dependencyCheck)
AutowireCapableBeanFactory#applyBeanPropertyValues
AutowireCapableBeanFactory#initializeBean
AutowireCapableBeanFactory#applyBeanPostProcessorsBeforeInitialization
AutowireCapableBeanFactory#applyBeanPostProcessorsAfterInitialization
AutowireCapableBeanFactory#destroyBean
AutowireCapableBeanFactory#resolveNamedBean
//@since 2.5   DependencyDescriptor-> 依赖描述符 ;requestBeanName->当前需要注入的Bean名称
AutowireCapableBeanFactory#resolveDependency(DependencyDescriptor,String requestBeanName)
//@since 2.5 解析依赖
AutowireCapableBeanFactory#resolveDependency(DependencyDescriptor, java.lang.String, java.util.Set<java.lang.String>, TypeConverter)
AutowireCapableBeanFactory#AUTOWIRE_NO
AutowireCapableBeanFactory#AUTOWIRE_BY_NAME
AutowireCapableBeanFactory#AUTOWIRE_BY_TYPE
AutowireCapableBeanFactory#AUTOWIRE_CONSTRUCTOR
AutowireCapableBeanFactory#AUTOWIRE_AUTODETECT
```

## AutowireCapableBeanFactory-resolveDependency

针对此工厂中定义的bean解析指定的依赖项

```java
@Nullable
Object resolveDependency(DependencyDescriptor descriptor, @Nullable String requestingBeanName) throws BeansException;
```

解析多个指定的依赖项

```java
@Nullable
AutowireCapableBeanFactory#resolveDependency(DependencyDescriptor descriptor, String requestingBeanName, Set<java.lang.String> autowiredBeanNames, TypeConverter)
```

## DependencyDescriptor

形参第一个是一个依赖描述器, 其核心字段如下

```java
public class DependencyDescriptor extends InjectionPoint implements Serializable {
	//当前声明的注入描述符,Class类型,容器类
	private final Class<?> declaringClass;
	//方法名称	
	@Nullable
	private String methodName;
	//参数类型
	@Nullable
	private Class<?>[] parameterTypes;
	//参数索引
	private int parameterIndex;
	//属性名称
	@Nullable
	private String fieldName;
	//是否必须  @Autowired#required
	private final boolean required;
	//是否饥饿加载 @Lazy
	private final boolean eager;
	//嵌套层数
	private int nestingLevel = 1;
	//是否包含Class文件
	@Nullable
	private Class<?> containingClass;
	//解析类型
	@Nullable
	private transient volatile ResolvableType resolvableType;
	//类型描述符
	@Nullable
	private transient volatile TypeDescriptor typeDescriptor;

```

