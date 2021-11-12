# 010-SpringCloudConfig配置初始化阶段

[TOC]

## 一言蔽之

- 使用配置中心必须先对配置中心的地址进行配置, 这个地址需要在配置文件里面配置
- SpringCloudBootstrap 阶段优先级高, 会先读取配置中心的配置, 这些配置在下一次正常的ApplicationContext启动时使用

SpringCloud Bootstrap 阶段的概念: 

- 在Bootstrap阶段会构造一个 Bootstrap ApplicationContext , 这个 ApplicatoonContext 加载配置的过程会基于bootstrap.properties  或者 bootstrap.yml 文件 去加载文件
- SpringCloud 在加载文件时, 会有一套机制 (PropertySourceLocator接口的定义)来构建数据源 PropertySources
- Bootstrap 阶段构造的ApplicationContext会作为正常阶段的 ApplicationContext 的父(parent), 读取顺序是先读取子, 再读取父

## Bootstrap配置的优先级

我们使用配置中心前必须先对配置中心的地址进行配置, 这个地址需要在配置文件内定义, 

Spring Cloud Bootstrap优先级别高, 会先读取配置中心的配置, 这些配置, 用于加载下一个 ApplicationContext 时使用

## Bootstrap初始化阶段

#### 一言蔽之

1. spring-cloud-context 模块内部的 META-INF/spring.factory 添加一个 BootstrapApplicationListener (实现了ApplicationListener接口), 
2. 用于监听 ApplicationEnvironmentPreparedEvent事件(Environment 刚创建, 但是ApplicationContext还未创建时触发的事件)
3. 收到该事件后进入Bootstrap阶段, 从配置中心加载配置

#### 监听ApplicationEnvironmentPreparedEvent事件

```java
org.springframework.boot.SpringApplication#prepareEnvironment

	private ConfigurableEnvironment prepareEnvironment(SpringApplicationRunListeners listeners,
			DefaultBootstrapContext bootstrapContext, ApplicationArguments applicationArguments) {
		// Create and configure the environment
  // 创建环境并配置环境
		ConfigurableEnvironment environment = getOrCreateEnvironment();
		configureEnvironment(environment, applicationArguments.getSourceArgs());
		ConfigurationPropertySources.attach(environment);
  //通知所有的监听器, 环境准备好了, 
		listeners.environmentPrepared(bootstrapContext, environment);
		DefaultPropertiesPropertySource.moveToEnd(environment);
		configureAdditionalProfiles(environment);
		bindToSpringApplication(environment);
		if (!this.isCustomEnvironment) {
			environment = new EnvironmentConverter(getClassLoader()).convertEnvironmentIfNecessary(environment,
					deduceEnvironmentClass());
		}
		ConfigurationPropertySources.attach(environment);
		return environment;
	}
```

值得注意的是

1. spring-cloud-context模块内部的 META-INF/spring.factoies 添加了一个BootstrapApplicationListener

![d](assets/image-20210909215511053.png)

当环境初始化好后广播`ApplicationEnvironmentPreparedEvent`事件

![image-20210909215854060](assets/image-20210909215854060.png)

## 监听具体逻辑

```java
@Override
public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
   ConfigurableEnvironment environment = event.getEnvironment();
  //判断是否启用Bootstrap , 配置文件 spring.cloud.bootstrap.enabled 可以关闭
   if (!bootstrapEnabled(environment) && !useLegacyProcessing(environment)) {
      return;
   }
   // don't listen to events in a bootstrap context
  // 2
   if (environment.getPropertySources().contains(BOOTSTRAP_PROPERTY_SOURCE_NAME)) {
     //防止遍历两次
      return;
   }
   ConfigurableApplicationContext context = null;
   String configName = environment.resolvePlaceholders("${spring.cloud.bootstrap.name:bootstrap}");
   for (ApplicationContextInitializer<?> initializer : event.getSpringApplication().getInitializers()) {
      if (initializer instanceof ParentContextApplicationContextInitializer) {
         context = findBootstrapContext((ParentContextApplicationContextInitializer) initializer, configName);
        //遍历 ApplicationContextInitializer 初始化器, 只执行 ParentContextApplicationContextInitializer 
        // 有的开发者可能会用到单层甚至多层父子关系, 并且手动设置bootstrapApplication 作为parent
        // SpringApplicationBuilder#parent(ConfiguableApplicationContext), 其目的就是找到已经构造的boostrap ApplicationContext
      }
   }
   if (context == null) {
       //5
      context = bootstrapServiceContext(environment, event.getSpringApplication(), configName);
       //6 
      event.getSpringApplication().addListeners(new CloseContextOnFailureApplicationListener(context));
   }

   apply(context, event.getSpringApplication(), environment);
}
```



