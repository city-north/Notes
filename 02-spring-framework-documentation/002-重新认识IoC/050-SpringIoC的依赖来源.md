# Spring IoC的依赖来源

- 自定义的Bean
- 容器内建Bean对象
- 容器内建依赖

## 内建依赖

类似于 BeanFactory

## 什么是内建的Bean对象

```
    /**
     * 内部内建Bean
     * @param beanFactory
     */
    private static void injectInsideBean(BeanFactory beanFactory) {
        final Environment bean = beanFactory.getBean(Environment.class);
        System.out.println(bean);

    }
```

