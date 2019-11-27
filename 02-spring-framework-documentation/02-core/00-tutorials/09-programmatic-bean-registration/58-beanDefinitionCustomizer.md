# 使用 Spring 5 BeanDefinitionCustomizer 和 GenericApplicationContext 的新放在编程时注册 bean

## BeanDefinitionCustomizer 的新方法

`GenericApplicationContext` 实现`ConfigurableApplicationContext`和`BeanDefinitionRegistry`接口

新增方法:

```java
//重写了BeanDefinitionRegistry#registerBeanDefinition
registerBeanDefinition(beanName, beanDefinition)
```

```java
//根据 class 注册一个 bean
registerBean(Class<T> beanClass, BeanDefinitionCustomizer... customizers)
```

```java
//根据 class 和 name 注册一个
registerBean(@Nullable String beanName, Class<T> beanClass, BeanDefinitionCustomizer... customizers)
```

#### 根据java8的 Supplier 返回 bean 的实例,所以在 bean 创建的过程中没有进行反射

```java
registerBean(Class<T> beanClass, Supplier<T> supplier, BeanDefinitionCustomizer... customizers)
```

```java

registerBean(@Nullable String beanName, Class<T> beanClass, @Nullable Supplier<T> supplier, BeanDefinitionCustomizer... customizers)
```

值得注意的是所有新方法的最后一个参数都是 Spring 5 的一个新接口`BeanDefinitionCustomizer`

`GenericApplicationContext`需要访问`AbstractApplicationContext.refresh()`来创建注册的 bean

## BeanDefinitionCustomizer



```java
package org.springframework.beans.factory.config;
 ....
@FunctionalInterface
public interface BeanDefinitionCustomizer {
   //Customize the given bean definition.
   void customize(BeanDefinition bd);
}
```

这个接口可以用来作为一个 自定义bean 定义回调,它是使用 Java8 lambda 表达式和方法引用来设计的,

## 例子

```java
public class Customizers {

    public static void prototypeScoped(BeanDefinition bd) {
        bd.setScope(ConfigurableBeanFactory.SCOPE_PROTOTYPE);
    }

    public static void lazy(BeanDefinition bd) {
        bd.setScope(ConfigurableBeanFactory.SCOPE_PROTOTYPE);
    }


    public static void defaultInitMethod(BeanDefinition bd) {
        bd.setInitMethodName("init");
    }

    public static void defaultDestroyMethod(BeanDefinition bd) {
        bd.setDestroyMethodName("destroy");
    }
}
```

```java
public interface LogService {
    void log(String msg);

    class LogServiceImpl implements LogService {
        public LogServiceImpl() {
            System.out.printf("instance of %s created: %s%n", this.getClass().getName(),
                    System.identityHashCode(this));
        }

        @Override
        public void log(String msg) {
            System.out.println(msg);
        }

        private void init() {
            System.out.printf("%s, init method called: ", this.getClass().getName(),
                    System.identityHashCode(this));
        }
    }
}
```

```java
public interface OrderService {
    void placeOrder(String item, int qty);

    public static class OrderServiceImpl implements OrderService {

        private LogService logService;

        public OrderServiceImpl() {
            System.out.printf("instance of %s created: %s%n", this.getClass().getName(),
                    System.identityHashCode(this));
        }

        public OrderServiceImpl(LogService logService) {
            this();
            this.logService = logService;
        }

        public void setLogService(LogService logService) {
            this.logService = logService;
        }

        @Override
        public void placeOrder(String item, int qty) {
            System.out.printf("placing order item: %s, qty: %s, isntance: %s%n",
                    item, qty, System.identityHashCode(this));
            if (logService != null) {
                logService.log("Order placed");
            }
        }

        private void init() {
            System.out.printf("%s, init method called: %s%n", this.getClass().getName(),
                    System.identityHashCode(this));
        }

        private void destroy() {
            System.out.printf("%s, destroy method called: %s%n", this.getClass().getName(),
                    System.identityHashCode(this));
        }
    }
}
```



#### Using a method of BeanDefinitionCustomizer

```java
/**
 * 使用 {@link GenericApplicationContext} 的新方法 {@link GenericApplicationContext#registerBean} 注册 bean
 *
 * @author EricChen 2019/11/27 18:06
 */
public class RegisterBeanExample1 {
    //using registerBean(beanClass, customizers)
    public static void main(String[] args) {
        GenericApplicationContext gac = new GenericApplicationContext();
        gac.registerBean(OrderService.OrderServiceImpl.class);//not using customizer
        gac.refresh();
        System.out.println("context refreshed");
        OrderService os = gac.getBean(OrderService.class);
        os.placeOrder("Laptop", 2);
        System.out.println("-----------");
        //retrieving the bean one more time
        os = gac.getBean(OrderService.class);
        os.placeOrder("Desktop", 2);
        gac.close();
    }
}

```

