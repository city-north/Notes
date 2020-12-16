# 080-依赖注入-ResourceLoader

[TOC]

## 注入ResourceLoader的方法

1. 实现ResourceLoaderAware回调
2. @Autowired注入ResourceLoader
3. 注入ApplicationContext作为ResourceLoader

### 

## DEMO

```java
/**
 * 注入 {@link ResourceLoader} 对象示例
 *
 * @see ResourceLoader
 * @see Resource
 * @see Value
 * @see AnnotationConfigApplicationContext
 * @since
 */
public class InjectingResourceLoaderDemo implements ResourceLoaderAware {

    private ResourceLoader resourceLoader; // 方法一

    @Autowired
    private ResourceLoader autowiredResourceLoader; // 方法二

    @Autowired
    private AbstractApplicationContext applicationContext; // 方法三

    @PostConstruct
    public void init() {
        System.out.println("resourceLoader == autowiredResourceLoader : " + (resourceLoader == autowiredResourceLoader));
        System.out.println("resourceLoader == applicationContext : " + (resourceLoader == applicationContext));
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        // 注册当前类作为 Configuration Class
        context.register(InjectingResourceLoaderDemo.class);
        // 启动 Spring 应用上下文
        context.refresh();
        // 关闭 Spring 应用上下文
        context.close();

    }
}

```

## 原理

 [006-SpringIoC依赖来源](../006-SpringIoC依赖来源/README.md) 

 [050-游离对象作为依赖来源.md](../006-SpringIoC依赖来源/050-游离对象作为依赖来源.md) 