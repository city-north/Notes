# 100-第十步-registerListeners-注册事件监听器

![image-20201007151953236](../../assets/image-20201007151953236.png)

#### 注册监听器

之前在介绍Spring的广播器时反复提到了事件监听器，那么在Spring注册监听器的时候又做了哪些逻辑操作呢？

```java
protected void registerListeners() {
         //硬编码方式注册的监听器处理
         for (ApplicationListener<?> listener : getApplicationListeners()) {
             getApplicationEventMulticaster().addApplicationListener(listener);
         }
         //配置文件注册的监听器处理
         String[] listenerBeanNames = getBeanNamesForType(ApplicationListener.class,   
true, false);
         for (String lisName : listenerBeanNames) {
             getApplicationEventMulticaster().addApplicationListenerBean(lisName);
         }
}
```

