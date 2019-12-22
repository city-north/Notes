# Cloud Native Applications:云原生应用

## 什么是云原生应用

- 更快交付
- 降低风险
- 拓展业务

云原生是一种方法，用于构建和运行充分利用云计算模型优势的应用。云计算不再将重点放在资本投资和员工上来运行企业数据中心，而是提供无限制的按需计算能力和根据使用情况付费的功能，从而重新定义了几乎所有行业的竞争格局。IT 开销减少意味着入行的壁垒更低，这一竞争优势使得各团队可以快速将新想法推向市场，这就是软件正在占据世界，并且初创公司正在使用云原生方法来颠覆传统行业的原因。

但是，企业需要一个用于构建和运行云原生应用和服务的平台，来自动执行并集成 DevOps、持续交付、微服务和容器等概念：

<img src="assets/diagram-cloud-native.png" alt="Cloud-Native" style="zoom: 50%;" />



- DevOps 是软件开发人员和 IT 运营之间的合作，目标是自动执行软件交付和基础架构更改流程。它创造了一种文化和环境，可在其中快速、频繁且更可靠地构建、测试和发布软件。

- 持续交付使得单个应用更改在准备就绪后即可发布，而不必等待与其他更改捆绑发布或等待维护窗口期等事件。持续交付让发布行为变得平淡可靠，因此企业可以以更低的风险频繁交付，并更快地获得最终用户的反馈，直到部署成为业务流程和企业竞争力必不可少的组成部分。

- 微服务是将应用作为小型服务集合进行开发的架构方法，其中每个服务都可实施业务功能，在自己的流程中运行并通过 HTTP API 进行通信。每个微服务都可以独立于应用中的其他服务进行部署、升级、扩展和重新启动，通常作为自动化系统的一部分运行，可以在不影响最终客户的情况下频繁更新正在使用中的应用。

- 与标准虚拟机相比，容器能同时提供效率和速度。单个操作系统实例使用操作系统 级的虚拟化，在一个或多个隔离容器之间进行动态划分，每个容器都具有唯一的可写文件系统和资源配额。创建和破坏容器的开销较低，再加上单个虚拟机中的高包装密度，使容器成为部署各个微服务的完美计算工具。

## 为什么云原生应用如此重要

云原生应用专为云模型而开发。小的专用功能团队快速将这些应用构建和部署到可提供轻松的横向扩展和硬件解耦的平台，为企业提供更高的敏捷性、弹性和云间的可移植性。

### 云是一种竞争优势

云原生意味着将云目标从节约 IT 成本转变为推动企业发展。在软件时代，如果企业可以根据客户需求快速构建和交付应用，那么该企业将在其行业中占据主导地位。一旦交付，应用必须像永远在线的弹性扩展服务一样运行。

#### 灵活性

企业可以构建无需修改便可在任何云上运行的应用。团队可以保留跨多个云供应商和一个私有云迁移或分发应用的能力，以匹配自己的业务优先级并优化云定价。

#### **让开发人员以最好的状态工作**

采用云原生应用的团队可为开发人员省去为了在各种云基础架构间运行和扩展而编写代码所产生的开销，让他们专注于编写能够交付客户价值的代码。标准化开发人员体系上的 12 因素应用需要一套标准的服务，从而提供标准的开发人员“合同”，确保其应用充分利用底层的云原生平台。

#### **协调运营和业务**

通过实现自动化 IT 运营，企业可以转变为一个重点明确的精益团队，与推动业务优先事项保持一致。由于员工专注于流程改进，而不是日常的普通管理任务，他们可以消除由于人为错误导致的故障风险。通过在体系的所有层面进行自动化的实时修补和升级，他们可以消除停机时间，并且不再需要具有“传承”专业知识的运营专家。

## 主要区别：云原生与传统企业应用

