# 040-SpringBeanDefinition合并阶段

[TOC]

## 先入为主

- 父子BeanDefinition合并
  - 当前BeanFactory查找
  - 层次性BeanFactory查找

SpringBean在实例化之前的时候,会进行合并

## 继承结构

![image-20201116213208976](../../assets/image-20201116213208976.png)

## 实例

有一个父类superUser

```xml
<!-- 普通 beanDefinition GenericBeanDefinition -->
<!-- 合并后 GenericBeanDefinition 变成 RootBeanDefinition，并且覆盖 parent 相关配置-->
<!-- primary = true , 增加了一个 address 属性 -->
<bean id="superUser" class="org.geekbang.thinking.in.spring.ioc.overview.domain.SuperUser" parent="user"
      primary="true">
  <property name="address" value="杭州"/>
</bean>
```

它有一个子类user,在进行BeanDefinition的解析是,很显然子类User必须要继承父类的属性

```xml
<!-- Root BeanDefinition 不需要合并，不存在 parent -->
<!-- 普通 beanDefinition GenericBeanDefinition -->
<!-- 经过合并后 GenericBeanDefinition 变成 RootBeanDefinition -->
<bean id="user" class="org.geekbang.thinking.in.spring.ioc.overview.domain.User">
  <property name="id" value="1"/>
  <property name="name" value="小马哥"/>
  <property name="city" value="HANGZHOU"/>
  <property name="workCities" value="BEIJING,HANGZHOU"/>
  <property name="lifeCities">
    <list>
      <value>BEIJING</value>
      <value>SHANGHAI</value>
    </list>
  </property>
  <property name="configFileLocation" value="classpath:/META-INF/user-config.properties"/>
</bean>

```

## 入口

- BeanDefinition#getParentName()

  > 可以获取一个BeanDefiniton的父

## 核心解析思想

- 如果一个BeanDefinition没有parent ,那么它就是一个 RootBeanDefinition, 它不需要合并
- 如果一个BeanDefinitiion包含一个parent属性,表达它就是 GenericBeanDefinition, 经过合并后,会变成 RootBeanDefinition

## 源代码

```java
public class MergedBeanDefinitionDemo {

    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 基于 XML 资源 BeanDefinitionReader 实现
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        String location = "META-INF/dependency-lookup-context.xml";
        // 基于 ClassPath 加载 XML 资源
        Resource resource = new ClassPathResource(location);
        // 指定字符编码 UTF-8
        EncodedResource encodedResource = new EncodedResource(resource, "UTF-8");
        int beanNumbers = beanDefinitionReader.loadBeanDefinitions(encodedResource);
        System.out.println("已加载 BeanDefinition 数量：" + beanNumbers);
        // 通过 Bean Id 和类型进行依赖查找
        User user = beanFactory.getBean("user", User.class);
        System.out.println(user);

        User superUser = beanFactory.getBean("superUser", User.class);
        System.out.println(superUser);
    }
}

```

## ConfigurableBeanFactory提供了合并功能

ConfigurableBeanFactory#getMergedBeanDefinition

```java
	/**
	 * Return a merged BeanDefinition for the given bean name,
	 * merging a child bean definition with its parent if necessary.
	 * Considers bean definitions in ancestor factories as well.
	 * @param beanName the name of the bean to retrieve the merged definition for
	 * @return a (potentially merged) BeanDefinition for the given bean
	 * @throws NoSuchBeanDefinitionException if there is no bean definition with the given name
	 * @since 2.5
	 */
	BeanDefinition getMergedBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;
```

<img src="../../assets/image-20200919224648982.png" alt="image-20200919224648982" style="zoom:80%;" />

其具体的实现是在 : 实现类AbstractBeanFactory#getMergedBeanDefinition

```java
@Override
public BeanDefinition getMergedBeanDefinition(String name) throws BeansException {
  String beanName = transformedBeanName(name);
  // Efficiently check whether bean definition exists in this factory.
  //递归往父Definition里获取
  if (!containsBeanDefinition(beanName) && getParentBeanFactory() instanceof ConfigurableBeanFactory) {
    return ((ConfigurableBeanFactory) getParentBeanFactory()).getMergedBeanDefinition(beanName);
  }
  // Resolve merged bean definition locally.
  //如果有的话,从 [当前Beanfactory] 的中获取BeanDefinition
  return getMergedLocalBeanDefinition(beanName);
}
```

