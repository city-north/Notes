# 070-合并后或无Parent的-RootBeanDefinition

---

[TOC]

## 图示

![image-20201116213208976](../../assets/image-20201116213208976.png)

## 参考

此过程是从GenericBeanFactory合并成一个没有Parent的RootBeanFactory

参考  [040-SpringBeanDefinition合并阶段.md](../008-SpringBean生命周期/040-SpringBeanDefinition合并阶段.md) 

## 什么是RootBeanDefinition

RootBeanDefinition是

- 无双亲的BeanDefinition
- GenericBeanDefinition经过合并以后,会生成一个RootBeanDefiniton 



