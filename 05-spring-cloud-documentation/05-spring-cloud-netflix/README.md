# Spring Cloud Netflix

## [Modules In Maintenance Mode](https://cloud.spring.io/spring-cloud-static/spring-cloud-netflix/2.2.1.RELEASE/reference/html/#modules-in-maintenance-mode)

Placing a module in maintenance mode means that the Spring Cloud team will no longer be adding new features to the module. We will fix blocker bugs and security issues, and we will also consider and review small pull requests from the community.

We intend to continue to support these modules for a period of at least a year from the general availability of the Greenwich release train.

The following Spring Cloud Netflix modules and corresponding starters will be placed into maintenance mode:

- spring-cloud-netflix-archaius
- spring-cloud-netflix-hystrix-contract
- spring-cloud-netflix-hystrix-dashboard
- spring-cloud-netflix-hystrix-stream
- spring-cloud-netflix-hystrix
- spring-cloud-netflix-ribbon
- spring-cloud-netflix-turbine-stream
- spring-cloud-netflix-turbine
- spring-cloud-netflix-zuul

官方建议的替代项目如下表所示：

| 当前                        | 替代项目                                                     |
| :-------------------------- | :----------------------------------------------------------- |
| Hystrix                     | [Resilience4j](https://github.com/resilience4j/resilience4j) |
| Hystrix Dashboard / Turbine | Micrometer + Monitoring System                               |
| Ribbon                      | Spring Cloud Loadbalancer                                    |
| Zuul 1                      | Spring Cloud Gateway                                         |
| Archaius 1                  | Spring Boot external config + Spring Cloud Config            |

> **本文链接**：[Spring Cloud Netflix项目进入维护模式之我见](http://www.itmuch.com/spring-cloud-sum/spring-cloud-netflix-in-maintenance-mode/)
>
> 转载声明：本博客由周立创作，采用 [CC BY 3.0 CN ](https://creativecommons.org/licenses/by-nc-nd/3.0/)许可协议。可自由转载、引用，但需署名作者且注明文章出处。如转载至微信公众号，请在文末添加作者公众号二维码。
>
> > ### Spring Cloud Netflix项目进入维护模式
> >
> > 最近，Netflix [宣布](https://github.com/Netflix/Hystrix#hystrix-status) Hystrix正在进入维护模式。自2016年以来，Ribbon已处于[类似状态](https://github.com/Netflix/ribbon#project-status-on-maintenance)。虽然Hystrix和Ribbon现已处于维护模式，但它们仍然在Netflix大规模部署。
> >
> > Hystrix Dashboard和Turbine已被Atlas取代。这些项目的最后一次提交别是2年和4年前。Zuul 1和Archaius 1都被后来不兼容的版本所取代。
> >
> > 以下Spring Cloud Netflix模块和相应的Starter将进入维护模式：
> >
> > 1. spring-cloud-netflix-archaius
> > 2. spring-cloud-netflix-hystrix-contract
> > 3. spring-cloud-netflix-hystrix-dashboard
> > 4. spring-cloud-netflix-hystrix-stream
> > 5. spring-cloud-netflix-hystrix
> > 6. spring-cloud-netflix-ribbon
> > 7. spring-cloud-netflix-turbine-stream
> > 8. spring-cloud-netflix-turbine
> > 9. spring-cloud-netflix-zuul
> >
> > 这不包括Eureka或并发限制模块。
> >
> > #### 什么是维护模式？
> >
> > 将模块置于维护模式，意味着Spring Cloud团队将不会再向模块添加新功能。我们将修复block级别的bug以及安全问题，我们也会考虑并审查社区的小型pull request。
> >
> > 我们打算继续支持这些模块，直到 [Greenwich版本](https://github.com/spring-cloud/spring-cloud-release/milestones?direction=asc&sort=due_date) 被GA（general availability，即正式版）**至少**一年。
>
> ## 解读
>
> 从上文可知，由于Netflix对Zuul 1、Ribbon、Archaius等的维护不力，Spring Cloud决定在Greenwich中将如上项目都进入“维护模式”——
>
> 基本上，除了`spring-cloud-netflix-eureka-*` 以及`spring-cloud-netflix-concurrency-limits` ，其他模块都进入“维护模式了”。
>
> > **TIPS：**
> >
> > - 考虑到`spring-cloud-netflix-concurrency-limits` 可能很多童鞋没有见过。简单介绍一下，`concurrency-limits` 是Netflix开源的限流器项目，Spring Cloud在Greenwich版本中引入。
> > - Netflix Concurrency Limits的GitHub：https://github.com/Netflix/concurrency-limits
>
> 进入维护模式意味着，再也不会有功能的变化了，不过呢，Spring Cloud承诺会维护严重的Bug & 接受社区的pull request。
>
> ## 何去何从？
>
> 这应该是大家最关注的问题。目前业界对Spring Cloud使用最广的就是Spring Cloud Netflix了。这TM一下80%都进入“维护模式”，再也没有新功能福利了，让人如何是好啊！**就目前来看，继续使用Sprng Cloud Netflix问题不大，但长期来看，显然是不合适的。**
>
> 官方建议的替代项目如下表所示：
>
> | 当前                        | 替代项目                                                     |
> | :-------------------------- | :----------------------------------------------------------- |
> | Hystrix                     | [Resilience4j](https://github.com/resilience4j/resilience4j) |
> | Hystrix Dashboard / Turbine | Micrometer + Monitoring System                               |
> | Ribbon                      | Spring Cloud Loadbalancer                                    |
> | Zuul 1                      | Spring Cloud Gateway                                         |
> | Archaius 1                  | Spring Boot external config + Spring Cloud Config            |
>
> ## 替代项目的孵化进度
>
> 目前：
>
> - Hystrix的替代Resilience4j：目前在https://github.com/spring-cloud-incubator/spring-cloud-circuitbreaker 中孵化。**该项目原名叫`spring-cloud-r4j` ，最近改名为`spring-cloud-circuitbreaker` 。**
>
>   > - TIPS：这么做， 笔者猜想：Spring是要抽象一个断路器的统一规范，让不同的断路器实现去实现，从而实现相同的注解（例如`EnableCircuitBreaker` ，然后不同的实现，诸如Hystrix、Resilience4j、Sentinel等想要接入只需更换不同的starter依赖，使用则完全一样），不过由于尚未孵化完毕，代码也比较新，暂时只是猜想。
>
> - Hystrix Dashboard /Turbine的替代：由于官方建议用Resilience4j替代Hystrix，所以你再也不需要Hystrix的那一堆监控轮子了！**Resilience4j自带整合了Micrometer！这其实是一个个人比较喜欢的福利**。
>
>   > TIPS：
>   >
>   > - 曾记否，你为了Hystrix的监控，得搞Hystrix Dashboard；为了监控微服务集群实例，又得搭Turbine；微服务整合Turbine又有HTTP方式&MQ方式，**两种方式还不能共存，不能兼容**……无比蛋疼！
>   > - Micrometer是Pivotal公司（也就是Spring所在的公司）**开源的监控门面**，类似于监控世界的Slf4j；它可以和各种监控系统/监控画板/时序数据库配合使用，诸如：Graphite、Influx、Ganglia、Prometheus等等。
>   > - Micrometer官网：http://micrometer.io/
>   > - Spring Boot 2中的Spring Boot Actuator底层用的就是Micrometer——这意味着，如果你用Resilience4j，监控的体验和Actuator是一致的！
>
> - Ribbon的替代`Spring Cloud Loadbalancer` ：之前`spring-cloud-loadbalancer` 在`spring-cloud-loadbalancer` 项目（https://github.com/spring-cloud-incubator/spring-cloud-loadbalancer）中孵化，现在，该项目已经成为`spring-cloud-commons` 的子项目了。**使用上，`spring-cloud-loadbalancer` 和Ribbon区别不大**。
>
> - Zuul 1的替代Spring Cloud Gateway：这个基本玩Spring Cloud的都知道。由于Zuul持续跳票1年多，Spring Cloud索性开发了Spring Cloud Gateway。在这里，有Spring Cloud Gateway和Zuul 1.x的性能对比：http://www.itmuch.com/spring-cloud-sum/performance-zuul-and-gateway-linkerd/
>
> - Archaius 1的替代Spring Boot external config + Spring Cloud Config：我太喜欢这个改变了！众所周知，Spring Cloud有N多组件，N多N多配置属性（1000+），其中很多配置是不给提示的。原因在于Spring Boot/Cloud的配置需要借助`spring-boot-configuration-processor` 读取代码的注释，并生成metadata.json文件才能有提示。而Netflix开源的组件（例如Ribbon/Hystrix等）都没有使用Spring Boot的规范，而是自己用Archaius管理配置（那代码风格，个人很不喜欢），根本没有metadata.json文件，于是这部分配置IDE无法给你提示。以后全面废弃Archaius，统一使用Spring Boot external config + Spring Cloud Config，**这意味着未来Spring Boot的编程体验更加统一的同时，配置提示还杠杠的。**
>
> ## 未来&其他的候选者
>
> 我相信未来Spring Cloud的生态会越来越好。事实上Spring Cloud生态中还有其他的替换项目&更多选择：
>
> | 作用             | 业界用得最多        | 已孵化成功的替代项目 | 孵化中的替代项目               |
> | :--------------- | :------------------ | :------------------- | :----------------------------- |
> | 服务发现         | Eureka              | Consul、Zookeeper    | Alibaba Nacos                  |
> | 负载均衡器       | Ribbon              | -                    | -（Hoxton M3才会有替代品）     |
> | 断路器           | Hystrix             | -                    | Resilience4j、Alibaba Sentinel |
> | 声明式HTTP客户端 | Feign               | -                    | Retrofit                       |
> | API网关          | Zuul 1              | Spring Cloud         | -                              |
> | 配置管理         | Spring Cloud Config | Consul、Zookeeper    | Alibaba Nacos                  |

