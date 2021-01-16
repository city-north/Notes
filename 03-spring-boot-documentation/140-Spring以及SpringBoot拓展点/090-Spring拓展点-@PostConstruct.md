# 090-Spring拓展点-@PostConstruct

> javax.annotation.PostConstruct

这个并不算一个扩展点，其实就是一个标注。其作用是在bean的初始化阶段，如果对一个方法标注了@PostConstruct，会先调用这个方法。这里重点是要关注下这个标准的触发点，这个触发点是在postProcessBeforeInitialization之后，InitializingBean.afterPropertiesSet之前。

使用场景：用户可以对某一方法进行标注，来进行初始化某一个属性

扩展方式为：

```
publicclassNormalBeanA{publicNormalBeanA(){System.out.println("NormalBean constructor");}
@PostConstructpublicvoid init(){System.out.println("[PostConstruct] NormalBeanA");}}
```

## 