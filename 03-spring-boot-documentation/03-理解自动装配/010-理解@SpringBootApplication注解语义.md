# 理解@SpringBootApplication注解语义

## 目录

- 注解代码





## 注解代码

```java
@Target(ElementType.TYPE) 
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = {
		@Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
		@Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public @interface SpringBootApplication {
```

- @SpringBootConfiguration

  > 负责标注配置类@Configuration

- @EnableAutoConfiguration

  > 负责激活SpringBoot自动装配机制

- @ComponentScan

  > 负责激活@Component扫描

## @ComponentScan

		//从SpringBoot 1.4 开始支持,用于查找BeanFactory中已经注册的TypeExclu的Filter Bean,作为代理对象
		@Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
		//从SpringBoot 1.5 开始支持,用于排除其他同时标注 @Configuration和 EnableAutoConfiguration的类
		@Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })