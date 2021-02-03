# 021-feign客户端扫描-自定义类扫描器

[TOC]

## 一言蔽之

Feign使用了Spring自定义扫描器机制ClassPathScanningCandidateComponentProvider, 返回过滤后的类

## 自定义扫描器ClassPathScanningCandidateComponentProvider

registerFeignClients方法中有一些细节值得认真学习，有利于加深了解Spring框架。

首先是如何自定义Spring类扫描器，即如何使用ClassPathScanningCandidateComponentProvider和各类TypeFilter。
OpenFeign使用了AnnotationTypeFilter，来过滤出被@FeignClient修饰的类，getScanner方法的具体实现如下所示：

```java
//FeignClientsRegistrar.java
protected ClassPathScanningCandidateComponentProvider getScanner() {
    return new ClassPathScanningCandidateComponentProvider(false, this.environment) {
        @Override
        protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
            boolean isCandidate = false;
            //判断beanDefinition是否为内部类，否则直接返回false
            if (beanDefinition.getMetadata().isIndependent()) {
                //判断是否为接口类，所实现的接口只有一个，并且该接口是Annotation。否则直接
                       返回true
                if (!beanDefinition.getMetadata().isAnnotation()) {
                    isCandidate = true;
                }
            }
            return isCandidate;
        }
    };
}
```

#### 值得注意的是

- ClassPathScanningCandidateComponentProvider的作用是遍历指定路径的包下的所有类。比如指定包路径为com/test/openfeign，它会找出com.test.openfeign包下所有的类，将所有的类封装成Resource接口集合
- 接着ClassPathScanningCandidateComponentProvider类会遍历Resource集合，通过includeFilters和excludeFilters两种过滤器进行过滤操作,includeFilters和excludeFilters是TypeFilter接口类型实例的集合
  - TypeFilter接口是一个用于判断类型是否满足要求的类型过滤器
  - excludeFilters中只要有一个TypeFilter满足条件，这个Resource就会被过滤掉
  - includeFilters中只要有一个TypeFilter满足条件，这个Resource就不会被过滤

如果一个Resource没有被过滤，它会被转换成ScannedGenericBeanDefinition添加到BeanDefinition集合中

## 使用

```java
ClassPathScanningCandidateComponentProvider scanner = getScanner();
scanner.setResourceLoader(this.resourceLoader);
Set〈BeanDefinition〉 candidateComponents = scanner.findCandidateComponents(basePackage);
```

