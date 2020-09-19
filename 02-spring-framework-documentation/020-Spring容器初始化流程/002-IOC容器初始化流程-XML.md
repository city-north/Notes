# IoC容器初始化流程-XML

## 图示

![image-20200919225312492](../../assets/image-20200919225312492.png)

## 初始化流程

- [定位](#定位) :  核心类为 XmlBeanDefinitionReader
- [加载](#加载)
- [注册](#注册)

## XML配置文件读取流程

- 通过继承自 AbstractBeanDefinitionReader 中的方法，来使用 ResourLoader 将资源文件路径转换为对应的Resource文件。
- 通过 DocumentLoader 对 Resource 文件进行转换，将 Resource 文件转换为 Document 文件。
- 通过实现接口 BeanDefinitionDocumentReader 的 DefaultBeanDefinitionDocumentReader 类对Document进行解析，并使用 BeanDefinitionParserDelegate 对 Element 进行解析。

#### 涉及的类包括

XML配置文件的读取是Spring中重要的功能，因为Spring的大部分功能都是以配置作为切入点的，那么我们可以XmlBeanDefinitionReader 中梳理一下资源文件读取、解析及注册的大致脉络，首先我们看看各个类的功能。

- ResourceLoader：定义资源加载器，主要应用于根据给定的资源文件地址返回对应的Resource。
- BeanDefinitionReader：主要定义资源文件读取并转换为BeanDefinition的各个功能。
- EnvironmentCapable：定义获取Environment方法。
- DocumentLoader：定义从资源文件加载到转换为Document的功能。
- AbstractBeanDefinitionReader：对EnvironmentCapable、BeanDefinitionReader类定义的功能进行实现
- BeanDefinitionDocumentReader：定义读取Docuemnt并注册BeanDefinition功能。
- BeanDefinitionParserDelegate：定义解析Element的各种方法。

## 加载

略,实际上就是解析xml以及各种规则配置

## 注册

- [通过beanName注册BeaDefinition](#通过beanName注册BeaDefinition)
- [通过别名注册BeanDefinition](#通过别名注册BeanDefinition)
- [通知监听器解析及注册完成](#通知监听器解析及注册完成)

processBeanDefinition函数中的

```java
BeanDefinitionReaderUtils.registerBeanDefinition(bdHolder, getReaderContext().getRegistry())
```

```java
	//将解析的BeanDefinitionHold注册到容器中
	public static void registerBeanDefinition(
			BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry)
			throws BeanDefinitionStoreException {

		// Register bean definition under primary name.
		//获取解析的BeanDefinition的名称
		String beanName = definitionHolder.getBeanName();
		//向IOC容器注册BeanDefinition
		registry.registerBeanDefinition(beanName, definitionHolder.getBeanDefinition());

		// Register aliases for bean name, if any.
		//如果解析的BeanDefinition有别名，向容器为其注册别名
		String[] aliases = definitionHolder.getAliases();
		if (aliases != null) {
			for (String alias : aliases) {
        //2.通过别名注册BeanDefinition
				registry.registerAlias(beanName, alias);
			}
		}
	}

```

## 通过beanName注册BeaDefinition

Feign在构建后就是这样注册的

registry.registerBeanDefinition(beanName, definitionHolder.getBeanDefinition());

- 对AbstractBeanDefinition的校验。在解析XML文件的时候我们提过校验，但是此校验非彼校验，之前的校验时针对于XML格式的校验，而此时的校验时针是对于AbstractBean- Definition的methodOverrides属性的。
- 对beanName已经注册的情况的处理。如果设置了不允许bean的覆盖，则需要抛出异常，否则直接覆盖。
- 加入map缓存。
- 清除解析之前留下的对应beanName的缓存。
- 通过别名注册BeanDefinition

## 通过别名注册BeanDefinition

```java
registry.registerAlias(beanName, alias);
```

- alias与beanName相同情况处理。若alias与beanName并名称相同则不需要处理并删除掉原有alias。
- alias覆盖处理。若aliasName已经使用并已经指向了另一beanName则需要用户的设置进行处理。
- alias循环检查。当A->B存在时，若再次出现A->C->B时候则会抛出异常。
- 注册alias。

## 通知监听器解析及注册完成

通过代码

```
//org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader#processBeanDefinition
getReaderContext().fireComponentRegistered(new BeanComponentDefinition(bdHolder))
```

完成此工作，这里的实现只为扩展，当程序开发人员需要对注册BeanDefinition事件进行监听时可以通过注册监听器的方式并将处理逻辑写入监听器中，目前在Spring中并没有对此事件做任何逻辑处理