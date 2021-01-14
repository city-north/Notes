# 002-注解方式触发Spring上下文初始化

## 图解

![image-20200917215957012](../../assets/image-20200917215957012.png)

### 实例代码

```java
@Configuration
class QuickStartExample {
    public static void main(String... strings) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(QuickStartExample.class);
        HelloWorldServiceClient bean = context.getBean(HelloWorldServiceClient.class);
        bean.showMessage();
    }
}002
```

### 查看构造器中的操作

```java
public AnnotationConfigApplicationContext(Class<?>... annotatedClasses) {
  this();
  register(annotatedClasses);
  refresh();
}
```

#### 看到了refresh,基本就走的通用流程,仅仅只是获取BeanDefination的方式不同而已

