# Spring 的代理

Spring整合了JDK代理（**优先使用**）和cglib代理（**无接口时使用**），

- 当 bean 有实现接口时,Spring 就会使用 JDK 的动态代理
- 当 bean 没有实现接口时,Spring 会选择 Cglib
- Spring 可以通过配置强制使用 CGlib,只需要在 spring 配置文件中

```xml
<aop:aspectj-autoproxy proxy-target-class="true"</>
```

## Cglib代理

上面的**静态代理**和**动态代理**都是要求目标对象是实现一个接口的对象，但是，有些时候，目标对象知识一个单独的对象，并没有实现任何的接口，这个时候就可以使用以目标对象子类的方式实现代理，这种方法叫做Cglib代理，Cglib代理是**继承代理。**

Cglib代理也叫做**子类代理，**它是在内存中构建一个子类对象从而实现对目标对象功能的拓展。

* JDK的动态代理有一个限制，使用动态代理的对象必须实现一个或者多个接口，如果想代理没有实现接口的类，就可以使用Cglib代理，Cglib代理，也叫做子类代理，它是在内存中构建一个子类对象从而实现对目标对象功能的拓展。
* Cglib是一个强大的高性能代码生成器，它在运行期间拓展Java类与现实Java接口,它广泛的被许多AOP的框架使用，例如Spring AOP 和`synaop`,为它们提供了方法的`interception`\(拦截\)
* Cglib包底层是通过使用一个小而快的字节码处理框架ASM来转换字节码并生产新的类，不鼓励直接使用ASM,因为它们要求你必须对JVM的内部结构包括class文件的格式和指令集很熟悉。

### 使用方法：

1. 需要引入cglib的jar文件
2. 代理的类不能为final，否则报错
3. 目标对象的方法如果是final/static，那么久不会被拦截，即不会执行目标对象额外的业务方法。

相关代码：


```java
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

```java
public class Test {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        UserServiceCglibProxyFactory proxy = new UserServiceCglibProxyFactory();
        UserService user = proxy.createProxy(userService.getClass());
        user.save();//执行save方法时，调用的是代理对象的方法intercept

    }
}
```

## 