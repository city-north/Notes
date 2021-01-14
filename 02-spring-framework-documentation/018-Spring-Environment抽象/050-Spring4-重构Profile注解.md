# 050-Spring4-重构Profile注解

[TOC]

## 一言蔽之

Spring4重构Profile注解,重构的思路主要是使用Condition接口和Conditional注解完成,在ConfigurationClassPostProcessor[ConfigurationClassPostProcessor的子类]中完成对注入的判断是否注入

## 实现DEMO

基于Spring4 org.springframework.context.annotation.Condition接口实现

org.springframework.context.annotation.ProfileCondition

```java
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(ProfileCondition.class)
public @interface Profile {

	/**
	 * The set of profiles for which the annotated component should be registered.
	 */
	String[] value();

}
```

```java
class ProfileCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    //获取所有属性
		MultiValueMap<String, Object> attrs = metadata.getAllAnnotationAttributes(Profile.class.getName());
		if (attrs != null) {
			for (Object value : attrs.get("value")) {
        //激活
				if (context.getEnvironment().acceptsProfiles(Profiles.of((String[]) value))) {
					return true;
				}
			}
			return false;
		}
		return true;
	}

}
```

## 实现原理

使用ConfigurationClassPostProcessor后置处理器的postProcessBeanDefinitionRegistry回调中助力具体的注册Bean

```java
org.springframework.context.annotation.ConfigurationClassPostProcessor#processConfigBeanDefinitions
```

执行流程如下

```
shouldSkip:85, ConditionEvaluator (org.springframework.context.annotation)
loadBeanDefinitionsForBeanMethod:184, ConfigurationClassBeanDefinitionReader (org.springframework.context.annotation)
loadBeanDefinitionsForConfigurationClass:144, ConfigurationClassBeanDefinitionReader (org.springframework.context.annotation)
loadBeanDefinitions:120, ConfigurationClassBeanDefinitionReader (org.springframework.context.annotation)
processConfigBeanDefinitions:337, ConfigurationClassPostProcessor (org.springframework.context.annotation)
postProcessBeanDefinitionRegistry:242, ConfigurationClassPostProcessor (org.springframework.context.annotation)
invokeBeanDefinitionRegistryPostProcessors:275, PostProcessorRegistrationDelegate (org.springframework.context.support)
invokeBeanFactoryPostProcessors:95, PostProcessorRegistrationDelegate (org.springframework.context.support)
invokeBeanFactoryPostProcessors:706, AbstractApplicationContext (org.springframework.context.support)
refresh:532, AbstractApplicationContext (org.springframework.context.support)
main:50, ProfileDemo (org.geekbang.thinking.in.spring.annotation)
```

shouldSkip方法具体判断了是否应该跳过Bean的注册或者是BeanClass的注册

```java
public boolean shouldSkip(@Nullable AnnotatedTypeMetadata metadata, @Nullable ConfigurationPhase phase) {
  if (metadata == null || !metadata.isAnnotated(Conditional.class.getName())) {
    return false;
  }

  if (phase == null) {
    if (metadata instanceof AnnotationMetadata &&
        ConfigurationClassUtils.isConfigurationCandidate((AnnotationMetadata) metadata)) {
      return shouldSkip(metadata, ConfigurationPhase.PARSE_CONFIGURATION);
    }
    return shouldSkip(metadata, ConfigurationPhase.REGISTER_BEAN);
  }

  List<Condition> conditions = new ArrayList<>();
  for (String[] conditionClasses : getConditionClasses(metadata)) {
    for (String conditionClass : conditionClasses) {
      Condition condition = getCondition(conditionClass, this.context.getClassLoader());
      conditions.add(condition);
    }
  }

  AnnotationAwareOrderComparator.sort(conditions);

  for (Condition condition : conditions) {
    ConfigurationPhase requiredPhase = null;
    if (condition instanceof ConfigurationCondition) {
      requiredPhase = ((ConfigurationCondition) condition).getConfigurationPhase();
    }
    if ((requiredPhase == null || requiredPhase == phase) && !condition.matches(this.context, metadata)) {
      return true;
    }
  }

  return false;
}
```

