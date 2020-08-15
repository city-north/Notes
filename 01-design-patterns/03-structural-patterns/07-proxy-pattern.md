# 代理模式（Proxy Patern）

相关知识点:  [README.md](110-动态代理/README.md) 

>  给某一个对象提供一个代理或占位符，并由代理对象来控制对原对象的访问

- 代理对象在客户端与真实对象之间起到了中介的作用,客户端不直接操作真实对象
- 结构性设计模式

主要作用:

- 保护目标对象
- 增强目标对象

## 代理分类

- 静态代理 : 显式地声明被代理对象

- 动态代理

### 静态代理

分为是角色：

* 共同接口
* 真实对象
* 代理对象

##### UML



![image-20200101121152607](assets/image-20200101121152607.png)





##### 共同接口

```java
/**
 * 代理对象和真实对象都实现的共同接口
 *
 * @author EricChen 2020/01/01 11:56
 */
public interface Action {
    /**
     * 对象的行为
     */
    void doSomething();
}

```

##### 真实对象

```java
/**
 * 真实对象,也就是被代理的对象
 *
 * @author EricChen 2020/01/01 11:57
 */
public class RealObject implements Action {

    @Override
    public void doSomething() {
        System.out.println("The real object is doing");
    }
}

```

##### 代理对象

```java
/**
 * 代理对象
 *
 * @author EricChen 2020/01/01 11:58
 */
public class ProxyObject implements Action {
    private RealObject realObject;

    public ProxyObject(RealObject realObject) {
        this.realObject = realObject;
    }

    @Override
    public void doSomething() {
        enhance();
        realObject.doSomething();
    }

    private void enhance() {
        System.out.println("Proxy object is enhancing ");
    }

}

```

测试：

```java
/**
 * 静态代理测试
 *
 * @author EricChen 2020/01/01 12:01
 */
public class StaticProxyExample {

    public static void main(String[] args) {
        doWithStaticProxy();
    }


    private static void doWithStaticProxy() {
        ProxyObject proxyObject = new ProxyObject(new RealObject());
        proxyObject.doSomething();
    }
}
```

#### 静态优势与缺陷

优势：业务类只需要关注业务逻辑本身，保证了业务类的重用性，当需要拓展时，使用其代理类。

缺陷：

* 代理对象的一个接口只服务于一种类型的对象，如果要代理的方法有很多，势必要每一种方法都要进行代理，静态代理在程序规模稍大时就无法胜任了
* 如果接口增加一个方法，除了所有实现类都实现这个方法外，所有的代理类也要实现这个方法，增加了代码维护的复杂度。



## 代理模式的优缺点

### 优点

- 代理模式能将代理对象与真实被调用的目标对象分离
- 一定程度上江都了系统的耦合程度,易于拓展
- 代理可以起到保护目标对象的作用
- 增强目标对象的职责

### 缺点

- 代理模式会造成系统设计中的类的数目的增加
- 在客户端与目标对象之间增加一个代理对象,会造成请求处理速度变慢
- 增加了系统的复杂度

## 综合案例

```java
/**
 * 案例: 使用不同方式代理 {@link IOrderService} 的默认实现类,使其支持多数据源
 *
 * @author EricChen 2020/01/01 15:03
 * @see OrderServiceImpl 默认的实现
 * @see OrderServiceStaticProxy 静态代理的实现方式
 * @see JdkOrderServiceProxy JDK 代理的方式
 * @see
 */
public interface IOrderService {

    /**
     * 创建订单
     */
    void createOrder();

    /**
     * 设置数据源
     *
     * @param dataSource 设置数据源
     */
    void setDataSource(String dataSource);

}

```

```java
/**
 * 默认的订单实现,被代理对象
 *
 * @author EricChen 2020/01/01 15:04
 */
public class OrderServiceImpl implements IOrderService {

    private String dataSource;

    @Override
    public void createOrder() {
        System.out.println("创建订单,使用数据源[" + dataSource + "]");

    }

    @Override
    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }
}
```

CglibOrderServiceProxy

```java
public class CglibOrderServiceProxy implements MethodInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(CglibOrderServiceProxy.class);

    public <T> T getInstance(Class<T> clazz) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        return (T) enhancer.create();
    }


    @Override
    public Object intercept(Object targetObject, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        if (!"createOrder".equals(method.getName())) {
            return methodProxy.invokeSuper(targetObject, objects);
        }
        if (!(targetObject instanceof IOrderService)) {
            return methodProxy.invokeSuper(targetObject, objects);
        }
        long currentTimeMillis = System.currentTimeMillis();
        String s = String.valueOf(currentTimeMillis);
        String substring = s.substring(s.length() - 1);
        LOGGER.debug("当前时间为{},切换数据源为{}", s, substring);
        Method setDataSource = targetObject.getClass().getMethod("setDataSource", String.class);
        setDataSource.invoke(targetObject, substring);
        methodProxy.invokeSuper(targetObject, objects);
        return null;
    }
}

```

```java
public class JdkOrderServiceProxy implements InvocationHandler {

    private IOrderService targetObject;
    private static final Logger LOGGER = LoggerFactory.getLogger(JdkOrderServiceProxy.class);


    public JdkOrderServiceProxy(IOrderService targetObject) {
        this.targetObject = targetObject;
    }

    /**
     * 获取被代理接口实例对象
     */
    public IOrderService getProxy() {
        return (IOrderService) Proxy.newProxyInstance(targetObject.getClass().getClassLoader(), targetObject.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!"createOrder".equals(method.getName())) {
            return method.invoke(proxy, args);
        }
        long currentTimeMillis = System.currentTimeMillis();
        String s = String.valueOf(currentTimeMillis);
        String substring = s.substring(s.length() - 1);
        LOGGER.debug("当前时间为{},切换数据源为{}", s, substring);
        targetObject.setDataSource(substring);
        targetObject.createOrder();
        return null;
    }
}
```

```java
/**
 * 通过静态代理的方式切换数据源
 *
 * @author EricChen 2020/01/01 15:07
 */
public class OrderServiceStaticProxy implements IOrderService {

    private String dataSource;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceStaticProxy.class);

    private IOrderService orderServiceProxy;

    public OrderServiceStaticProxy(IOrderService proxyTarget) {
        this.orderServiceProxy = proxyTarget;
    }

    @Override
    public void createOrder() {
        long currentTimeMillis = System.currentTimeMillis();
        String s = String.valueOf(currentTimeMillis);
        String substring = s.substring(s.length() - 1);
        LOGGER.debug("当前时间为{},切换数据源为{}", s, substring);
        orderServiceProxy.setDataSource(substring);
        orderServiceProxy.createOrder();
    }

    @Override
    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }
}
```