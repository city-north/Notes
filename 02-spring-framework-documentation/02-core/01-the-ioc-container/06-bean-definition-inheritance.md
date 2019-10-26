# Bean 定义继承

- 简介

- Bean 定义的继承的优点

## 简介

一个 bean 的定义可以包含许多配置文件,类似于 **构造器参数**,**属性参数**,和 **容器相关信息**,例如 **初始化方法**, **静态工厂方法**等等

**一个子定义可以继承父 bean 的相关信息,子 bean 可以重写相关的值的信息**



### 例子

如果你需要在编程时使用`ApplicationContext`接口,`bean`的声明被抽象为一个`ChildBeanDefinition`类,大多数开发者不需要这么底层的定制,通常,当你使用的是 xml 的配置方式,只需要声明`ClassPathXmlApplicationContext`类

简单使用

```xml
<bean id="inheritedTestBean" abstract="true"
        class="org.springframework.beans.TestBean">
    <property name="name" value="parent"/>
    <property name="age" value="1"/>
</bean>

<bean id="inheritsWithDifferentClass"
        class="org.springframework.beans.DerivedTestBean"
        parent="inheritedTestBean" init-method="initialize">  
    <property name="name" value="override"/>
    <!-- the age property value of 1 will be inherited from parent -->
</bean>
```

> 请注意 `parent` 属性

值得注意的是:

- 如果没有覆盖,子类 bean 中的定义默认使用父类 bean 中的定义
- 子类 bean 必须与父类兼容(也就是说,它必须接受父类的属性值)

## 子类 bean 可以继承的属性

子类 bean 可以继承的属性如下:

- scope
- 构造器参数值
- 属性值
- 方法重写
- 具有添加新 value 的选项

指定的任何范围、初始化方法、销毁方法或静态工厂方法设置将覆盖相应的父设置。

其余的设置通常取子 bean 中的定义:

- 依赖
- 自动装配方式
- 依赖检查
- 单例
- 懒加载设置

## 抽象父类

下面的例子,使用`abstract`属性将父 bean 声明成了一个抽象类.如果父定义不指定一个类,那么必须要声明 abstract 属性,下面是例子:

```xml
<bean id="inheritedTestBeanWithoutClass" abstract="true">
    <property name="name" value="parent"/>
    <property name="age" value="1"/>
</bean>

<bean id="inheritsWithClass" class="org.springframework.beans.DerivedTestBean"
        parent="inheritedTestBeanWithoutClass" init-method="initialize">
    <property name="name" value="override"/>
    <!-- age will inherit the value of 1 from the parent bean definition-->
</bean>
```

父类无法被实例化因为它是抽象的,并不完整,当你使用了`abstract`属性后,如果你在别的 bean 定义中使用了 `ref`属性去引用它就会报错



## Bean 定义的继承的优点

- 简化书写
- 与抽象类一样,可以将其子 bean 同样的相关属性或者方法进行抽出