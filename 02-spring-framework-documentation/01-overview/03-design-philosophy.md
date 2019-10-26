# 设计哲学(Design Philosophy)

学习一个框架,学习他的设计原则很重要,下列是 Spring 的设计原则:

- 在每个 level 都提供选择,并让你的设计选择尽量推迟

例如,你可以切换持久层提供者,仅仅通过配置文件而不修改代码,对于许多其他基础设施问题和与第三方api的集成也是如此。

- 容纳不同观点(Accommodate diverse perspectives)

Spring 拥抱灵活性,但是对问题如何具体解决没有发表意见,它支持具有不同视角的广泛应用程序需求。

- 保持强大的向后兼容(Maintain strong backward compatibility.)
- 关注 API
- 高代码质量