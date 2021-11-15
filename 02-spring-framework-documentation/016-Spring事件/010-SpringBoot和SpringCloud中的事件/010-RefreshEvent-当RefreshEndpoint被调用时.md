# 010-RefreshEvent-当RefreshEndpoint被调用时 

[TOC]

## 一言蔽之

配置刷新事件，接收到这个事件时会构造一个临时的ApplicationContext（会加上BootstrapApplicationListener和 ConfigFileApplicationListener）意味着从配置中心和配置文件中重新获取配置数据，构建完毕后新的Environment里的PropertySource会跟原来的Environment里的PropertySource进行对比覆盖

## 实例

bootstrap.properties添加配置

```properties
book.author=jim
```

 ```java
 @RestController
 class ConfigurationController {
 
   @GetMapping("/config")
   public String config() {
     StringBuilder sb = new StringBuilder();
     sb.append("env.get('book.category')=" + applicationContext.getEnvironment()
               .getProperty("book.category", "unknown"))
       .append("<br/>env.get('book.author')=" + applicationContext.getEnvironment()
               .getProperty("book.author", "unknown"));
     return sb.toString();
   }
 }
 
 @Component
 class EventReceiver implements ApplicationListener<EnvironmentChangeEvent> {
 
   @Override
   public void onApplicationEvent(ApplicationEvent event) {
     System.out.println(((EnvironmentChangeEvent)event).getKeys());
 }
 ```

#### 发布RefreshEvent事件

```java
@RestController
class EventController {

  @Autowired
  ApplicationContext applicationContext;

  @GetMapping("/event")
  public String event() {
    applicationContext.publishEvent(new RefreshEvent(this, null, "just for test"));
    return "send RefreshEvent";
  }
}
```

#### 更新前的数据

```java
env.get('book.ctegory') = spring cloud
env.get('book.author') = jim
```

去target/boostrtap.properties里修改后

```properties
book.author=jim 2
```

#### 更新后的数据

console打印

```
[book.author]
```

html输出

```
env.get('book.ctegory') = spring cloud
env.get('book.author') = jim 2
```

## 结论

修改本地文件后, 触发RefreshEvent事件后, Environment的配置是更新后的数据
