# 依赖查找实战

- [实时查找](#实时查找)
- [根据类型查找](#根据类型查找)
- [延迟加载](#延迟加载)
- [根据注解查找集合](#根据注解查找集合)

## 实时查找

```java
/**
 * 实时查找方式
 */
public static void lookupInRealTime(BeanFactory beanFactory) {
    final User user = (User) beanFactory.getBean("user");
    log.error("实时查找: {}", user);
}
```
## 根据类型查找

```java
/**
 * 根据类型查询
 */
private static void lookupByType(BeanFactory beanFactory) {
  final User bean = beanFactory.getBean(User.class);
  log.error("根据类型查询 :" + bean);
}
```

## 延迟加载

使用ObjectFactory 在调用getObject的时候会去初始化bean

```xml
<bean id="objectFactory" class="org.springframework.beans.factory.config.ObjectFactoryCreatingFactoryBean">
  <property name="targetBeanName" value="user"/>
</bean>
```

可以看到ObjectFactory使用了ObjectFactoryCreatingFactoryBean 类,可以理解为ObjectFactory的默认实现

```java
/**
 * 懒加载
 */
private static void lookupLazy(BeanFactory beanFactory) {
  final ObjectFactory<User> objectFactory = (ObjectFactory) beanFactory.getBean("objectFactory");
  final User object = objectFactory.getObject();
  log.error("延迟加载 :{}", object);
}
```

## 根据注解查找集合

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Super {
}
```

```java
/**
 * 根据注解查找Bean
 * @param beanFactory
 */
private static void lookupCollectionByAnnotation(ClassPathXmlApplicationContext beanFactory) {
  final Map<String, Object> beansWithAnnotation = beanFactory.getBeansWithAnnotation(Super.class);
  log.error("根据注解获取bean: {}", beansWithAnnotation);
}
```

## 根据类型查找集合

```java
/**
 * 根据类型查找集合
 */
private static void lookupCollectionByType(ClassPathXmlApplicationContext beanFactory) {
    final Map<String, User> beansOfType = beanFactory.getBeansOfType(User.class);
    log.error("collection By Type {}", beansOfType);
}
```