```
instance of OrderService$OrderServiceImpl created: 940553268
context refreshed
placing order item: Laptop, qty: 2, isntance: 940553268
-----------
placing order item: Desktop, qty: 2, isntance: 940553268
```

我们已经创建了一个 BeanDefinitionCustomizers 的工具类,这个方法可以用来使用 Java8 方法引用

```java
public class Customizers {

  public static void prototypeScoped(BeanDefinition bd) {
      bd.setScope(ConfigurableBeanFactory.SCOPE_PROTOTYPE);
  }

  public static void lazy(BeanDefinition bd) {
      bd.setScope(ConfigurableBeanFactory.SCOPE_PROTOTYPE);
  }


  public static void defaultInitMethod(BeanDefinition bd) {
      bd.setInitMethodName("init");
  }

  public static void defaultDestroyMethod(BeanDefinition bd) {
      bd.setDestroyMethodName("destroy");
  }
}
```

我们可以将 bean 注册为 pototype 类型:

```java
public class RegisterBeanExample2 {
    //using registerBean(beanClass, customizers)
    public static void main(String[] args) {
        GenericApplicationContext gac = new GenericApplicationContext();
        gac.registerBean(OrderService.OrderServiceImpl.class, Customizers::prototypeScoped);
        gac.refresh();
        System.out.println("context refreshed");
        OrderService os = gac.getBean(OrderService.class);
        os.placeOrder("Laptop", 2);
        System.out.println("-----------");
        //retrieving the bean one more time
        os = gac.getBean(OrderService.class);
        os.placeOrder("Desktop", 3);
        gac.close();
    }
}
```

```
instance of cn.eccto.study.springframework.tutorials.spring5.OrderService$OrderServiceImpl created: 195615004
placing order item: Laptop, qty: 2, isntance: 195615004
-----------
instance of cn.eccto.study.springframework.tutorials.spring5.OrderService$OrderServiceImpl created: 1860250540
placing order item: Desktop, qty: 3, isntance: 1860250540

```

根据 bean 的 name 注册 bean:

```java
public class RegisterBeanExample3 {
    //using registerBean(beanName, beanClass, customizers)
    public static void main(String[] args) {
        GenericApplicationContext gac = new GenericApplicationContext();
        gac.registerBean("orderBean", OrderService.OrderServiceImpl.class, Customizers::lazy);
        gac.refresh();
        System.out.println("context refreshed");
        OrderService os = (OrderService) gac.getBean("orderBean");
        os.placeOrder("Laptop", 2);
        gac.close();
    }
}

```

使用多个 BeanDefintionCustomizer:

```java
public class RegisterBeanExample4 {
    //using registerBean(beanClass, beanSupplier,  customizers)
    public static void main(String[] args) {
        GenericApplicationContext gac = new GenericApplicationContext();
        gac.registerBean(LogService.class, LogService.LogServiceImpl::new, Customizers::lazy, Customizers::defaultInitMethod);
        gac.refresh();
        System.out.println("context refreshed");
        LogService ls = gac.getBean(LogService.class);
        ls.log("msg from main method");
        gac.close();
    }
}
```

```java
context refreshed
instance of cn.eccto.study.springframework.tutorials.spring5.LogService$LogServiceImpl created: 1843289228
cn.eccto.study.springframework.tutorials.spring5.LogService$LogServiceImpl, init method called: msg from main method
```



根据 构造器注入其他的 bean:

```java
public class RegisterBeanExample5 {
    //injecting other bean via constructor
    public static void main(String[] args) {
        GenericApplicationContext gac = new GenericApplicationContext();
        gac.registerBean(LogService.class, LogService.LogServiceImpl::new, Customizers::lazy, Customizers::defaultInitMethod);
        gac.registerBean(OrderService.OrderServiceImpl.class, Customizers::defaultInitMethod, Customizers::defaultDestroyMethod);
        gac.refresh();
        System.out.println("context refreshed");
        OrderService os = gac.getBean(OrderService.class);
        os.placeOrder("Laptop", 2);
        gac.close();
    }
}
```

```
cn.eccto.study.springframework.tutorials.spring5.LogService$LogServiceImpl, init method called: instance of cn.eccto.study.springframework.tutorials.spring5.OrderService$OrderServiceImpl created: 2142080121
cn.eccto.study.springframework.tutorials.spring5.OrderService$OrderServiceImpl, init method called: 2142080121
context refreshed
placing order item: Laptop, qty: 2, isntance: 2142080121
Order placed
cn.eccto.study.springframework.tutorials.spring5.OrderService$OrderServiceImpl, destroy method called: 2142080121
```

