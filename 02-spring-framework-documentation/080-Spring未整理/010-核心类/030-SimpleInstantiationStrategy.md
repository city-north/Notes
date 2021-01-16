# SimpleInstantiationStrategy

## 目录

默认的初始化策略

- [instantiate](#instantiate) 使用初始化策略实例化Bean对象
- [instantiateWithMethodInjection](#instantiateWithMethodInjection)

## instantiate

```java
	//使用初始化策略实例化Bean对象
	@Override
	public Object instantiate(RootBeanDefinition bd, @Nullable String beanName, BeanFactory owner) {
		// Don't override the class with CGLIB if no overrides.
		//如果Bean定义中没有方法覆盖，则就不需要CGLIB父类类的方法
		if (!bd.hasMethodOverrides()) {
			Constructor<?> constructorToUse;
			synchronized (bd.constructorArgumentLock) {
				//获取对象的构造方法或工厂方法
				constructorToUse = (Constructor<?>) bd.resolvedConstructorOrFactoryMethod;
				//如果没有构造方法且没有工厂方法
				if (constructorToUse == null) {
					//使用JDK的反射机制，判断要实例化的Bean是否是接口
					final Class<?> clazz = bd.getBeanClass();
					if (clazz.isInterface()) {
						throw new BeanInstantiationException(clazz, "Specified class is an interface");
					}
					try {
						if (System.getSecurityManager() != null) {
							//这里是一个匿名内置类，使用反射机制获取Bean的构造方法
							constructorToUse = AccessController.doPrivileged(
									(PrivilegedExceptionAction<Constructor<?>>) () -> clazz.getDeclaredConstructor());
						}
						else {
							constructorToUse =	clazz.getDeclaredConstructor();
						}
						bd.resolvedConstructorOrFactoryMethod = constructorToUse;
					}
					catch (Throwable ex) {
						throw new BeanInstantiationException(clazz, "No default constructor found", ex);
					}
				}
			}
			//使用BeanUtils实例化，通过反射机制调用”构造方法.newInstance(arg)”来进行实例化
			return BeanUtils.instantiateClass(constructorToUse);
		}
		else {
			// Must generate CGLIB subclass.
			//使用CGLIB来实例化对象
			return instantiateWithMethodInjection(bd, beanName, owner);
		}
	}
```

- 如果Bean的方法被覆盖了,则使用CGLib进行实例化
- 否则使用JDK反射机制进行实例化

## CglibSubclassingInstantiationStrategy#instantiateWithMethodInjection

使用CGLIB进行Bean对象实例化

```java
		//使用CGLIB进行Bean对象实例化
		public Object instantiate(@Nullable Constructor<?> ctor, @Nullable Object... args) {
			//创建代理子类
			Class<?> subclass = createEnhancedSubclass(this.beanDefinition);
			Object instance;
			if (ctor == null) {
				instance = BeanUtils.instantiateClass(subclass);
			}
			else {
				try {
					Constructor<?> enhancedSubclassConstructor = subclass.getConstructor(ctor.getParameterTypes());
					instance = enhancedSubclassConstructor.newInstance(args);
				}
				catch (Exception ex) {
					throw new BeanInstantiationException(this.beanDefinition.getBeanClass(),
							"Failed to invoke constructor for CGLIB enhanced subclass [" + subclass.getName() + "]", ex);
				}
			}
			// SPR-10785: set callbacks directly on the instance instead of in the
			// enhanced class (via the Enhancer) in order to avoid memory leaks.
			Factory factory = (Factory) instance;
			factory.setCallbacks(new Callback[] {NoOp.INSTANCE,
					new LookupOverrideMethodInterceptor(this.beanDefinition, this.owner),
					new ReplaceOverrideMethodInterceptor(this.beanDefinition, this.owner)});
			return instance;
		}
```