|                          云原生应用                          |                        传统的企业应用                        |
| :----------------------------------------------------------: | :----------------------------------------------------------: |
| **可预测。** 云原生应用符合旨在通过可预测行为最大限度提高弹性的框架或“合同”。云平台中使用的高度自动化的容器驱动的基础架构推动着软件编写方式的发展。第一次作为 12 因素应用记录的 12 个原则就是阐释此类“合同”的良好示例。 | **不可预测。** 传统应用的架构或开发方式使其无法实现在云原生平台上运行的所有优势。此类应用通常构建时间更长，大批量发布，只能逐渐扩展，并且会发生更多的单点故障。 |
| **操作系统抽象化。** 云原生应用架构要求开发人员使用平台作为一种方法，从底层基础架构依赖关系中抽象出来，从而实现应用的简单迁移和扩展。实现云原生应用架构最有效的抽象方法是提供一个形式化的平台。Pivotal Platform 非常适用于在谷歌云端平台 、微软 Azure 或亚马逊云服务等基于云的基础架构上运行。 | **依赖操作系统。** 传统的应用架构允许开发人员在应用和底层操作系统、硬件、存储和支持服务之间建立紧密的依赖关系。这些依赖关系使应用在新基础架构间的迁移和扩展变得复杂且充满风险，与云模型相背而驰。 |
| **合适的容量。** 云原生应用平台可自动进行基础架构调配和配置，根据应用的日常需求在部署时动态分配和重新分配资源。基于云原生运行时的构建方式可优化应用生命周期管理，包括扩展以满足需求、资源利用率、可用资源编排，以及从故障中恢复，最大程度减少停机时间。 | **过多容量。** 传统 IT 会为应用设计专用的自定义基础架构解决方案，这延迟了应用的部署。由于基于最坏情况估算容量，解决方案通常容量过大，同时几乎没有能力继续扩展以满足需求。 |
| **协作。** 云原生可协助 DevOps，从而在开发和运营职能部门之间建立密切协作，将完成的应用代码快速顺畅地转入生产。 | **孤立。** 传统 IT 将完成的应用代码从开发人员“隔墙”交接到运营，然后由运营人员在生产中运行此代码。企业的内部问题之严重以至于无暇顾及客户，导致内部冲突产生，交付缓慢折中，员工士气低落。 |
| **持续交付。** IT 团队可以在单个软件更新准备就绪后立即将其发布出去。快速发布软件的企业可获得更紧密的反馈循环，并能更有效地响应客户需求。持续交付最适用于其他相关方法，包括测试驱动型开发和持续集成。 | **瀑布式开发。** IT 团队定期发布软件，通常间隔几周或几个月，事实上，当代码构建至发布版本时，该版本的许多组件已提前准备就绪，并且除了人工发布工具之外没有依赖关系。如果客户需要的功能被延迟发布，那企业将会错失赢得客户和增加收入的机会。 |
| **独立。** 微服务架构将应用分解成小型松散耦合的独立运行的服务。这些服务映射到更小的独立开发团队，可以频繁进行独立的更新、扩展和故障转移/重新启动操作，而不影响其他服务。 | **依赖。** 一体化架构将许多分散的服务捆绑在一个部署包中，使服务之间出现不必要的依赖关系，导致开发和部署过程丧失敏捷性。 |
| **自动化可扩展性。** 大规模基础架构自动化可消除因人为错误造成的停机。计算机自动化无需面对此类挑战，可以在任何规模的部署中始终如一地应用同一组规则。云原生还超越了基于以虚拟化为导向的传统编排而构建的专用自动化。全面的云原生架构包括适用于团队的自动化和编排，而不要求他们将自动化作为自定义方法来编写。换句话说，自动化可轻松构建和运行易于管理的应用。 | **手动扩展。** 手动基础架构包括人工运营人员，他们负责手动构建和管理服务器、网络及存储配置。由于复杂程度较高，运营人员无法快速地大规模正确诊断问题，并且很容易执行错误实施。手动构建的自动化方法可能会将人为错误的硬编码到基础架构中。 |
| **快速恢复。** 容器运行时和编排程序可在虚拟机上提供动态的高密度虚拟化覆盖，与托管微服务非常匹配。编排可动态管理容器在虚拟机群集间的放置，以便在发生故障时提供弹性扩展和恢复/重新启动功能。 | **恢复缓慢。** 基于虚拟机的基础架构对于基于微服务的应用来说是一个缓慢而低效的基础，因为单个虚拟机启动或关闭的速度很慢，甚至在向其部署应用代码之前就存在很大的开销。 |



## SpringCloud 中的云原生应用

> [Cloud Native](https://pivotal.io/platform-as-a-service/migrating-to-cloud-native-application-architectures-ebook) is a style of application development that encourages easy adoption of best practices in the areas of continuous delivery and value-driven development. A related discipline is that of building [12-factor Applications](https://12factor.net/), in which development practices are aligned with delivery and operations goals — for instance, by using declarative programming and management and monitoring. Spring Cloud facilitates these styles of development in a number of specific ways. The starting point is a set of features to which all components in a distributed system need easy access.

- Cloud Native 是一种鼓励在持续交付和价值驱动开发领域轻松采用最佳实践的应用程序开发风格。
- 一个相关的原则是构建 [12-factor](../01-basic/04-12-factor.md)，在这个原则中，开发实践与交付和操作目标是一致的
- SpringCloud 使用不同的方式实践着这个风格

> Many of those features are covered by [Spring Boot](https://projects.spring.io/spring-boot), on which Spring Cloud builds. Some more features are delivered by Spring Cloud as two libraries: Spring Cloud Context and Spring Cloud Commons. Spring Cloud Context provides utilities and special services for the `ApplicationContext` of a Spring Cloud application (bootstrap context, encryption, refresh scope, and environment endpoints). Spring Cloud Commons is a set of abstractions and common classes used in different Spring Cloud implementations (such as Spring Cloud Netflix and Spring Cloud Consul).

- SpringBoot 覆盖了这些特性,SpringCloud 基于 SpringBoot 构建

- 更多的特性体现在 SpringCloud 的两个库中

  - Spring Cloud Context

    提供了基于`ApllicationContet`的工具类与特殊服务如

    - bootstrap context
    - encryption
    - refresh scope
    - environment endpoints

  - Spring Cloud Commons是一组SpringCloud 不同实现类的抽象.和一些 SpringCloud 实现类的通用工具,例如 SpringCloud Netflix 和 SpringCloud Consul

  