- 2 处的代码.防止经理两边Bootstrap阶段, Bootstrap阶段执行的时候, 会添加一个名称为 bootstrap 的` PropertySource`
- 5处代码， 如果没有找到bootstrap ApplicationContext 则调用boostrapServiceContext 方法构建 bootstrap ApplicationContext
- 6处代码， 给ApplicationCOntext添加一些额外的CloseContextOnFailureApplicationListener， 目的是为了子ApplicationContext启动时关闭父ApplicationContext

## 构造bootstrapServiceContext

bootstrapServiceContext 方法的内容是构建bootstrap ApplicationContext

1.  这里先构造出一个 StandardEnvoronment（默认机上系统参数和启动参数的PropertySource数据源)，然后删除所有内部的数据， 子ApplicationContext也有系统参数和启动参数的数据源， 所以这里删除重复的数据源， 后续在添加新的数据源， 这个Evironment会作为bootstrapApplicationContext的Environment



```java
	private ConfigurableApplicationContext bootstrapServiceContext(ConfigurableEnvironment environment,
			final SpringApplication application, String configName) {
        //1
		StandardEnvironment bootstrapEnvironment = new StandardEnvironment();
		MutablePropertySources bootstrapProperties = bootstrapEnvironment.getPropertySources();
		for (PropertySource<?> source : bootstrapProperties) {
			bootstrapProperties.remove(source.getName());
		}
		String configLocation = environment.resolvePlaceholders("${spring.cloud.bootstrap.location:}");
		String configAdditionalLocation = environment
				.resolvePlaceholders("${spring.cloud.bootstrap.additional-location:}");
		Map<String, Object> bootstrapMap = new HashMap<>();
		bootstrapMap.put("spring.config.name", configName);
		// if an app (or test) uses spring.main.web-application-type=reactive, bootstrap
		// will fail
		// force the environment to use none, because if though it is set below in the
		// builder
		// the environment overrides it
		bootstrapMap.put("spring.main.web-application-type", "none");
		if (StringUtils.hasText(configLocation)) {
			bootstrapMap.put("spring.config.location", configLocation);
		}
		if (StringUtils.hasText(configAdditionalLocation)) {
			bootstrapMap.put("spring.config.additional-location", configAdditionalLocation);
		}
    //添加一个名称为bootstrap 的PropertySource到 bootstrap ApplicationContext
		bootstrapProperties.addFirst(new MapPropertySource(BOOTSTRAP_PROPERTY_SOURCE_NAME, bootstrapMap));
		for (PropertySource<?> source : environment.getPropertySources()) {
			if (source instanceof StubPropertySource) {
				continue;
			}
			bootstrapProperties.addLast(source);
		}
		// TODO: is it possible or sensible to share a ResourceLoader?
        //构造SpringApplicationContextBuilder， 后续偶见出的ApplicationCOntext就是BootstrapApplicationContext, bootstrap ApplicationContext关闭了banner打印的开关
		SpringApplicationBuilder builder = new SpringApplicationBuilder().profiles(environment.getActiveProfiles())
				.bannerMode(Mode.OFF).environment(bootstrapEnvironment)
				// Don't use the default properties in this builder
           //不注册钩子， 因为这些已经在子上下文里做， 不是 bootstrap 上下文的目的
				.registerShutdownHook(false).logStartupInfo(false).web(WebApplicationType.NONE);
		final SpringApplication builderApplication = builder.application();
		if (builderApplication.getMainApplicationClass() == null) {
			// gh_425:
			// SpringApplication cannot deduce the MainApplicationClass here
			// if it is booted from SpringBootServletInitializer due to the
			// absense of the "main" method in stackTraces.
			// But luckily this method's second parameter "application" here
			// carries the real MainApplicationClass which has been explicitly
			// set by SpringBootServletInitializer itself already.
			builder.main(application.getMainApplicationClass());
		}
		if (environment.getPropertySources().contains("refreshArgs")) {
			// If we are doing a context refresh, really we only want to refresh the
			// Environment, and there are some toxic listeners (like the
			// LoggingApplicationListener) that affect global static state, so we need a
			// way to switch those off.
			builderApplication.setListeners(filterListeners(builderApplication.getListeners()));
		}
    //注册 BootstrapImportSelectorConfiguration 配置类, 这个配置类非常关键, 它会使用工厂加载机制找出key 为
// 找出所有的 org.springframework.cloud.bootstrap.BootstrapConfiguration
		builder.sources(BootstrapImportSelectorConfiguration.class);
		final ConfigurableApplicationContext context = builder.run();
		// gh-214 using spring.application.name=bootstrap to set the context id via
		// `ContextIdApplicationContextInitializer` prevents apps from getting the actual
		// spring.application.name
		// during the bootstrap phase.
		context.setId("bootstrap");
		// Make the bootstrap context a parent of the app context
        //为子上下文添加 AncestorInitializer， 如果子上下文已经存在 AncestorInitialzer, 则建立关系， 内部建立的是父子关系
		addAncestorInitializer(application, context);
		// It only has properties in it now that we don't want in the parent so remove
		// it (and it will be added back later)
		bootstrapProperties.remove(BOOTSTRAP_PROPERTY_SOURCE_NAME);
		mergeDefaultProperties(environment.getPropertySources(), bootstrapProperties);
		return context;
	}
```