容器中存储

```java
/** Map from bean name to merged RootBeanDefinition */
// 仅仅关注当前Beanfactory,相当于本地缓存
private final Map<String, RootBeanDefinition> mergedBeanDefinitions = new ConcurrentHashMap<>(256);
```

```java
protected RootBeanDefinition getMergedLocalBeanDefinition(String beanName) throws BeansException {
  // Quick check on the concurrent map first, with minimal locking.
  //从缓存中进行第一次找
  RootBeanDefinition mbd = this.mergedBeanDefinitions.get(beanName);
  if (mbd != null && !mbd.stale) {
    return mbd;
  }
  //缓存没有命中
  return getMergedBeanDefinition(beanName, getBeanDefinition(beanName));
}
```

由于BeanDefinition是可以嵌套的,所以提供了containingBd属性来判断

```java
protected RootBeanDefinition getMergedBeanDefinition(String beanName, BeanDefinition bd, 
  @Nullable BeanDefinition containingBd // 嵌套Bean
)throws BeanDefinitionStoreException {
  //线程不安全,加锁,ConcurrentHashMap仅仅是put和get是线程安全的
  synchronized (this.mergedBeanDefinitions) {
    RootBeanDefinition mbd = null;
    RootBeanDefinition previous = null;

    // Check with full lock now in order to enforce the same merged instance.
    //第2 次找为什么要再找一次呢?其他线程有可能会修改
    if (containingBd == null) {
      mbd = this.mergedBeanDefinitions.get(beanName);
    }
    if (mbd == null || mbd.stale) {
      //bean在加锁前是否已经被修改
      previous = mbd;
      //如果没有parent
      if (bd.getParentName() == null) {
        // Use copy of given root bean definition.
        //克隆一份
        if (bd instanceof RootBeanDefinition) {
          mbd = ((RootBeanDefinition) bd).cloneBeanDefinition();
        }
        else {
          mbd = new RootBeanDefinition(bd);
        }
      }
      //如果有parent
      else {
        // Child bean definition: needs to be merged with parent.
        BeanDefinition pbd;
        try {
          //规范名称
          String parentBeanName = transformedBeanName(bd.getParentName());
          if (!beanName.equals(parentBeanName)) {
            //从当前的BeanFactory中查找 parentBeanName,递归设置
            pbd = getMergedBeanDefinition(parentBeanName);
          }
          else {
            BeanFactory parent = getParentBeanFactory();
            if (parent instanceof ConfigurableBeanFactory) {
              pbd = ((ConfigurableBeanFactory) parent).getMergedBeanDefinition(parentBeanName);
            }
            else {
              throw new NoSuchBeanDefinitionException(parentBeanName,
"Parent name '" + parentBeanName + "' is equal to bean name '" + beanName +"': cannot be resolved without an AbstractBeanFactory parent");
            }
          }
        }
        catch (NoSuchBeanDefinitionException ex) {
          throw new BeanDefinitionStoreException(bd.getResourceDescription(), beanName,
                                                 "Could not resolve parent bean definition '" + bd.getParentName() + "'", ex);
        }
        // Deep copy with overridden values.
        mbd = new RootBeanDefinition(pbd);
        mbd.overrideFrom(bd);
      }

      // Set default singleton scope, if not configured before.
      if (!StringUtils.hasLength(mbd.getScope())) {
        mbd.setScope(SCOPE_SINGLETON);
      }

      // A bean contained in a non-singleton bean cannot be a singleton itself.
      // Let's correct this on the fly here, since this might be the result of
      // parent-child merging for the outer bean, in which case the original inner bean
      // definition will not have inherited the merged outer bean's singleton status.
      if (containingBd != null && !containingBd.isSingleton() && mbd.isSingleton()) {
        mbd.setScope(containingBd.getScope());
      }

      // Cache the merged bean definition for the time being
      // (it might still get re-merged later on in order to pick up metadata changes)
      if (containingBd == null && isCacheBeanMetadata()) {
        this.mergedBeanDefinitions.put(beanName, mbd);
      }
    }
    if (previous != null) {
      copyRelevantMergedBeanDefinitionCaches(previous, mbd);
    }
    return mbd;
  }
}
```

