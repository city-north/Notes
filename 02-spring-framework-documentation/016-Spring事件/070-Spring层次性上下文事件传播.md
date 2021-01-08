# 070-Spring层次性上下文事件传播

[TOC]

## 简介

当Spring应用上下文出现多层次Spring应用上下文(ApplicationContext)时. 如 

- Spring WebMVC
- SpringBoot
- SpringCloud场景

由子ApplicationContext发起的Spring事件可能会传递给其父ApplicationContext(直到Root)的过程

## 如何避免

- 定位Spring事件源(ApplicationContext)进行过滤处理

## 代码实例

```java
public class HierarchicalSpringEventPropagateDemo {

    public static void main(String[] args) {
        // 1. 创建 parent Spring 应用上下文
        AnnotationConfigApplicationContext parentContext = new AnnotationConfigApplicationContext();
        parentContext.setId("parent-context");
        // 注册 MyListener 到 parent Spring 应用上下文
        parentContext.register(MyListener.class);

        // 2. 创建 current Spring 应用上下文
        AnnotationConfigApplicationContext currentContext = new AnnotationConfigApplicationContext();
        currentContext.setId("current-context");
        // 3. current -> parent
        currentContext.setParent(parentContext);
        // 注册 MyListener 到 current Spring 应用上下文
        currentContext.register(MyListener.class);

        // 4.启动 parent Spring 应用上下文
        parentContext.refresh();

        // 5.启动 current Spring 应用上下文
        currentContext.refresh();

        // 关闭所有 Spring 应用上下文
        currentContext.close();
        parentContext.close();
    }

    static class MyListener implements ApplicationListener<ApplicationContextEvent> {

        private Set<ApplicationContextEvent> processedEvents = new LinkedHashSet<>();

        @Override
        public void onApplicationEvent(ApplicationContextEvent event) {
            if (processedEvents.add(event)) {//使用一个Set确保只消费一次
                System.out.printf("监听到 Spring 应用上下文[ ID : %s ] 事件 :%s\n", event.getApplicationContext().getId(),
                        event.getClass().getSimpleName());
            }
        }
    }
}

```

