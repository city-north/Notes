# Host组件

Tomcat中Host组件代表虚拟主机，这些虚拟主机可以存放若干Web应用的抽象(Context容器)。除了Context组件之外，它还包含如下组件。

- Listener组件：可以在Tomcat生命周期中完成某些Host容器相关工作的监听器。
- AccessLog组件：客户端的访问日志，对该虚拟主机上所有Web应用的访问都会被记录。
- Cluster组件：它提供集群功能，可以将Host容器需要共享的数据同步到集群中的其他Tomcat实例上。
- Pipeline组件：Host容器对请求进行处理的管道。
- Realm组件：提供了Host容器级别的用户-密码-权限的数据对象，配合资源认证模块使用。