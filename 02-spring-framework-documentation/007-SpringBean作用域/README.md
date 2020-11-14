# SpringBean作用域

| 来源        | 说明                                                         |
| ----------- | ------------------------------------------------------------ |
| singleton   | 默认的SpringBean**作用域了一个BeanFactory有且仅有一个实例**,并不代表在一个层次性内是唯一的 |
| prototype   | 原型作用域,每次一来查找和依赖注入都会生成新的Bean对象        |
| request     | 针对Servlet引擎,将Spring Bean存储在ServletRequest 上下文中,用于模板引擎的渲染 |
| session     | 针对Servlet引擎,将Spring Bean 存储在HttpSession中,用于模板引擎的渲染 |
| application | 针对Servlet引擎,将Spring Bean 存储在ServletContext中,用于模板引擎的渲染 |
| websocket   |                                                              |

-  [020-Singleton-Bean作用域.md](020-Singleton-Bean作用域.md) 

-  [030-Prototype-Bean作用域.md](030-Prototype-Bean作用域.md) 
-  [040-Request-Bean作用域.md](040-Request-Bean作用域.md) 
-  [050-Session-Bean作用域.md](050-Session-Bean作用域.md) 
-  [060-Application-Bean作用域.md](060-Application-Bean作用域.md) 

