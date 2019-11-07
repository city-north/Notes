# Bean 的自动装配 (Beans Auto-wiring)

Spring容器可以隐式地自动装配依赖项。我们可以使用@Bean注释指定自动装配模式(默认模式除外)。

```java
 @Configuration
 public class Config{
    @Bean(autowire == <xyz>)
     public ABean aBean(){
        return new ABean();
    }
   .....
 }
```

autowire 属性分为三种:

- `Autowire.NO`

- `Autowire.BY_NAME`

- `Autowire.BY_TYPE`

![img](assets/autowiring.png)

