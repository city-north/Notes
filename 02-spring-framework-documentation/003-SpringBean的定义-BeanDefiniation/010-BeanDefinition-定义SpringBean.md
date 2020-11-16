# BeanDefinition定义SpringBean

- [什么是BeanDefinition](#什么是BeanDefinition)
- [BeanDefinition元信息](#BeanDefinition元信息)
- [使用BeanDefinitionBuilder来定义一个BeanDefinition](#使用BeanDefinitionBuilder来定义一个BeanDefinition)

## 什么是BeanDefinition

![image-20201116213208976](../../assets/image-20201116213208976.png)

BeanDefinition 是Spring Framework 中定义Bean的配置元信息接口,包括:

- Bean的类名
- Bean行为配置元素, 如作用域, 自动绑定的模式, 生命周期的回调等
- 其他Bean的引用, 又可以称为(Collaborators)或者依赖 (Dependencies)
- 配置设置 , 比如Bean属性(Properties)

## BeanDefinition元信息

| 属性                    | 说明                                           |
| ----------------------- | ---------------------------------------------- |
| Class                   | Bean全类名,必须是具体的类,不能用抽象类或者接口 |
| Name                    | Bean的名称或者ID                               |
| Scope                   | Bean的作用域                                   |
| Constructor aguments    | Bean构造器参数(用于依赖注入)                   |
| Properties              | Bean属性设置(用于依赖注入)                     |
| Autowiring mode         | Bean 自动绑定模式 , 如 通过名称 byName         |
| Lazy initalization mode | Bean 延迟初始化模式(延迟和非延迟)              |
| Initialization method   | Bean 初始化回调方法名称                        |
| Destruction method      | Bean 销毁回调方法名称                          |

## 使用BeanDefinitionBuilder来定义一个BeanDefinition

```java
public class BeanDefinitionBuilderExample {

    public static void main(String[] args) {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
                .rootBeanDefinition(MyBean.class)
                .addPropertyValue("str", "hello")
                .setScope(ConfigurableBeanFactory.SCOPE_PROTOTYPE);
        factory.registerBeanDefinition("myBean", beanDefinitionBuilder.getBeanDefinition());

        MyBean bean = factory.getBean(MyBean.class);
        bean.doSomething();
    }

    public static class MyBean {
        private String str;

        public void setStr(String str) {
            this.str = str;
        }

        public void doSomething() {
            System.out.println("from MyBean " + str);
        }
    }
}
```

## SpringCloud中OpenFeign的形式

org.springframework.cloud.openfeign.FeignClientsRegistrar#registerFeignClient

```java
	private void registerFeignClient(BeanDefinitionRegistry registry,
			AnnotationMetadata annotationMetadata, Map<String, Object> attributes) {
		String className = annotationMetadata.getClassName();
		BeanDefinitionBuilder definition = BeanDefinitionBuilder
				.genericBeanDefinition(FeignClientFactoryBean.class);
		validate(attributes);
		definition.addPropertyValue("url", getUrl(attributes));
		definition.addPropertyValue("path", getPath(attributes));
		String name = getName(attributes);
		definition.addPropertyValue("name", name);
		definition.addPropertyValue("type", className);
		definition.addPropertyValue("decode404", attributes.get("decode404"));
		definition.addPropertyValue("fallback", attributes.get("fallback"));
		definition.addPropertyValue("fallbackFactory", attributes.get("fallbackFactory"));
		definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

		String alias = name + "FeignClient";
		AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();

		boolean primary = (Boolean)attributes.get("primary"); // has a default, won't be null

		beanDefinition.setPrimary(primary);

		String qualifier = getQualifier(attributes);
		if (StringUtils.hasText(qualifier)) {
			alias = qualifier;
		}

		BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, className,
				new String[] { alias });
		BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
	}
```

