# Tomcat属性与环境变量

## 环境变量

| 环境变量    | 解释                                                         |
| ----------- | ------------------------------------------------------------ |
| %JAVA_HOME% | 表示JDK的安装目录。                                          |
| %CLASSPATH% | JDK搜索class时优先搜索%CLASSPATH%指定的jar包。JDK搜索class时优先搜索%CLASSPATH%指定的jar包。 |
| %PATH%      | 执行某命令时，如果在本地找不到此命令或文件，则会从%PATH%变量声明的目录中区查找。 |

## JVM系统变量

| 环境变量                                                     | 解释                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| user.dir                                                     | 表示当前用户工作目录                                         |
| java.io.tmpdir                                               | 表示系统默认的临时文件目录。不同操作系统的目录不同。         |
| java.home                                                    | 表示Java安装目录。                                           |
| user.home                                                    | 表示用户目录。                                               |
| java.vm.vendor                                               | 表示Java虚拟机实现供应商。                                   |
| java.runtime.version                                         | 表示Java运行时版本号。                                       |
| java.library.path                                            | 表示系统搜索库文件的路径。                                   |
| java.vendor                                                  | 表示Java运行时环境供应商。                                   |
| java.ext.dirs                                                | 表示Java扩展包的目录。                                       |
| user.name                                                    | 表示用户的账户名。                                           |
| package.definition                                           | 表示Java安全管理器需要检查的包。                             |
| package.access                                               | 表示Java安全管理器需要检查访问权限的包。                     |
| path.separator                                               | 表示多个文件路径之间的分隔符。                               |
| file.encoding                                                | 表示默认JVM编码。                                            |
| os.version                                                   | 表示操作系统的版本。                                         |
| **catalina.home**                                            | 配置Tomcat的安装目录。这个路径变量很重要，Tomcat中常用到。在执行Tomcat启动的批处理脚本中会附带-Dcatalina.home=＂%CATALINA_HOME%＂，即启动Tomcat程序时会把catalina.home作为JVM系统变量。 |
| **catalina.base**                                            | 配置Tomcat的工作目录。这个目录容易与catalina.home混淆，工作目录与安装目录有什么区别呢？当我们想要运行多个Tomcat实例时，就可以创建多个工作目录，而使用同一个安装目录，达到了多个Tomcat实例重用Tomcat程序的目的。在执行Tomcat启动的批处理脚本中会附带-`Dcatalina.base=＂%CATALINA_BASE %＂`，即启动Tomcat程序时会把catalina.base作为JVM系统变量 |
| catalina.config                                              | 配置Tomcat配置文件catalina.properties的路径                  |
| org.apache.catalina.startup.EXIT_ON_INIT_FAILURE             | 配置启动初始化阶段遇到问题是否退出                           |
| tomcat.util.scan.DefaultJarScanner.jarsToSkip                | 配置此选项将使JarScanner扫描时会跳过这些包                   |
| org.apache.catalina.startup.ContextConfig.jarsToSkip         | 配置此选项避免扫描Servlet 3.0插件功能。                      |
| org.apache.catalina.startup.TldConfig.jarsToSkip             | 配置此选项避免扫描TLD                                        |
| org.apache.catalina.tribes.dns_lookups                       | 配置是否在集群中尝试使用DNS查找主机。                        |
| org.apache.catalina.connector.CoyoteAdapter.ALLOW_BACKSLASH  | 配置是否允许使用“\”符号作为路径分隔符。                      |
| org.apache.tomcat.util.buf.UDecoder.ALLOW_ENCODED_SLASH      | 配置是否允许使用%2F和%5C作为路径分隔符。                     |
| org.apache.catalina.core.ApplicationContext.GET_RESOURCE_REQUIRE_SLASH | 配置是否传入ServletContext.getResource()或ServletContext.getResourceAsStream()的参数一定要以“/”开头。 |
| org.apache.tomcat.util.http.ServerCookie.ALLOW_EQUALS_IN_VALUE | 配置Cookie中的值是否可以包含“=”符号。                        |
| org.apache.catalina.session.StandardSession.ACTIVITY_CHECK   | 配置是否跟踪统计活跃的会话数。                               |
| org.apache.catalina.authenticator.Constants.SSO_SESSION_COOKIE_NAME | 配置单点登录的会话Cookie名字。                               |
| jvmRoute                                                     | 配置Engine默认的路由标识。                                   |
| org.apache.jasper.Constants.SERVICE_METHOD_NAME              | 配置JSP执行时调用的服务方法，默认是_jspService。             |
| org.apache.jasper.Constants.JSP_PACKAGE_NAME                 | 配置编译的JSP页面的包名，默认为org.apache.jsp。              |
| org.apache.juli.formatter                                    | 配置日志框架的格式类。                                       |
| org.apache.juli.AsyncMaxRecordCount                          | 配置异步方式下日志在内存中能保存的最大记录数。               |
| org.apache.juli.AsyncOverflowDropType                        | 配置异步方式下到达日志记录内存限制时所采取的措施。           |
| org.apache.coyote.USE_CUSTOM_STATUS_MSG_IN_HEADER            | 配置是否在HTTP报文头部使用自定义状态。                       |

## Tomcat属性

| Tomcat属性         |                                                              |
| ------------------ | ------------------------------------------------------------ |
| package.access     | 此属性与Java安全管理器的权限配置有关，用于配置包的访问权限。它的值包含多个包路径，默认配置为package.access=sun., org.apache.catalina., org.apache.coyote., org.apache.tomcat., org. apache.jasper.。 |
| package.definition | 此属性与Java安全管理器的权限配置相关，用于配置包的定义权限。默认配置为package.definition=sun.,java., org.apache.catalina., org.apache.coyote., org.apache.tomcat., org.apache. jasper.。 |
| common.loader      | 此属性用于配置Tomcat中用commonLoader类加载器加载的类库。配置的值可以使用特定的变量，例如`${catalina.base}`，Tomcat程序中会对其进行解析替换。默认配置为`common. loader=${catalina.base}/lib`, `${catalina.base}/lib/-*.jar`, `${catalina.home}/lib`, `${catalina.home}/lib/-*.jar`。 |
| server.loader      | 此属性用于配置Tomcat中用serverLoader类加载器加载的类库。默认配置为空。 |
| shared.loader      | 此属性用于配置Tomcat中用sharedLoader类加载器加载的类库。默认配置为空。 |