# 020-EnvironmentChangeEvent-当Environment配置属性发生变化时

[TOC]

## 一言蔽之

环境发生变化时， 接收到此事件表示应用里的配置数据已经发生改变， EnvironmentChangeEvent事件里维护了一个配置项Key的集合， 当配置动态刷新后， 配置值发生变化后， key会设置到事件的key集合中

- SpringCloud对 @ConfigurationProperties 注释修饰的配置类做了特殊处理, 当触发 EnvironmentChangeEvent 事件的时候, 这些配置类会进行重新绑定 (rebind) ,操作以获取最新的配置

## RefreshScope

RefreshScope的核心作用是在其修饰的类在收到 RefreshEvent事件的时候会被销毁, 再次获取这个类的时候会重新构建, 重新构建意味着重新解析表达式, 这也代表着最新的配置

 [090-SpringCloud中的@RefreshScope.md](../../007-SpringBean作用域/090-SpringCloud中的@RefreshScope.md) 

## DEMO

### 实例

bootstrap.properties添加配置

```properties
book.author=jim
```

 ```java
@RestController
@RefreshScope  //必须要加上RefreshScope, 刷新才能生效
class ConfigurationController {

  @Autowired
  ApplicationContext applicationContext;

  @Value("${book.author:unknown}")
  String bookAuthor;

  @Autowired
  BookProperties bookProperties;

  @GetMapping("/config")
  public String config() {
    StringBuilder sb = new StringBuilder();
    sb.append("env.get('book.category')=" + applicationContext.getEnvironment()
              .getProperty("book.category", "unknown"))
      .append("<br/>env.get('book.author')=" + applicationContext.getEnvironment()
              .getProperty("book.author", "unknown"))
      .append("<br/>bookAuthor=" + bookAuthor)
      .append("<br/>bookProperties=" + bookProperties);
    return sb.toString();
  }

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

- 必须要加上@RefreshScope注解, @Value 才支持动态刷新

