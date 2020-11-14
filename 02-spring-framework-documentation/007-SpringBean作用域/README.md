# SpringBean作用域

- 作用域列表

- 官方解释
- 目录

## 作用域列表

| 来源        | 说明                                                         |
| ----------- | ------------------------------------------------------------ |
| singleton   | 默认的SpringBean**作用域了一个BeanFactory有且仅有一个实例**,并不代表在一个层次性内是唯一的 |
| prototype   | 原型作用域,每次一来查找和依赖注入都会生成新的Bean对象        |
| request     | 针对Servlet引擎,将Spring Bean存储在ServletRequest 上下文中,用于模板引擎的渲染 |
| session     | 针对Servlet引擎,将Spring Bean 存储在HttpSession中,用于模板引擎的渲染 |
| application | 针对Servlet引擎,将Spring Bean 存储在ServletContext中,用于模板引擎的渲染 |
| websocket   | Scopes a single bean definition to the lifecycle of a `WebSocket`. Only valid in the context of a web-aware Spring `ApplicationContext`. |

## 官方解释

| Scope                                                        | Description                                                  |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| [singleton](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-scopes-singleton) | (Default) Scopes a single bean definition to a single object instance for each Spring IoC container. |
| [prototype](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-scopes-prototype) | Scopes a single bean definition to any number of object instances. |
| [request](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-scopes-request) | Scopes a single bean definition to the lifecycle of a single HTTP request. That is, each HTTP request has its own instance of a bean created off the back of a single bean definition. Only valid in the context of a web-aware Spring `ApplicationContext`. |
| [session](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-scopes-session) | Scopes a single bean definition to the lifecycle of an HTTP `Session`. Only valid in the context of a web-aware Spring `ApplicationContext`. |
| [application](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-scopes-application) | Scopes a single bean definition to the lifecycle of a `ServletContext`. Only valid in the context of a web-aware Spring `ApplicationContext`. |
| [websocket](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#websocket-stomp-websocket-scope) | Scopes a single bean definition to the lifecycle of a `WebSocket`. Only valid in the context of a web-aware Spring `ApplicationContext`. |



通常情况下,我们只要记住两种作用域就行了

- Singleton 单例作用域, 对应Gof23里面的单例模式 [04-singleton-pattern.md](../../01-design-patterns/02-creational-patterns/04-singleton-pattern.md) 
- Prototype 原型作用域,对应Gof23里面的原型模式 [06-prototype-pattern.md](../../01-design-patterns/02-creational-patterns/06-prototype-pattern.md) 

其他的几种都是在特定的环境下使用的,仅仅只是存储的地方不一样

## 目录

-  [020-Singleton-Bean作用域.md](020-Singleton-Bean作用域.md) 

-  [030-Prototype-Bean作用域.md](030-Prototype-Bean作用域.md) 
-  [040-Request-Bean作用域.md](040-Request-Bean作用域.md) 
-  [050-Session-Bean作用域.md](050-Session-Bean作用域.md) 
-  [060-Application-Bean作用域.md](060-Application-Bean作用域.md) 

