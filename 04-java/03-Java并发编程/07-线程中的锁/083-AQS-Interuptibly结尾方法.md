# 083-AQS-Interuptibly结尾方法

[TOC]

意思是不对中断做出响应,也就是线程在调用不带`Interruptibly`关键字的方法获取资源时或者获取资源失败被挂起时

- 不带 Interruptibly 的方法

  > 其他线程中断了这个线程,这个线程不会因为中断而抛出异常,它还是继续获取资源或者被挂起,也就是不对中断相应,忽略中断

- 带Interruptibly的方法

  > 中断并抛出 InterruptedException 异常然后返回

