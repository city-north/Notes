#  FactoryBean和BeanFactory的区别

## 目录

- [BeanFactory是什么](#BeanFactory是什么)

- [为什么要有FactoryBean接口](#为什么要有FactoryBean接口)
- [FactoryBean实例](#FactoryBean实例)

## 为什么要有FactoryBean接口

一般情况下，Spring通过反射机制利用bean的class属性指定实现类来实例化bean 。

在某些情况下，实例化bean过程比较复杂，如果按照传统的方式，则需要在<bean>中提供大量的配置信息，配置方式的灵活性是受限的，这时采用编码的方式可能会得到一个简单的方案。

Spring为此提供了一个org.Springframework.bean.factory.FactoryBean的工厂类接口，用户可以通过实现该接口定制实例化bean的逻辑。

FactoryBean接口对于Spring框架来说占有重要的地位，Spring 自身就提供了70多个FactoryBean的实现。它们隐藏了实例化一些复杂bean的细节，给上层应用带来了便利。

从Spring 3.0 开始， FactoryBean开始支持泛型，即接口声明改为`FactoryBean<T> `的形式：

```java
package org.Springframework.beans.factory;  
public interface FactoryBean<T> {
  //获取工厂类生产的对象
   T getObject() throws Exception;
  //获取对象的作用于是singleton还是prototype
   Class<?> getObjectType();
  //返回FactoryBean创建的bean类型
   boolean isSingleton();  
}
```

#### 知名例子

```java
public class SqlSessionFactoryBean implements FactoryBean<SqlSessionFactory>{
}
```

## FactoryBean实例

当配置文件中`<bean>`的class属性配置的实现类是FactoryBean时，通过 getBean()方法返回的不是FactoryBean本身，而是FactoryBean#getObject()方法所返回的对象，相当于FactoryBean#getObject()代理了getBean()方法。

例如：如果使用传统方式配置下面Car的`<bean>`时，Car的每个属性分别对应一个`<property>`元素标签。

```java
 public class Car {  
        private   int maxSpeed ;  
        private  String brand ;  
        private   double price ;  
       //get/set方法
}
```

如果用FactoryBean的方式实现就会灵活一些，下例通过逗号分割符的方式一次性地为Car的所有属性指定配置值：

```java
public   class  CarFactoryBean  implements  FactoryBean<Car>  {  
    private  String carInfo ;  
    public  Car getObject ()   throws  Exception  {  
        Car car =  new  Car () ;  
        String []  infos =  carInfo .split ( "," ) ;  
        car.setBrand ( infos [ 0 ]) ;  
        car.setMaxSpeed ( Integer. valueOf ( infos [ 1 ])) ;  
        car.setPrice ( Double. valueOf ( infos [ 2 ])) ;  
        return  car;  
    }  
    public  Class<Car> getObjectType ()   {
      return  Car. class ;  
    }  
    public   boolean  isSingleton ()   {  
        return   false ;  
    }  
    public  String getCarInfo ()   {  
        return   this . carInfo ;  
    }  

    // 接受逗号分割符设置属性信息  
    public   void  setCarInfo ( String carInfo )   {  
        this . carInfo  = carInfo;  
    }  
}
```

有了这个CarFactoryBean后，就可以在配置文件中使用下面这种自定义的配置方式配置Car Bean了：

```
<bean id="car" class="com.test.factorybean.CarFactoryBean" carInfo="超级跑车,400,2000000"/>
```

- 当调用getBean("car") 时，
- Spring通过反射机制发现CarFactoryBean实现了FactoryBean的接口，这时Spring容器就调用接口方法CarFactoryBean#getObject()方法返回。

**如果希望获取CarFactoryBean的实例，则需要在使用getBean(beanName) 方法时在beanName前显示的加上 "&" 前缀，例如getBean("&car")。**

## BeanFactory是什么

Bean工厂，是一个工厂（Factory）接口，Spring IoC容器的最高层接口就是 BeanFactory，它的作用是管理Bean，即实例化、定位、配置应用程序中的对象及建立这些对象之间的依赖。