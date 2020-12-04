# 160-SpringBean销毁阶段

## 三个销毁阶段

- CommonAnnotationBeanPostProcessor(注解驱动)用于解析@PostDestory
- 调用接口 DisposableBean的destroy回调
- 调用xml中自定义的回调

 [150-SpringBean销毁前阶段.md](150-SpringBean销毁前阶段.md) 

