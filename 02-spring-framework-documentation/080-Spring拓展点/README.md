# Spring拓展功能

我们说,refresh函数包含了几乎ApplicationContext 的所有功能,而且逻辑非常清晰

1. [初始化优化前的准备工作,例如对系统属性或者环境变量进行准备和验证](#初始化优化前的准备工作,例如对系统属性或者环境变量进行准备和验证)

   ```java
   //1、调用容器准备刷新的方法，获取容器的当时时间，同时给容器设置同步标识
   prepareRefresh();
   ```

2. [初始化BeanFactory,并进行XML文件读取](#初始化BeanFactory,并进行XML文件读取)

   ```java
   //2、告诉子类启动refreshBeanFactory()方法，Bean定义资源文件的载入从
   //子类的refreshBeanFactory()方法启动
   ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();
   ```

3. [对BeanFactory进行各种功能填充](#对BeanFactory进行各种功能填充)

   ```java
   //3、为BeanFactory配置容器特性，例如类加载器、事件处理器等
   prepareBeanFactory(beanFactory);
   ```

4. [子类覆盖方法做额外的处理](#子类覆盖方法做额外的处理)

   ```java
   //4、为容器的某些子类指定特殊的BeanPost事件处理器
   postProcessBeanFactory(beanFactory);
   ```

5. [激活各种BeanFactory处理器](#激活各种BeanFactory处理器)

   ```java
   //5、调用所有注册的BeanFactoryPostProcessor的Bean
   invokeBeanFactoryPostProcessors(beanFactory);
   ```

6. 注册拦截bean创建的bean处理器，这里只是注册，真正的调用是在getBean时候

   ```java
   //6、为BeanFactory注册BeanPost事件处理器.
   //BeanPostProcessor是Bean后置处理器，用于监听容器触发的事件
   registerBeanPostProcessors(beanFactory);
   ```

7. 为上下文初始化Message源，即对不同语言的消息体进行国际化处理

   ```java
   //7、初始化信息源，和国际化相关.
   initMessageSource();
   ```

8. 初始化应用消息广播器，并放入“applicationEventMulticaster”bean中。

   ```java
   //8、初始化容器事件传播器.
   initApplicationEventMulticaster();
   ```

9. 留给子类来初始化其他的bean。

   ```java
   //9、调用子类的某些特殊Bean初始化方法
   onRefresh();
   ```

10. 在所有注册的bean中查找listener bean，注册到消息广播器中。

    ```java
    //10、为事件传播器注册事件监听器.
    registerListeners();
    ```

11. 初始化剩下的单实例（非惰性的）。

    ```java
    //11、初始化所有剩余的单例Bean
    finishBeanFactoryInitialization(beanFactory);
    ```

12. 完成刷新过程，通知生命周期处理器lifecycleProcessor刷新过程，同时发出ContextRefreshEvent通知别人。

    ```java
    //-----调用 InitializingBean
    //12、初始化容器的生命周期事件处理器，并发布容器的生命周期事件
    finishRefresh();
    ```

    

```java
	@Override
	public void refresh() throws BeansException, IllegalStateException {
		synchronized (this.startupShutdownMonitor) {
			//1、调用容器准备刷新的方法，获取容器的当时时间，同时给容器设置同步标识
			prepareRefresh();

			//2、告诉子类启动refreshBeanFactory()方法，Bean定义资源文件的载入从
			//子类的refreshBeanFactory()方法启动
			ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();
      //----到这已经初始化好了Bean

			//3、为BeanFactory配置容器特性，例如类加载器、事件处理器等
			prepareBeanFactory(beanFactory);

			try {
				//4、为容器的某些子类指定特殊的BeanPost事件处理器
				postProcessBeanFactory(beanFactory);

				//5、调用所有注册的BeanFactoryPostProcessor的Bean
				invokeBeanFactoryPostProcessors(beanFactory);

				//6、为BeanFactory注册BeanPost事件处理器.
				//BeanPostProcessor是Bean后置处理器，用于监听容器触发的事件
				registerBeanPostProcessors(beanFactory);

				//7、初始化信息源，和国际化相关.
				initMessageSource();

				//8、初始化容器事件传播器.
				initApplicationEventMulticaster();

				//9、调用子类的某些特殊Bean初始化方法
				onRefresh();

				//10、为事件传播器注册事件监听器.
				registerListeners();

				//11、初始化所有剩余的单例Bean
				finishBeanFactoryInitialization(beanFactory);
        	//-----调用 InitializingBean

				//12、初始化容器的生命周期事件处理器，并发布容器的生命周期事件
				finishRefresh();
			}

			catch (BeansException ex) {
				if (logger.isWarnEnabled()) {
					logger.warn("Exception encountered during context initialization - " +
							"cancelling refresh attempt: " + ex);
				}
				//13、销毁已创建的Bean
				destroyBeans();

				//14、取消refresh操作，重置容器的同步标识。
				cancelRefresh(ex);

				throw ex;
			}
			finally {
				//15、重设公共缓存
				resetCommonCaches();
			}
		}
	}
```

## 初始化优化前的准备工作,例如对系统属性或者环境变量进行准备和验证

在某种情况下项目的使用需要读取某些系统变量，而这个变量的设置很可能会影响着系统的正确性，那么ClassPathXmlApplicationContext为我们提供的这个准备函数就显得非常必要，它可以在Spring启动的时候提前对必需的变量进行存在性验证

 [010-拓展点-环境准备.md](010-拓展点-环境准备.md) 

## 初始化BeanFactory,并进行XML文件读取

之前有提到ClassPathXmlApplicationContext包含着BeanFactory所提供的一切特征，那么在这一步骤中将会复用BeanFactory中的配置文件读取解析及其他功能，这一步之后，ClassPathXmlApplicationContext实际上就已经包含了BeanFactory所提供的功能，也就是可以进行bean的提取等基础操作了。

 [020-拓展点-加载BeanFactory.md](020-拓展点-加载BeanFactory.md) 







## 对BeanFactory进行各种功能填充

@Qualifier与@Autowired应该是大家非常熟悉的注解，那么这两个注解正是在这一步骤中增加的支持。

## 子类覆盖方法做额外的处理

Spring之所以强大，为世人所推崇，除了它功能上为大家提供了便例外，还有一方面是它的完美架构，开放式的架构让使用它的程序员很容易根据业务需要扩展已经存在的功能。这种开放式的设计在Spring中随处可见，例如在本例中就提供了一个空的函数实现postProcess- BeanFactory来方便程序员在业务上做进一步扩展。


## 激活各种BeanFactory处理器