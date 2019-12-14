# MSA-微服务架构

> Microservices is a variant of the service- oriented architecture (SOA) architectural style that structures an application as a collection of loosely coupled services. In a microservices architecture, services should be fine-grained and the protocols should be lightweight. The benefit of decomposing an application into different smaller services is that it improves modularity and makes the application easier to understand, develop and test.

- 微服务架构是 SOA (Service-oriented-architechture)的一种变种
- 服务应用之间松散耦合
- 细粒度服务,轻量级协议
- 增强模块性,更易理解与开发测试

## 微服务概念

微服务是一种架构风格，是以开发一组小型服务的方式来作为一个独立的应用系统，每个服务都运行在自已 的进程中，服务之间采用轻量级的HTTP通信机制 ( 通常是采用HTTP的RESTful API )进行通信。这些服务都 是围绕具体业务进行构建的，并且可以独立部署到生产环境上。这些服务可以用不同的编程语言编写，并且 可以使用不同的数据存储技术。对这些微服务我们只需要使用一个非常轻量级的集中式管理来进行协调。



![image-20191130202712415](assets/image-20191130202712415.png)

## 优缺点

### 优点

- 易于开发和维护:一个微服务只会关注一个特定的业务功能，所以业务清晰、代码量较少。开发和 维护单个微服务相对简单。

- 单个微服务启动较快

- 局部修改容易部署:单一应用只要有修改，就得重新部署整个应用。微服务解决了这样的问题。一
  般来说，对某个微服务进行修改，只需要重新部署这个服务即可。

- 技术栈不受限制:在微服务架构中，可以结合项目业务及团队的特点，合理的选择技术栈。

- 按需伸缩:可根据需求，实现细粒度的扩展。

### 缺点

- 运维要求高:更多的服务意味着要投入更多的运维。

- 分布式固有的复杂性:使用微服务构建的是分布式系统。对于一个分布式系统，系统容错、网络延
       迟、分布式事务等都会带来巨大的问题。

- 接口调整成本高:微服务之间通过接口进行通信。如果修改某一个微服务的API，可能所有用到这
       个接口的微服务都需要进行调整。