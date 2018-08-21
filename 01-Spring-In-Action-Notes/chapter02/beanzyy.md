[返回根目录](/README.md)

[返回目录](../README.md)


# bean的作用域

在默认情况下，Spring应用上下文中所有bean都是作为单例（singleton）的形式创建的。不管给定的一个bean被注入到其他bean多少次，每次所注入的都是同一个实例。

除了默认的单例作用域，还有其他：

## Spring中bean的作用域

* **单例（Singleton）：**在整个应用中，只创建bean的一个实例。
* **原型（Prototype）：**每次注入或者通过Spring应用上下文获取的时候，都会创建一个新的bean实例。
* **会话（Session）：**在Web应用中，为每一次会话创建一个bean实例。
* **请求（Request）：**在Web应用中，为每个请求创建一个bean实例。

对于一遍类型，我们会保持一些状态，所以重用是不安全的。如果选择其他作用域，要使用@Scope注解，它可以和@Component或者@Bean一起使用。

## 配置bean的作用域

### 注解

```
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Notepad{...}
```

### XML配置

```
<bean id = "notepad" class="com.myapp.Notepad" scop="prototype" />
```

我们来看一个例子

假设我们要将ShoppingCart bean注入到单例StoreService bean 的Setter方法中：

```
@Component
public class StoreService{
    private ShoppingCart shoppingCart;

    @Autowired
    public void setShoppingCart(ShoppingCart shoppingCart){
        this.shoppingCart = shoppingCart;
    }
}
```

## 使用会话和请求作用域

在一个典型的电子商务应用中，可能会有一个bean代表用户的购物车，如果购物车是单例的话，那么将会导致所有的用户都会向同一个购物车添加商品，另一方面，如果购物车是原型作用域的，那么在应用中某一个地方往购物车中添加商品，在应用的另外一个地方就不可用了，因为注入的是原型作用域（每次都创建一个新的实例）的购物车。

**就购物车bean来说，会话作用域是最为合适的，因为它与给定的用户关联性最大，要指定会话作用域，我们可以使用@Scope注解：**

```
@Component
@Scope(
    value = WebApplicationContext.SCOPE_SESSION,
   proxyMode = ScopedProxyMode.INTERFACS)
public ShoppingCart cart(){...}
```

这里，我们将value设置成了WebApplicationContext中的SCOPE\_SESSION常量（它的值是“session”），他会告诉Spring为Web应用中的**每个会话创建一个ShoppingCart**。在一个Web应用中，会有多个ShoppingCart bean的实例，但是对于给定的会话值会创建一个实例。

因为StoreService 是一个单例的bean,会在Spring应用上下文加载的时候创建。

当它创建的时候，Spring会试图将ShoppingCart bean注入到setShoppingCart\(\)方法中。但是，ShoppingCart bean的作用域是会话作用域，此时并不存在。直到某个用户进入系统，创建了会话之后，才会出现ShoppingCart实例。

另外，系统中将会有多个ShoppingCart实例：每个用户一个，我们并不想让Spring注入某个固定的ShoppingCart实例到StoreService中。我们希望的是当StoreService会处理购物车功能时，他说使用的ShoppingCart实例恰好是当前对话所对应的那一个。

Spring并不会将实际的ShoppingCart bean 注入到StoreService中，Spring会注入一个到ShoppingCart bean的代理。如图3.1。

![](/assets/import02.png)这个代理会暴露与ShoppingCart相同的方法，所以StoreService会认为它就是一个购物车。但是，当StoreService调用ShoppingCart的方法时，代理会对其进行懒解析并将调用委托给会话作用域内真正的ShoppingCart bean。

## 作用域的理解

proxyMode属性被设置成了ScopedProxyMode.INTERFACES,这表明这个代理要实现ShoppingCart接口，并将调用委托给实现bean。

如果ShoppingCart是接口而不是类的话,是可以的，也是最理想的代理方式，

[Spring代理模式的传送门](/chapter02/springProxy.md)

如果ShoppingCart是一个具体的类的话，Spring就没有办法创建基于接口的代理了。

此时，它必须使用CGLib代理来生成基于类的代理。所以，如果bean类型是具体的类的话，我们必须要将proxyMode属性设置为ScopedPrixyMode.TARGET\_CLASS，一次来表示要以生成目标类拓展的方式创建代理。

**请求作用域的bean也以作用域代理的方式进行注入。**

## 在XML配置中声明作用域代理

```
<bean id = "cart" class="com.myapp.ShoppingCart" scope ="session">
    <aop:scoped-proxxy />
</bean>
```

`<aop:scoped-proxy/>`是与`@Scope`注解的proxyMode属性功能相同的Spring XML 配置元素，默认使用CGLib代理创建目标类的代理，但是我们也可以将`proxy-target-class`属性设置为false,进而要求**它生成基于接口的代理**。

```
<bean id = "cart" class ="com.myapp.ShoppingCart" scope="session">
    <aop:scoped-proxy proxy-target-class="false"/>
</bean>
```

[返回根目录](/README.md)

[返回目录](../README.md)

