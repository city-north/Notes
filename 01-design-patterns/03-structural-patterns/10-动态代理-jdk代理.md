# JDK代理

> - 

## 目录

- [JDK代理使用方法](#JDK代理使用方法)
- [手写模仿JDK代理](#手写模仿JDK代理)

- [JDK代理实现原理](#JDK代理实现原理)

## JDK代理使用方法



## 手写模仿JDK代理

## JDK代理实现原理

1. 拿到被代理类的引用,并且获取它的所有接口(反射获取)
2. `JDK Proxy` 类重新生成一个新的类,实现了被代理类的所有接口的方法
3. 动态生成 Java 代码,把增强逻辑加入到新生成的代码中
4. 编译生成新的 Java 代码的 class 文件
5. 加载并重新运行新的 class ,得到类就是全新类

#### JDK 代理代码实例

JDK代理是不需要以来第三方的库，只要要JDK环境就可以进行代理，它有几个要求

* 

```java
/**
 * JDK代理 ,用来生成源代码的工具类
 *
 * @author EricChen 2020/01/01 12:16
 */
public class JDKProxy implements InvocationHandler {

    private Object targetObject;


    public JDKProxy(Object targetObject) {
        this.targetObject = targetObject;
    }

    /**
     * 获取被代理接口实例对象
     *
     * @param <T>
     * @return
     */
    public <T> T getProxy() {
        return (T) Proxy.newProxyInstance(targetObject.getClass().getClassLoader(), targetObject.getClass().getInterfaces(), this);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before");
        Object invoke = method.invoke(targetObject, args);
        System.out.println("after");
        return invoke;
    }

}

```

JDK实现代理只需要使用`newProxyInstance`方法，但是该方法需要接受三个参数，完整的写法是：

```java
/**
 * JDK 代码实例
 *
 * @author EricChen 2020/01/01 12:22
 */
public class JDKProxyExample {

    public static void main(String[] args) {
        // 保存生成的代理类的字节码文件
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");

        // jdk动态代理测试
        Action action = new JDKProxy(new RealObject()).getProxy();
        action.doSomething();
    }

}

```

注意，该方法是在Proxy类中是静态方法，且接受了三个参数依次为：

* `ClassLoader loader`，指定当前目标对象使用类加载器，获取加载器的方法是固定的。
* `Class<?& \[\] interfaces` ，目标对象实现的接口的类型，使用泛型方式确认类型。
* `InvocationHandler` ，事件处理，执行目标对象的方法时，会触发事件处理器的方法，会把当前执行目标对象的方法作为参数。

代理对象不需要实现接口，但是目标独享一定要实现接口，否则不能用动态代理。

#### 动态生成的 class文件

```java
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sun.`proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;
import vip.ericchen.study.designpatterns.structural.proxy.staticproxy.Action;

public final class $Proxy0 extends Proxy implements Action {
    private static Method m1;
    private static Method m3;
    private static Method m2;
    private static Method m0;

    public $Proxy0(InvocationHandler var1) throws  {
        super(var1);
    }

    public final boolean equals(Object var1) throws  {
        try {
            return (Boolean)super.h.invoke(this, m1, new Object[]{var1});
        } catch (RuntimeException | Error var3) {
            throw var3;
        } catch (Throwable var4) {
            throw new UndeclaredThrowableException(var4);
        }
    }

    public final void doSomething() throws  {
        try {
            super.h.invoke(this, m3, (Object[])null);
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    public final String toString() throws  {
        try {
            return (String)super.h.invoke(this, m2, (Object[])null);
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    public final int hashCode() throws  {
        try {
            return (Integer)super.h.invoke(this, m0, (Object[])null);
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    static {
        try {
            m1 = Class.forName("java.lang.Object").getMethod("equals", Class.forName("java.lang.Object"));
            m3 = Class.forName("vip.ericchen.study.designpatterns.structural.proxy.staticproxy.Action").getMethod("doSomething");
            m2 = Class.forName("java.lang.Object").getMethod("toString");
            m0 = Class.forName("java.lang.Object").getMethod("hashCode");
        } catch (NoSuchMethodException var2) {
            throw new NoSuchMethodError(var2.getMessage());
        } catch (ClassNotFoundException var3) {
            throw new NoClassDefFoundError(var3.getMessage());
        }
    }
}

```

