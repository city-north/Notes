# 080-SpringBean实例化后阶段

## 一言蔽之

实例化后置处理器的执行方法为

```
boolean InstantiationAwareBeanPostProcessor#postProcessAfterInstantiation(Object bean, String beanName)
```

在实例化后,初始化bean之前进行调用,我们可以使用这个机制自定义我们对bean的赋值操作,进而自定义Bean

## 目录

- [Bean属性赋值(populate)判断](#Bean属性赋值(populate)判断)
- [典型实现](#典型实现)
- [调用流程](#调用流程)

## Bean属性赋值(populate)判断

在Bean的实例被初始化以后(可以是通过构造器或者是工厂方法), 但是在实例的属性被设置之前,执行这个方法

通常用语

- 在Spring自动装配开始之前，这是在给定bean实例上执行定制字段注入的理想回调。
- 默认的值是ture

```
//org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor
boolean InstantiationAwareBeanPostProcessor#postProcessAfterInstantiation(Object bean, String beanName)
```

- 如果返回true, 则执行Spring对Bean属性的设置
- 如果返回false,则跳过Spring对Bean属性的设置

## 典型实现

```java
class MyInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {

  @Override
  public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
    if (ObjectUtils.nullSafeEquals("user", beanName) && User.class.equals(bean.getClass())) {
      User user = (User) bean;
      user.setId(2L);
      user.setName("mercyblitz");
      // "user" 对象不允许属性赋值（填入）（配置元信息 -> 属性值）
      return false;
    }
    //使用Spring配置对Bean进行配置
    return true;
  }
}
```

## 调用流程

![image-20201125221451477](../../assets/image-20201125221451477.png)

可以从流程中看出,进行赋值操作之前,执行实例化后置处理器,这个处理器允许我们自己设置属性

![image-20201125192955451](../../assets/image-20201125192955451.png)