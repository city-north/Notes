# 代理模式（Proxy Patern）

给某一个对象提供一个代理或占位符，并由代理对象来控制对原对象的访问



# Spring代理

代理总共分两种：

* 静态代理
* 动态代理
  * JDK代理，被代理对象必须要**实现接口**，才能产生代理对象，如果没有接口，将不能使用JDK代理
  * cglib代理，第三方代理技术，可以对任何类生产代理，代理的原理是对目标对象进行**继承代理**。如果目标对象呗final修饰，那么该类不能被cglib代理

Spring整合了JDK代理（**优先使用**）和cglib代理（**无接口时使用**），

## 静态代理

分为是角色：

* 共同接口
* 真实对象
* 代理对象




共同接口

```
public interface Action {
    public void doSomething();
}
```

真实对象

```
public class RealObject implements Action{

    public void doSomething() {
        System.out.println("do something");
    }
}
```

代理对象

```
public class Proxy implements Action {
    private Action realObject;

    public Proxy(Action realObject) {
        this.realObject = realObject;
    }
    public void doSomething() {
        System.out.println("proxy do");
        realObject.doSomething();
    }
}
```

测试：

```
Proxy proxy = new Proxy(new RealObject());
    proxy.doSomething();
```

### 静态优势与缺陷

优势：业务类只需要关注业务逻辑本身，保证了业务类的重用性，当需要拓展时，使用其代理类。

缺陷：

* 代理对象的一个接口只服务于一种类型的对象，如果要代理的方法有很多，势必要每一种方法都要进行代理，静态代理在程序规模稍大时就无法胜任了
* 如果接口增加一个方法，除了所有实现类都实现这个方法外，所有的代理类也要实现这个方法，增加了代码维护的复杂度。

## JDK代理

JDK代理是不需要以来第三方的库，只要要JDK环境就可以进行代理，它有几个要求

* 实现InvocationHandler 
* 使用Proxy.newProxyInstance产生代理对象
* 被代理的对象**必须要实现接口**

```
public class JDKProxy implements InvocationHandler {
    private Object targetObject;
    private Logger logger = LoggerFactory.getLogger(ProxyObject.class);


    public Object newProxy(Object targetObject) {//将目标对象传入进行代理
        logger.info("newProxy init()");
        this.targetObject = targetObject;
        return Proxy.newProxyInstance(targetObject.getClass().getClassLoader(),
                targetObject.getClass().getInterfaces(), this);//返回代理对象
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        checkPopedom();//一般我们进行逻辑处理的函数比如这个地方是模拟检查权限
        return method.invoke(targetObject,args);      // 设置方法的返回值
    }

    //模拟检查权限的例子
    private void checkPopedom() {
        logger.info("正在检查权限");
    }
}
```

### JDK实现代理只需要使用newProxyInstance方法，但是该方法需要接受三个参数，完整的写法是：

```
static Object newProxyInstance(ClassLoadr loader,Class<?>[] interfaces,InvocationHandler handler)
```

注意，该方法是在Proxy类中是静态方法，且接受了三个参数依次为：

* ClassLoader loader，指定当前目标对象使用类加载器，获取加载器的方法是固定的。
* Class<?& \[\] interfaces ，目标对象实现的接口的类型，使用泛型方式确认类型。
* InvocationHandler ，事件处理，执行目标对象的方法时，会触发事件处理器的方法，会把当前执行目标对象的方法作为参数。

代理对象不需要实现接口，但是目标独享一定要实现接口，否则不能用动态代理。

## Cglib代理

上面的**静态代理**和**动态代理**都是要求目标对象是实现一个接口的对象，但是，有些时候，目标对象知识一个单独的对象，并没有实现任何的接口，这个时候就可以使用以目标对象子类的方式实现代理，这种方法叫做Cglib代理，Cglib代理是**继承代理。**

Cglib代理也叫做**子类代理，**它是在内存中构建一个子类对象从而实现对目标对象功能的拓展。

* JDK的动态代理有一个限制，使用动态代理的对象必须实现一个或者多个接口，如果想代理没有实现接口的类，就可以使用Cglib代理，Cglib代理，也叫做子类代理，它是在内存中构建一个子类对象从而实现对目标对象功能的拓展。
* Cglib是一个强大的高性能代码生成器，它在运行期间拓展Java类与现实Java接口,它广泛的被许多AOP的框架使用，例如Spring AOP 和synaop,为它们提供了方法的interception\(拦截\)
* Cglib包底层是通过使用一个小而快的字节码处理框架ASM来转换字节码并生产新的类，不鼓励直接使用ASM,因为它们要求你必须对JVM的内部结构包括class文件的格式和指令集很熟悉。

### 使用方法：

1. 需要引入cglib的jar文件
2. 代理的类不能为final，否则报错
3. 目标对象的方法如果是final/static，那么久不会被拦截，即不会执行目标对象额外的业务方法。

相关代码：


```
public class UserServiceCglibProxyFactory implements MethodInterceptor {

    private Logger logger = LoggerFactory.getLogger(UserServiceCglibProxyFactory.class);


    /**
     * 获取Cglib代理对象
     * @return
     */
    public UserService createProxy(Class superclass){
        //创建Cglib核心类
        Enhancer enhancer = new Enhancer();
        //Cglib代理是基于继承的代理
        enhancer.setSuperclass(superclass);
        //设置回调，需要传入一个Callback的接口
        enhancer.setCallback(this);
        //生成代理
        return (UserService)enhancer.create();
    }

    /**
     *
     * 只记录save方法
     * @param proxyobj 代理对象
     * @param method 方法名
     * @param args 代理方法的参数
     * @param methodProxy 代理方法
     * @return
     * @throws Throwable
     */
    public Object intercept(Object proxyobj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object o = methodProxy.invokeSuper(proxyobj, args);
        if ("save".equals(method.getName())){
            logger.info("chapter02.proxy.cglibProxy.UserServiceCglibProxyFactory.createProxy");
            return o;
        }

        return o;
    }
```

测试代码：

```
public class Test {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        UserServiceCglibProxyFactory proxy = new UserServiceCglibProxyFactory();
        UserService user = proxy.createProxy(userService.getClass());
        user.save();//执行save方法时，调用的是代理对象的方法intercept

    }
}
```