## BootstrapImportSelectorConfiguration :找出所有的BootstrapConfiguration

spring-cloud-config-client-3.0.3.jar

```properties
org.springframework.cloud.bootstrap.BootstrapConfiguration=\
org.springframework.cloud.config.client.ConfigServiceBootstrapConfiguration,\
org.springframework.cloud.config.client.DiscoveryClientConfigServiceBootstrapConfiguration
```

spring-cloud-context-3.0.2.jar

```properties
# Spring Cloud Bootstrap components
org.springframework.cloud.bootstrap.BootstrapConfiguration=\
org.springframework.cloud.bootstrap.config.PropertySourceBootstrapConfiguration,\
org.springframework.cloud.bootstrap.encrypt.EncryptionBootstrapConfiguration,\
org.springframework.cloud.autoconfigure.ConfigurationPropertiesRebinderAutoConfiguration,\
org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration
```

- PropertySourceBootstrapConfiguration : 内部会获取 PropertySourceLocator列表 用于架子配置中心的配置, 并封装到PropertySource, 之后将列表添加到Envoronment 里的PropertySource

## PropertySourceBootstrapConfiguration

使用PropertySourceLocator找到对应的PropertySource后并加入的Envoronment

```java
	private List<PropertySourceLocator> propertySourceLocators = new ArrayList<>();

@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		List<PropertySource<?>> composite = new ArrayList<>();
		AnnotationAwareOrderComparator.sort(this.propertySourceLocators);
		boolean empty = true;
		ConfigurableEnvironment environment = applicationContext.getEnvironment();
		for (PropertySourceLocator locator : this.propertySourceLocators) {
      //调用后产生PropertySource
			Collection<PropertySource<?>> source = locator.locateCollection(environment);
			if (source == null || source.size() == 0) {
				continue;
			}
			List<PropertySource<?>> sourceList = new ArrayList<>();
			for (PropertySource<?> p : source) {
				if (p instanceof EnumerablePropertySource) {
					EnumerablePropertySource<?> enumerable = (EnumerablePropertySource<?>) p;
					sourceList.add(new BootstrapPropertySource<>(enumerable));
				}
				else {
					sourceList.add(new SimpleBootstrapPropertySource(p));
				}
			}
			logger.info("Located property source: " + sourceList);
			composite.addAll(sourceList);
			empty = false;
		}
		if (!empty) {
			MutablePropertySources propertySources = environment.getPropertySources();
			String logConfig = environment.resolvePlaceholders("${logging.config:}");
			LogFile logFile = LogFile.get(environment);
			for (PropertySource<?> p : environment.getPropertySources()) {
				if (p.getName().startsWith(BOOTSTRAP_PROPERTY_SOURCE_NAME)) {
          //先移除
					propertySources.remove(p.getName());
				}
			}
			insertPropertySources(propertySources, composite);
			reinitializeLoggingSystem(environment, logConfig, logFile);
			setLogLevels(applicationContext, environment);
			handleIncludedProfiles(environment);
		}
	}
```

## 图示

![image-20210909225501340](assets/image-20210909225501340.png)



- NacosPropertySourceLocator : Nacos实现
- ConfigServicePropertySourceLocator : SpringCloudConifg