# 010-Spring应用上下文启动准备阶段

[TOC]

## 一言蔽之

准备阶段主要是创建PropertySource,创建Environment,可以让你拓展监听器集合,添加早期Spring事件

## Spring应用上下文启动准备阶段做了什么

核心入口- **AbstractApplicationContext#prepareRefresh()**方法

值得注意的点

- 启动时间 - startupDate (日志里进行一个输出)
- 状态标识-closed(false)、active(true)
- [初始化PropertySources](#初始化PropertySources)-initPropertySource()
- [检验Environment中必须属性](#检验Environment中必须属性)
- [初始化时间监听器集合](#初始化时间监听器集合)
- [初始化早期Spring事件集合](#初始化早期Spring事件集合)

## 源码分析

### prepareRefresh主要作用

主要是做些准备工作,例如对系统属性以及环境变量的初始化和验证

```java
protected void prepareRefresh() {
  //登记刷新时间
  this.startupDate = System.currentTimeMillis();
  this.closed.set(false);//标志位
  this.active.set(true);//标志位

  if (logger.isInfoEnabled()) {
    logger.info("Refreshing " + this);
  }

  // Initialize any placeholder property sources in the context environment
  //留给子类覆盖
  initPropertySources();

  // Validate that all properties marked as required are resolvable
  // see ConfigurablePropertyResolver#setRequiredProperties
  // 验证需要的属性文件是否都已经放入环境中
  getEnvironment().validateRequiredProperties();


  // Store pre-refresh ApplicationListeners...
  if (this.earlyApplicationListeners == null) {
    //初始化监听器集合,我们可以自定义加一下
    this.earlyApplicationListeners = new LinkedHashSet<>(this.applicationListeners);
  }
  else {
    // Reset local application listeners to pre-refresh state.
    this.applicationListeners.clear();
    this.applicationListeners.addAll(this.earlyApplicationListeners);
  }
  // Allow for the collection of early ApplicationEvents,
  // to be published once the multicaster is available...
  //初始化早期事件
  this.earlyApplicationEvents = new LinkedHashSet<>();
}
```

### 初始化PropertySources

```java
//留给子类覆盖
initPropertySources();
```

initPropertySources留给子类实现,例如在Servlet项目中, 主动去调用了自定义的getEnvironment()逻辑创建环境信息

```java
//org.springframework.web.context.support.AbstractRefreshableWebApplicationContext#initPropertySources
@Override
protected void initPropertySources() {
  ConfigurableEnvironment env = getEnvironment();
  if (env instanceof ConfigurableWebEnvironment) {
    ((ConfigurableWebEnvironment) env).initPropertySources(this.servletContext, this.servletConfig);
  }
}

@Override
public ConfigurableEnvironment getEnvironment() {
  //没有则自动创建
  if (this.environment == null) {
      //创建
    this.environment = createEnvironment();
  }
  return this.environment;
}

@Override
protected ConfigurableEnvironment createEnvironment() {
  //创建自定义的Environment抽象
  return new StandardEnvironment();
}
```

然后就可以定制外部化配置

### 检验Environment中必须属性

检验Environment中必须属性 : `getEnvironment().validateRequiredProperties();`

假如现在有这样一个需求，工程在运行过程中用到的某个设置（例如VAR）是从系统环境变量中取得的，而如果用户没有在系统环境变量中配置这个参数，那么工程可能不会工作。这一要求可能会有各种各样的解决办法，当然，在Spring中可以这样做，你可以直接修改Spring的源码，例如修改ClassPathXmlApplicationContext。当然，最好的办法还是对源码进行扩展，我们可以自定义类：

```java
public class MyClassPathXmlApplicationContext extends ClassPathXmlApplicationContext{
     public MyClassPathXmlApplicationContext(String... configLocations ){
         super(configLocations);
     }
			protected void initPropertySources() {
 				//添加验证要求
     		getEnvironment().setRequiredProperties("VAR");
 			}
}
```

我们自定义了继承自 `ClassPathXmlApplicationContext` 的 `MyClassPathXmlApplicationContext` ，并重写了`initPropertySources`方法，在方法中添加了我们的个性化需求，那么在验证的时候也就是程序走到 `getEnvironment().validateRequiredProperties() `代码的时候，如果系统并没有检测到对应VAR的环境变量，那么将抛出异常。当然我们还需要在使用的时候替换掉原有的`ClassPathXmlApplicationContext`：

```java
public static void main(String[] args) {
 				ApplicationContext bf = new MyClassPathXmlApplicationContext ("test/customtag/test.xml"); 
        User user=(User) bf.getBean("testbean");
}
```

### 初始化时间监听器集合

仅仅初始化容器对象LinkedHashSet();

### 初始化早期Spring事件集合

我们可以在启动时预定义Spring事件的早期事件

