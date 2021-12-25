# 020-SpringCloudConfig配置动态刷新阶段

[TOC]

## 前置知识点

1. RefreshEvent事件EnvironmentChangeEvent事件
   1.  [010-RefreshEvent-当RefreshEndpoint被调用时.md](..\..\..\..\02-spring-framework-documentation\016-Spring事件\010-SpringBoot和SpringCloud中的事件\010-RefreshEvent-当RefreshEndpoint被调用时.md) 
   2.  [020-EnvironmentChangeEvent-当Environment配置属性发生变化时.md](..\..\..\..\02-spring-framework-documentation\016-Spring事件\010-SpringBoot和SpringCloud中的事件\020-EnvironmentChangeEvent-当Environment配置属性发生变化时.md) 
2. @RefreshScope
3. @ConfigurationProperties
4. 配置动态刷新原理

## 配置动态刷新原理

- 通过Environment获取的配置, 比如 env.getProperties("book.category")
- 被 @ConfigurationProperties 注解修饰的配置类, 这些配置类生效的原因是触发了EnvironmentChangeEvent事件
- 当@RefreshScope注解修饰的类, 这些scope值为refresh的类初始化时, 经过了特殊处理, 当触发refreshEvent事件后会重新构造

## NacosContextRefresher

```java
public class NacosContextRefresher implements ApplicationListener<ApplicationReadyEvent>, ApplicationContextAware {

		....

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		// many Spring context状态判断!防止注册两侧
		if (this.ready.compareAndSet(false, true)) {
      //注册Nacos Listeners
			this.registerNacosListenersForApplications();
		}
	}

	/**
	 * register Nacos Listeners.
	 */
	private void registerNacosListenersForApplications() {
		if (isRefreshEnabled()) {
      //遍历所有的Nacos配置项(dataId和 group组成一个配置项)
			for (NacosPropertySource propertySource : NacosPropertySourceRepository.getAll()) {
				if (!propertySource.isRefreshable()) {
          //如果不可以刷新
					continue;
				}
				String dataId = propertySource.getDataId();
        //注册
				registerNacosListener(propertySource.getGroup(), dataId);
			}
		}
	}

	private void registerNacosListener(final String groupKey, final String dataKey) {
		String key = NacosPropertySourceRepository.getMapKey(dataKey, groupKey);
		Listener listener = listenerMap.computeIfAbsent(key,
				lst -> new AbstractSharedListener() {
					@Override
					public void innerReceive(String dataId, String group, String configInfo) {
						refreshCountIncrement();
						nacosRefreshHistory.addRefreshRecord(dataId, group, configInfo);
						// todo feature: support single refresh for listening
            //监听器每次收到Nacos配置更新的推送事件后都会发送RefreshEvent配置刷新事件
						applicationContext.publishEvent(new RefreshEvent(this, null, "Refresh Nacos config"));
						if (log.isDebugEnabled()) {
	log.debug(String.format("Refresh Nacos config group=%s,dataId=%s,configInfo=%s",group, dataId, configInfo));
						}
					}
				});
		try {
      //为Nacos配置项添加监听器
			configService.addListener(dataKey, groupKey, listener);
		}
		catch (NacosException e) {
			log.warn(String.format(
					"register fail for nacos listener ,dataId=[%s],group=[%s]", dataKey,
					groupKey), e);
		}
	}

...
}

```

## 被 @ConfigurationProperties 注解修饰的配置类, 这些配置类生效的原因是触发了EnvironmentChangeEvent事件

@ConfigurationProperties 通过 ConfigurationPropertiesRebinder (一个 `EnvironmentChangeEvent`事件的监听器)

```java
@Component
@ManagedResource
public class ConfigurationPropertiesRebinder implements ApplicationContextAware, ApplicationListener<EnvironmentChangeEvent> {
  
  //维护着所有被@ConfiguratoonProperties修饰的Bean
	private ConfigurationPropertiesBeans beans;

	@ManagedOperation
	public void rebind() {
		this.errors.clear();
    //找到所有 ConfigurationPropertiesBeans
		for (String name : this.beans.getBeanNames()) {
			rebind(name);
		}
	}

	@ManagedOperation
	public boolean rebind(String name) {
		if (!this.beans.getBeanNames().contains(name)) {
			return false;
		}
		if (this.applicationContext != null) {
			try {
				Object bean = this.applicationContext.getBean(name);
				if (AopUtils.isAopProxy(bean)) {
					bean = ProxyUtils.getTargetObject(bean);
				}
				if (bean != null) {
					// TODO: determine a more general approach to fix this.
					// see https://github.com/spring-cloud/spring-cloud-commons/issues/571
          //如果不是可刷新的
					if (getNeverRefreshable().contains(bean.getClass().getName())) {
						return false; // ignore
					}
          //销毁掉
					this.applicationContext.getAutowireCapableBeanFactory().destroyBean(bean);
          //重新创建
					this.applicationContext.getAutowireCapableBeanFactory().initializeBean(bean, name);
					return true;
				}
			}
			catch (RuntimeException e) {
				this.errors.put(name, e);
				throw e;
			}
			catch (Exception e) {
				this.errors.put(name, e);
				throw new IllegalStateException("Cannot rebind to " + name, e);
			}
		}
		return false;
	}

  //触发监听
	@Override
	public void onApplicationEvent(EnvironmentChangeEvent event) {
		if (this.applicationContext.equals(event.getSource())
				// Backwards compatible
				|| event.getKeys().equals(event.getSource())) {
			rebind();
		}
	}

}

```

