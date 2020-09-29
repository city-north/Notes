# Spring依赖注入流程-原型模式的依赖检查

只有在单例情况下才会尝试解决循环依赖，如果存在A中有B的属性，B中有A的属性，那么当依赖注入的时候，就会产生当A还未创建完的时候因为对于B的创建再次返回创建A，造成循环依赖，也就是情况：isPrototypeCurrentlyInCreation(beanName) 判断true。

## 源码

![image-20200929211608178](../../assets/image-20200929211608178.png)

![image-20200922192538797](../../assets/image-20200922192538797.png)