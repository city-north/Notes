# 140-Spring应用上下文关闭阶段-close()

[TOC]

## Spring应用上下文关闭阶段做了什么?

- AbstractApplicationContext#close()方法
  - 状态标识
    - active(false)
    - closed(true)
  - Live Beans JMX 撤销托管
    - LiveBeansView.unregisterApplicationContext(ConfigurableApplicationContext)
  - 发布Spring应用上下文已关闭事件-ContextClosedEvent
  - 关闭LifecycleProcessor
    - 依赖查找- Lifecycle Beans
    - 停止Lifecycle Beans
  - 销毁 Spring Beans
  - 关闭 BeanFactory
  - 回调 onClose()
  - 注册 Shutdown Hook 线程(如果曾注册)

## 源码分析

```java
@Override
public void close() {
  synchronized (this.startupShutdownMonitor) {
    doClose();
    // If we registered a JVM shutdown hook, we don't need it anymore now:
    // We've already explicitly closed the context.
    if (this.shutdownHook != null) {
      try {
        //移除已经注册的一个JVM 的shutdownHook
        Runtime.getRuntime().removeShutdownHook(this.shutdownHook);
      }
      catch (IllegalStateException ex) {
        // ignore - VM is already shutting down
      }
    }
  }
}
```

#### doClose方法

Live Beans JMX 撤销托管

- LiveBeansView.unregisterApplicationContext(ConfigurableApplicationContext)

```java
	protected void doClose() {
		// Check whether an actual close attempt is necessary...
		if (this.active.get() && this.closed.compareAndSet(false, true)) {
			if (logger.isDebugEnabled()) {
				logger.debug("Closing " + this);
			}
			//Live Beans JMX 撤销托管
			LiveBeansView.unregisterApplicationContext(this);

			try {
				// Publish shutdown event.
//        发布Spring应用上下文已关闭事件-ContextClosedEvent
				publishEvent(new ContextClosedEvent(this));
			}
			catch (Throwable ex) {
				logger.warn("Exception thrown from ApplicationListener handling ContextClosedEvent", ex);
			}

      //关闭LifecycleProcessor
			// Stop all Lifecycle beans, to avoid delays during individual destruction.
			if (this.lifecycleProcessor != null) {
				try {
					this.lifecycleProcessor.onClose();
				}
				catch (Throwable ex) {
					logger.warn("Exception thrown from LifecycleProcessor on context close", ex);
				}
			}
			//销毁 Spring Beans
			// Destroy all cached singletons in the context's BeanFactory.
			destroyBeans();

      //关闭 BeanFactory
			// Close the state of this context itself.
			closeBeanFactory();

			// Let subclasses do some final clean-up if they wish...
			onClose();

			// Reset local application listeners to pre-refresh state.
			if (this.earlyApplicationListeners != null) {
				this.applicationListeners.clear();
				this.applicationListeners.addAll(this.earlyApplicationListeners);
			}

			// Switch to inactive.
			this.active.set(false);
		}
	}
```

## destroyBeans();

销毁 Spring Beans

```java
protected void destroyBeans() {
  getBeanFactory().destroySingletons();
}
```

销毁单例Bean

```java
public void destroySingletons() {
  if (logger.isTraceEnabled()) {
    logger.trace("Destroying singletons in " + this);
  }
  synchronized (this.singletonObjects) {
    this.singletonsCurrentlyInDestruction = true;
  }

  String[] disposableBeanNames;
  synchronized (this.disposableBeans) {
    //并不是所有的Bean都实现了DisposableBean回调
    disposableBeanNames = StringUtils.toStringArray(this.disposableBeans.keySet());
  }
  for (int i = disposableBeanNames.length - 1; i >= 0; i--) {
    //销毁
    destroySingleton(disposableBeanNames[i]);
  }

  this.containedBeanMap.clear();//清空缓存
  this.dependentBeanMap.clear();
  this.dependenciesForBeanMap.clear();

  clearSingletonCache();
}
```

引用脱钩,将容器的map设置为不可达,等待jvm回收,

```java
protected void clearSingletonCache() {
   synchronized (this.singletonObjects) {
      this.singletonObjects.clear();
      this.singletonFactories.clear();
      this.earlySingletonObjects.clear();
      this.registeredSingletons.clear();
      this.singletonsCurrentlyInDestruction = false;
   }
}
```

## 使用ShutdownHook

在kill -1 优雅关闭

```java
@Override
public void registerShutdownHook() {
  if (this.shutdownHook == null) {
    // No shutdown hook registered yet.
    this.shutdownHook = new Thread(SHUTDOWN_HOOK_THREAD_NAME) {
      @Override
      public void run() {
        synchronized (startupShutdownMonitor) {
          doClose();
        }
      }
    };
    Runtime.getRuntime().addShutdownHook(this.shutdownHook);
  }
}
```

registerShutdownHook 以后,就会在结束的时候发送一个ContextClosedEvent事件

```java
public class SpringShutdownHookThreadDemo {
    public static void main(String[] args) throws IOException {
        GenericApplicationContext context = new GenericApplicationContext();

        context.addApplicationListener(new ApplicationListener<ContextClosedEvent>() {
            @Override
            public void onApplicationEvent(ContextClosedEvent event) {
                System.out.printf("[线程 %s] ContextClosedEvent 处理\n", Thread.currentThread().getName());
            }
        });

        // 刷新 Spring 应用上下文
        context.refresh();

        // 注册 Shutdown Hook
        context.registerShutdownHook();

        System.out.println("按任意键继续并且关闭 Spring 应用上下文");
        System.in.read();

        // 关闭 Spring 应用（同步）
        context.close();
    }
}
```

