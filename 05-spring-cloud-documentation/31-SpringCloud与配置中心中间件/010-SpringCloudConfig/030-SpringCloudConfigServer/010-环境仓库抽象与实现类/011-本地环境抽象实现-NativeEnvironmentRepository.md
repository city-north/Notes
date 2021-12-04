# NativeEnvironmentRepository

[TOC]

## 一句话总结

NativeEnvironmentRepository  是文件系统 EnvironmentRepository 的实现, 存储到本地

## 详情

NativeEnvironmentRepository 生效的前提是 active profile 需要一个native profile(NativeRepositoryConfiguration)

```java
@Configuration(proxyBeanMethods = false)
@Profile("native")
class NativeRepositoryConfiguration {

	@Bean
	public NativeEnvironmentRepository nativeEnvironmentRepository(NativeEnvironmentRepositoryFactory factory,
			NativeEnvironmentProperties environmentProperties) {
		return factory.build(environmentProperties);
	}
}
```

源码: 

```java
public class NativeEnvironmentRepository implements EnvironmentRepository, SearchPathLocator, Ordered {

  @Override
	public Environment findOne(String config, String profile, String label, boolean includeOrigin) {
SpringApplicationBuilder builder = new SpringApplicationBuilder(PropertyPlaceholderAutoConfiguration.class);
		ConfigurableEnvironment environment = getEnvironment(profile);
		builder.environment(environment);
    //类型为NONE 非WEB应用
		builder.web(WebApplicationType.NONE).bannerMode(Mode.OFF);
		if (!logger.isDebugEnabled()) {
			// Make the mini-application startup less verbose
			builder.logStartupInfo(false);
		}
		String[] args = getArgs(config, profile, label);
		// Explicitly set the listeners (to exclude logging listener which would change
		// log levels in the caller)
		builder.application().setListeners(Arrays.asList(new ConfigFileApplicationListener()));

		try (ConfigurableApplicationContext context = builder.run(args)) {
			environment.getPropertySources().remove("profiles");
return clean(new PassthruEnvironmentRepository(environment).findOne(config, profile, label, includeOrigin));
		}
		catch (Exception e) {
			...//Handle Exception
		}
	}
}
```

如果客户端传递的

- application = book
- profile = prod

并在服务器存储配置如下: 

```
spring.profiles.active=navtive
spring.cloud.config.server.native.searchLocations=classpath:/
```

那么最终加载的配置文件名为 application 和 book, 配置目录是classpath:/ 和 classpath:master , 生效的profile = prod