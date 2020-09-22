# Spring中管理Bean依赖的方式

## 目录

- [AbstractAutoWireCapabIeBeanFactory对Bean实例对象进行属性依赖注入](#AbstractAutoWireCapabIeBeanFactory对Bean实例对象进行属性依赖注入)
- [SpringIoC容器根据Bean名称或者类型进行autowiring自动属性依赖注入](#SpringIoC容器根据Bean名称或者类型进行autowiring自动属性依赖注入)
- [DefauItSingIetonBeanRegistry的registerDependentBean()方法实现属性依赖注入](#DefauItSingIetonBeanRegistry的registerDependentBean()方法实现属性依赖注入)

---

Spring IoC容器提供了两种管理Bean依赖关系的方式：

- 显式管理：通过BeanDefinition的属性值和构造方法实现Bean依赖关系管理。
- autowiring：Spring IoC容器有依赖自动装配功能，不需要对Bean属性的依赖关系做显式的声明，只需要配置好autowiring属性，IoC容器会自动使用反射查找属性的类型和名称，然后基于属性的类型或者名称来自动匹配容器中的Bean，从而自动完成依赖注入。

容器对Bean的自动装配发生在容器对Bean依赖注入的过程中。在对Spring IoC容器的依赖注入源码进行分析时，我们已经知道容器对 Bean 实例对象的依赖属性注入发生在AbstractAutoWireCapableBeanFactory类的populateBean（）方法中，下面通过程序流程分析autowiring的实现原理。

## AbstractAutoWireCapabIeBeanFactory对Bean实例对象进行属性依赖注入

应用程序第一次通过getBean（）方法（配置了lazy-init预实例化属性的除外）向IoC容器索取Bean时，容器创建Bean实例对象，并且对Bean实例对象进行属性依赖注入，AbstractAutoWireCapableBeanFactory的populateBean方法就实现了属性依赖注入的功能，其主要源码如下：

```java
//将Bean属性设置到生成的实例对象上
protected void populateBean(String beanName, RootBeanDefinition mbd, @Nullable BeanWrapper bw) {
		//获取容器在解析Bean定义资源时为BeanDefiniton中设置的属性值
		PropertyValues pvs = (mbd.hasPropertyValues() ? mbd.getPropertyValues() : null);
//对依赖注入处理，首先处理autowiring自动装配的依赖注入
		if (mbd.getResolvedAutowireMode() == RootBeanDefinition.AUTOWIRE_BY_NAME ||
				mbd.getResolvedAutowireMode() == RootBeanDefinition.AUTOWIRE_BY_TYPE) {
			MutablePropertyValues newPvs = new MutablePropertyValues(pvs);

			// Add property values based on autowire by name if applicable.
			//根据Bean名称进行autowiring自动装配处理
			if (mbd.getResolvedAutowireMode() == RootBeanDefinition.AUTOWIRE_BY_NAME) {
				autowireByName(beanName, mbd, bw, newPvs);
			}

			// Add property values based on autowire by type if applicable.
			//根据Bean类型进行autowiring自动装配处理
			if (mbd.getResolvedAutowireMode() == RootBeanDefinition.AUTOWIRE_BY_TYPE) {
				autowireByType(beanName, mbd, bw, newPvs);
			}

			pvs = newPvs;
		}

		//对非autowiring的属性进行依赖注入处理
}
```

## SpringIoC容器根据Bean名称或者类型进行autowiring自动属性依赖注入

Spring IoC容器根据Bean名称或者类型进行autowiring自动属性依赖注入的重要代码如下：

```java
//根据类型对属性进行自动依赖注入
protected void autowireByType(
      String beanName, AbstractBeanDefinition mbd, BeanWrapper bw, MutablePropertyValues pvs) {

   //获取用户定义的类型转换器
   TypeConverter converter = getCustomTypeConverter();
   if (converter == null) {
      converter = bw;
   }

   //存放解析的要注入的属性
   Set<String> autowiredBeanNames = new LinkedHashSet<>(4);
   //对Bean对象中非简单属性(不是简单继承的对象，如8中原始类型，字符
   //URL等都是简单属性)进行处理
   String[] propertyNames = unsatisfiedNonSimpleProperties(mbd, bw);
   for (String propertyName : propertyNames) {
      try {
         //获取指定属性名称的属性描述器
         PropertyDescriptor pd = bw.getPropertyDescriptor(propertyName);
         // Don't try autowiring by type for type Object: never makes sense,
         // even if it technically is a unsatisfied, non-simple property.
         //不对Object类型的属性进行autowiring自动依赖注入
         if (Object.class != pd.getPropertyType()) {
            //获取属性的setter方法
            MethodParameter methodParam = BeanUtils.getWriteMethodParameter(pd);
            // Do not allow eager init for type matching in case of a prioritized post-processor.
            //检查指定类型是否可以被转换为目标对象的类型
            boolean eager = !PriorityOrdered.class.isInstance(bw.getWrappedInstance());
            //创建一个要被注入的依赖描述
            DependencyDescriptor desc = new AutowireByTypeDependencyDescriptor(methodParam, eager);
            //根据容器的Bean定义解析依赖关系，返回所有要被注入的Bean对象
            Object autowiredArgument = resolveDependency(desc, beanName, autowiredBeanNames, converter);
            if (autowiredArgument != null) {
               //为属性赋值所引用的对象
               pvs.add(propertyName, autowiredArgument);
            }
            for (String autowiredBeanName : autowiredBeanNames) {
               //指定名称属性注册依赖Bean名称，进行属性依赖注入
               registerDependentBean(autowiredBeanName, beanName);
               if (logger.isDebugEnabled()) {
                  logger.debug("Autowiring by type from bean name '" + beanName + "' via property '" +
                        propertyName + "' to bean named '" + autowiredBeanName + "'");
               }
            }
            //释放已自动注入的属性
            autowiredBeanNames.clear();
         }
      }
      catch (BeansException ex) {
         throw new UnsatisfiedDependencyException(mbd.getResourceDescription(), beanName, propertyName, ex);
      }
   }
}
```

#### DefauItSingIetonBeanRegistry的registerDependentBean()方法实现属性依赖注入

DefaultSingletonBeanRegistry 的 registerDependentBean（）方法实现属性依赖注入的重要代码如下：

```java
	//为指定的Bean注入依赖的Bean
	public void registerDependentBean(String beanName, String dependentBeanName) {
		// A quick check for an existing entry upfront, avoiding synchronization...
		//处理Bean名称，将别名转换为规范的Bean名称
		String canonicalName = canonicalName(beanName);
		Set<String> dependentBeans = this.dependentBeanMap.get(canonicalName);
		if (dependentBeans != null && dependentBeans.contains(dependentBeanName)) {
			return;
		}

		// No entry yet -> fully synchronized manipulation of the dependentBeans Set
		//多线程同步，保证容器内数据的一致性
		//先从容器中：bean名称-->全部依赖Bean名称集合找查找给定名称Bean的依赖Bean
		synchronized (this.dependentBeanMap) {
			//获取给定名称Bean的所有依赖Bean名称
			dependentBeans = this.dependentBeanMap.get(canonicalName);
			if (dependentBeans == null) {
				//为Bean设置依赖Bean信息
				dependentBeans = new LinkedHashSet<>(8);
				this.dependentBeanMap.put(canonicalName, dependentBeans);
			}
			//向容器中：bean名称-->全部依赖Bean名称集合添加Bean的依赖信息
			//即，将Bean所依赖的Bean添加到容器的集合中
			dependentBeans.add(dependentBeanName);
		}
		//从容器中：bean名称-->指定名称Bean的依赖Bean集合找查找给定名称Bean的依赖Bean
		synchronized (this.dependenciesForBeanMap) {
			Set<String> dependenciesForBean = this.dependenciesForBeanMap.get(dependentBeanName);
			if (dependenciesForBean == null) {
				dependenciesForBean = new LinkedHashSet<>(8);
				this.dependenciesForBeanMap.put(dependentBeanName, dependenciesForBean);
			}
			//向容器中：bean名称-->指定Bean的依赖Bean名称集合添加Bean的依赖信息
			//即，将Bean所依赖的Bean添加到容器的集合中
			dependenciesForBean.add(canonicalName);
		}
	}
```

Autowiring的实现流程如下

- 对Bean的属性调用getBean()方法，完成依赖Bean的初始化和依赖注入。
- 将依赖Bean的属性引用设置到被依赖的Bean属性上。
- 将依赖Bean的名称和被依赖Bean的名称存储在IoC容器的集合中。

Spring IoC容器的autowiring自动属性依赖注入是一个很方便的特性，可以简化开发配置，但是凡事都有两面性，自动属性依赖注入也有不足：首先，Bean的依赖关系在配置文件中无法很清楚地看出来，会给维护造成一定的困难；其次，由于自动属性依赖注入是Spring容器自动执行的，容器是不会智能判断的，如果配置不当，将会带来无法预料的后果。所以在使用自动属性依赖注入时需要综合考虑。

