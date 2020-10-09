# 检测parentBeanFactory

从代码上看，如果缓存没有数据的话直接转到父类工厂上去加载了，这是为什么呢？


## 源码

可能读者忽略了一个很重要的判断条件：parentBeanFactory != null && !containsBean Definition (beanName)，parentBeanFactory != null。



![image-20200929211813368](../../assets/image-20200929211813368.png)

parentBeanFactory如果为空，则其他一切都是浮云，这个没什么说的，但是 !containsBeanDefinition(beanName) 就比较重要了，它是在检测如果当前加载的XML配置文件中不包含beanName所对应的配置，就只能到parentBeanFactory去尝试下了，然后再去递归的调用getBean方法。

![image-20200922192538797](../../assets/image-20200922192538797.png)