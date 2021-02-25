# 040-Zuul内置的Filter

[TOC]

![image-20200603124318302](../../../../assets/image-20200603124318302.png)

## 



![image-20200603124328786](../../../../assets/image-20200603124328786.png)

![image-20200603124335340](../../../../assets/image-20200603124335340.png)

如果使用`@EnableZuulProxy` ,会默认开启上面的所有 Filter

如果使用`@EnableZuulServer` 将缺少`PreDecorationFilter`.`RibbonRoutingFilter`, `SimpleHostRoutingFilter`

可以通过配置文件关闭

```
zuul.<ClassName>.<FilterType>.disable=true
例如
zuul.SendErrorFilter.error.disable=true
```

