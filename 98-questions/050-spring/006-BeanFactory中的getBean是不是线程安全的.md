# BeanFactory中的getBean是不是线程安全的

是的

`BeanFactory.getBean`方法的执行是线程安全的,操作过程中会增加互斥锁synchronized 代码块



## 注册的时候

![image-20201102001636974](../../assets/image-20201102001636974.png)

## 删除的时候

![image-20201102001653475](../../assets/image-20201102001653475.png)

## 偏向锁

`synchronized`关键字1.6引入了偏向锁机制,所以要尽量避免在主线程以外的线程进行注册或者移除操作