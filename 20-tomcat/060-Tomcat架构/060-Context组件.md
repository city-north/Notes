# Context组件

Context组件是Web应用的抽象，我们开发的Web应用部署到Tomcat后运行时就会转化成Context对象。它包含了各种静态资源、若干Servlet(Wrapper容器)以及各种其他动态资源。它主要包括如下组件。

- Listener组件：可以在Tomcat生命周期中完成某些Context容器相关工作的监听器。
- AccessLog组件：客户端的访问日志，对该Web应用的访问都会被记录。
- Pipeline组件：Context容器对请求进行处理的管道。
- Realm组件：提供了Context容器级别的用户-密码-权限的数据对象，配合资源认证模块使用。
- Loader组件：Web应用加载器，用于加载Web应用的资源，它要保证不同Web应用之间的资源隔离。
- Manager组件：会话管理器，用于管理对应Web容器的会话，包括维护会话的生成、更新和销毁。
- NamingResource组件：命名资源，它负责将Tomcat配置文件的server.xml和Web应用的context.xml资源和属性映射到内存中。
- Mapper组件：Servlet映射器，它属于Context内部的路由映射器，只负责该Context容器的路由导航。
- Wrapper组件：Context的子容器。