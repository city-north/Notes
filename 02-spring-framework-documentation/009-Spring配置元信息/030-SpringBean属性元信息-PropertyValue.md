# 030-SpringBean属性元信息

## 目录

------

[TOC]

## 一言蔽之

PropertyValue 是对属性元信息的封装,它的关注点是属性名和属性的值

- 在BeanDefinition中包含一些列PropertyValue 的合集 PropertyValues
- 经过属性绑定后,会将PropertyValue 中的属性值进行设置

## SpringBean属性元信息的分类

- Bean属性抽象-PropertyValues
  - 可修改实现-MutablePropertyValues
  - 元素成员-PropertyValue
- [Bean属性上下文存储-AttributeAccessor](#Bean属性上下文存储-AttributeAccessor)
- [Bean元信息元素-BeanMetadataElement](#Bean元信息元素-BeanMetadataElement)

### Bean属性抽象-PropertyValues

PropertyValues 代表Bean的属性集合, 其中集成多个PropertyValues

#### 元素成员-PropertyValue接口

PropertyValue代表了配置的bean的属性,我们可以使用以下方式设置

```java
DefaultListableBeanFactory defaultListableBeanFactory = new DefaultListableBeanFactory();
BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(User.class);
builder.addPropertyValue("name", "EricChen");
final AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();

//-- 
defaultListableBeanFactory.registerBeanDefinition("user", beanDefinition);
final Object user = defaultListableBeanFactory.getBean("user");
//-- 
```

## 附加属性setAttribute和source的作用

```java
public static void main(String[] args) {
  // BeanDefinition 的定义（声明）
  final BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(User.class);
  beanDefinitionBuilder.addPropertyValue("name", "EricChen");
  DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
  // 获取 AbstractBeanDefinition
  final AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
  
  // 附加属性（不影响 Bean populate、initialize）,主要用作上下文传递临时变量
  beanDefinition.setAttribute("name", "chengbei");
  
  // 当前 BeanDefinition 来自于何方（辅助作用）
  beanDefinition.setSource(BeanConfigurationMetadataDemo.class);
  beanFactory.registerBeanDefinition("user", beanDefinition);
  beanFactory.addBeanPostProcessor(new BeanPostProcessor() {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
      if (ObjectUtils.nullSafeEquals("user", beanName) && User.class.equals(bean.getClass())) {
        BeanDefinition bd = beanFactory.getBeanDefinition(beanName);
        // 通过 source 判断来
        if ( BeanConfigurationMetadataDemo.class.equals(bd.getSource())){
          // 属性（存储）上下文
          final String name = (String) bd.getAttribute("name");
          User user = (User) bean;
          user.setName(name);
          return bean;
        }
      }
      return bean;
    }
  });
  // 注册 User 的 BeanDefinition
  final User user = beanFactory.getBean("user", User.class);
  System.out.println("User = " + user);
}
```

#### 可修改实现-MutablePropertyValues

MutablePropertyValues是PropertyValues的实现类

```java
MutablePropertyValues pvs = beanDefinition.getPropertyValues();
```

## Propertyvalues的特征

| 特征         | 说明                                                         |
| ------------ | ------------------------------------------------------------ |
| 数据来源     | BeanDefinition, 主要来源XML资源配置BeanDefinition            |
| 数据结构     | 由一个或者多个PropertyValue组成                              |
| 成员结构     | PropertyValue包含属性名称,以及属性值(包括原始值和类型转换后的值) |
| 常见实现     | MutablePropertyValues                                        |
| Web拓展实现  | ServletConfigPropertyValues<br />SerletRequestParameterPropertyValues<br /> |
| 相关声明周期 | InstantiationAwareBeanPostProcessor#postProcessProperties<br />[080-SpringBean实例化后阶段.md](../008-SpringBean生命周期/080-SpringBean实例化后阶段.md) |

## Propertyvalue源码

Propertyvalue 是一个数据结构,包含了key-value形式,包含了属性名以及对应的值

```java
public class PropertyValue extends BeanMetadataAttributeAccessor implements Serializable {

	private final String name;

  //原始值
	@Nullable
	private final Object value;

  //是否是可选的
	private boolean optional = false;

  //是否已经转换
	private boolean converted = false;

  //转换后的值
	@Nullable
	private Object convertedValue;

	/**是否有必要进行转换 */
	@Nullable
	volatile Boolean conversionNecessary;

	/**  */
	@Nullable
	transient volatile Object resolvedTokens;

}
```

## Bean属性上下文存储-AttributeAccessor

属性存储器,

接口设置的非常简单,就是对附加属性的增删改查

 附加属性（不影响 Bean populate、initialize）,主要用作上下文传递临时变量

![image-20201201205432454](../../assets/image-20201201205432454.png)

```java
public interface AttributeAccessor {
	void setAttribute(String name, @Nullable Object value);
	@Nullable
	Object getAttribute(String name);
	@Nullable
	Object removeAttribute(String name);
	boolean hasAttribute(String name);
	String[] attributeNames();
}
```

### Bean元信息元素-BeanMetadataElement

![image-20201201205432454](../../assets/image-20201201205432454.png)

```java
public interface BeanMetadataElement {

   /**
    * Return the configuration source {@code Object} for this metadata element
    * (may be {@code null}).
    */
   @Nullable
   Object getSource();

}
```

Source主要是用来标记当前 BeanDefinition 来自于何方（辅助作用）

```java
public static void main(String[] args) {
  // BeanDefinition 的定义（声明）
  final BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(User.class);
  beanDefinitionBuilder.addPropertyValue("name", "EricChen");
  DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
  // 获取 AbstractBeanDefinition
  final AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
  // 附加属性（不影响 Bean populate、initialize）,主要用作上下文传递临时变量
  beanDefinition.setAttribute("name", "chengbei");
  // 当前 BeanDefinition 来自于何方（辅助作用）
  
  //----------↓↓↓↓↓↓-----本段关注的重点---------↓↓↓↓↓↓↓------------//
  beanDefinition.setSource(BeanConfigurationMetadataDemo.class);
  //----------↑↑↑↑↑↑-----本段关注的重点---------↑↑↑↑↑↑------------//
  
  beanFactory.registerBeanDefinition("user", beanDefinition);
  beanFactory.addBeanPostProcessor(new BeanPostProcessor() {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
      if (ObjectUtils.nullSafeEquals("user", beanName) && User.class.equals(bean.getClass())) {
        BeanDefinition bd = beanFactory.getBeanDefinition(beanName);
        // 通过 source 判断来
          //----------↓↓↓↓↓↓-----本段关注的重点---------↓↓↓↓↓↓↓------------//
        if ( BeanConfigurationMetadataDemo.class.equals(bd.getSource())){
            //----------↑↑↑↑↑↑-----本段关注的重点---------↑↑↑↑↑↑------------//
          // 属性（存储）上下文
          final String name = (String) bd.getAttribute("name");
          User user = (User) bean;
          user.setName(name);
          return bean;
        }
      }
      return bean;
    }
  });
  // 注册 User 的 BeanDefinition
  final User user = beanFactory.getBean("user", User.class);
  System.out.println("User = " + user);
}
```

## 演示代码

```java
public static void main(String[] args) {
  // BeanDefinition 的定义（声明）
  final BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(User.class);
  beanDefinitionBuilder.addPropertyValue("name", "EricChen");
  DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
  // 获取 AbstractBeanDefinition
  final AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
  // 附加属性（不影响 Bean populate、initialize）,主要用作上下文传递临时变量
  
    //----------↓↓↓↓↓↓-----本段关注的重点---------↓↓↓↓↓↓↓------------//
	  beanDefinition.setAttribute("name", "chengbei");
    //----------↑↑↑↑↑↑-----本段关注的重点---------↑↑↑↑↑↑------------//
  // 当前 BeanDefinition 来自于何方（辅助作用）
 
  beanDefinition.setSource(BeanConfigurationMetadataDemo.class);
  
  beanFactory.registerBeanDefinition("user", beanDefinition);
  beanFactory.addBeanPostProcessor(new BeanPostProcessor() {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
      if (ObjectUtils.nullSafeEquals("user", beanName) && User.class.equals(bean.getClass())) {
        BeanDefinition bd = beanFactory.getBeanDefinition(beanName);
        // 通过 source 判断来
        if ( BeanConfigurationMetadataDemo.class.equals(bd.getSource())){
          // 属性（存储）上下文
         //----------↓↓↓↓↓↓-----本段关注的重点---------↓↓↓↓↓↓↓------------//
          final String name = (String) bd.getAttribute("name");
         //----------↑↑↑↑↑↑-----本段关注的重点---------↑↑↑↑↑↑------------//
          User user = (User) bean;
          user.setName(name);
          return bean;
        }
      }
      return bean;
    }
  });
  // 注册 User 的 BeanDefinition
  final User user = beanFactory.getBean("user", User.class);
  System.out.println("User = " + user);
}
```

