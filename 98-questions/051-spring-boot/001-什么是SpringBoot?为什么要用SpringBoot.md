# 什么是SpringBoot?为什么要用SpringBoot

SpringBoot主要是用来简化spring应用的初始搭建以及开发过程 使用特定的方式来进行配置(properties或yml文件)

- 独立运行

  一个很鲜明的特点是创建独立的spring引用程序main方法运行 , Spring Boot而且内嵌了各种servlet容器，Tomcat、Jetty等，现在不再需要打成war包部署到容器中， Spring Boot只要打成一个可执行的jar包就能独立运行，所有的依赖包都在一个jar包内。

   [010-SpringBoot为什么要采用嵌入式容器.md](../../03-spring-boot-documentation/02-理解独立的Spring应用/010-SpringBoot为什么要采用嵌入式容器.md) 

- 简化配置

   spring boot来简化spring应用开发，约定大于配置 , spring-boot-starter-web启动器自动依赖其他组件，简少了maven的配置。

3. 自动配置
   Spring Boot能根据当前类路径下的类、jar包来自动配置bean，如添加一个spring-boot-starter-web启 动器就能拥有web的功能，无需其他配置。

4. 无代码生成和XML配置
   Spring Boot配置过程中无代码生成，也无需XML配置文件就能完成所有配置工作，这一切都是借助于条件注解完成的，这也是Spring4.x的核心功能之一。

5. 应用监控
   Spring Boot提供一系列端点可以监控服务及应用，做健康检测。

