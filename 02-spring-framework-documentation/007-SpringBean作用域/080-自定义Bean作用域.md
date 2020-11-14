# 080-自定义Bean作用域

- 实现Scope接口
  - org.springframework.beans.factory.config.Scope

- 注册Scope 

  - API -> 

    ```java
    org.springframework.beans.factory.config.ConfigurableBeanFactory#registerScope
    ```

  - XML配置的方式

  ```xml
  <bean class = "org.springframework.beans.factory.config.CustomScopeConfigurer">
    <property name="scopes">
      <map>
        <entry key ="">
        </entry>
      </map>
    </property>
  </bean>
  ```

## 自定义Bean作用域实战

自定义一个线程作用域*ThreadLocal级别的缓存

```java
/**
 * ThreadLocal 级别 Scope
 */
public class ThreadLocalScope implements Scope {

    public static final String SCOPE_NAME = "thread-local";

    private final NamedThreadLocal<Map<String, Object>> threadLocal = new NamedThreadLocal("thread-local-scope") {
        public Map<String, Object> initialValue() {
            return new HashMap<>();
        }
    };

  //通过容器去取,给容器去掉
  @Override
  public Object get(String name, ObjectFactory<?> objectFactory) {
    // 非空
    Map<String, Object> context = getContext();
    Object object = context.get(name);
    if (object == null) {
      object = objectFactory.getObject();
      context.put(name, object);
    }
    return object;
  }

  @NonNull
  private Map<String, Object> getContext() {
    return threadLocal.get();
  }

  //删除
  @Override
  public Object remove(String name) {
    Map<String, Object> context = getContext();
    return context.remove(name);
  }

  //销毁回调
  @Override
  public void registerDestructionCallback(String name, Runnable callback) {
    // TODO 销毁回调
  }
  @Override
  public Object resolveContextualObject(String key) {
    Map<String, Object> context = getContext();
    return context.get(key);
  }

  @Override
  public String getConversationId() {
    Thread thread = Thread.currentThread();
    return String.valueOf(thread.getId());
  }
}

```

标注自定义Scope

```java
    @Bean
    @Scope(ThreadLocalScope.SCOPE_NAME)
    public User user() {
        return createUser();
    }
```

使用ApplicationContext注册一个Scope

```java
public static void main(String[] args) {
  // 创建 BeanFactory 容器
  AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
  // 注册 Configuration Class（配置类） -> Spring Bean
  applicationContext.register(ThreadLocalScopeDemo.class);

  applicationContext.addBeanFactoryPostProcessor(beanFactory -> {
    // 注册自定义 scope
    beanFactory.registerScope(ThreadLocalScope.SCOPE_NAME, new ThreadLocalScope());
  });

  // 启动 Spring 应用上下文
  applicationContext.refresh();

  scopedBeansByLookup(applicationContext);

  // 关闭 Spring 应用上下文
  applicationContext.close();
}

private static void scopedBeansByLookup(AnnotationConfigApplicationContext applicationContext) {
  for (int i = 0; i < 3; i++) {
    Thread thread = new Thread(() -> {
      // user 是共享 Bean 对象
      User user = applicationContext.getBean("user", User.class);
      System.out.printf("[Thread id :%d] user = %s%n", 		Thread.currentThread().getId(), user);
    });

    // 启动线程
    thread.start();
    // 强制线程执行完成
    try {
      thread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
```

```
User Bean [user] 初始化...
[Thread id :13] user = User{id=93086457488844, name='null', city=null, workCities=null, lifeCities=null, configFileLocation=null, company=null, context=null, contextAsText='null', beanName='user'}
User Bean [user] 初始化...
[Thread id :14] user = User{id=93086470766040, name='null', city=null, workCities=null, lifeCities=null, configFileLocation=null, company=null, context=null, contextAsText='null', beanName='user'}
User Bean [user] 初始化...
[Thread id :15] user = User{id=93086472085256, name='null', city=null, workCities=null, lifeCities
```

