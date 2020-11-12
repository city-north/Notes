# SpringBeanDefinition作为依赖来源

- 元数据: BeanDefinition
- 注册: BeanDefinitionRegistry#registerBeanDefinition

- 类型 : 延迟 和非延迟
- 顺序 : Bean生命周期按照注册顺序

#### Spring容器管理和游离对象

| 来源                          | Spring Bean对象 | 生命周期管理 | 配置元信息 | 使用场景          |
| ----------------------------- | --------------- | ------------ | ---------- | ----------------- |
| Spring BeanDifinition         | 是              | 是           | 有         | 依赖查找,依赖注入 |
| 单例对象 singletonObjects     | 是              | 否           | 无         | 依赖查找,依赖注入 |
| 游离对象 ReslovableDependency | 否              | 否           | 无         | 依赖注入          |

#### BeanDefinition的注册

 [030-BeanDefinition的注册.md](../003-SpringBean的定义-BeanDefiniation/030-BeanDefinition的注册.md) 

## 