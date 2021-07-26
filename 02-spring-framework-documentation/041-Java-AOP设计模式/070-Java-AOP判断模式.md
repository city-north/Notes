# 070-Java-AOP判断模式

[TOC]

## 简介

AOP的判断模式的意思是指， 如何判断是我要拦截的类或者方法，如何进行筛选的

## 判断来源

- 类型（Class）
- 方法（Method)
- 注解（Annotation）
- 参数（Parameter)
- 异常（Exception)

## 目标方法过滤

找到方法

```
Method targetMethod = ReflectionUtils.findMethod(targetClass, "echo", String.class);
```

## 目标类判断

```
// 获取目标类
Class<?> targetClass = classLoader.loadClass(targetClassName);
```

## 查找方法抛出指定异常

```java
throws 类型为 NullPointerException

        // 查找方法  throws 类型为 NullPointerException
        ReflectionUtils.doWithMethods(targetClass, new ReflectionUtils.MethodCallback() {
            @Override
            public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                System.out.println("仅抛出 NullPointerException 方法为：" + method);
            }
        }, new ReflectionUtils.MethodFilter() {
            @Override
            public boolean matches(Method method) {
                Class[] parameterTypes = method.getParameterTypes();//获取参数
                Class[] exceptionTypes = method.getExceptionTypes();
                return parameterTypes.length == 1//长度
                        && String.class.equals(parameterTypes[0])//参数类型
                        && exceptionTypes.length == 1//异常类型
                        && NullPointerException.class.equals(exceptionTypes[0]);//空指针
            }
        });
```

## 代码

```java
public class TargetFilterDemo {

    public static void main(String[] args) throws ClassNotFoundException {
        String targetClassName = "org.geekbang.thinking.in.spring.aop.overview.EchoService";
        // 获取当前线程 ClassLoader
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        // 获取目标类
        Class<?> targetClass = classLoader.loadClass(targetClassName);
        // 方法定义：String echo(String message);
        // Spring 反射工具类
        Method targetMethod = ReflectionUtils.findMethod(targetClass, "echo", String.class);
        System.out.println(targetMethod);

        // 查找方法  throws 类型为 NullPointerException
        ReflectionUtils.doWithMethods(targetClass, new ReflectionUtils.MethodCallback() {
            @Override
            public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                System.out.println("仅抛出 NullPointerException 方法为：" + method);
            }
        }, new ReflectionUtils.MethodFilter() {
            @Override
            public boolean matches(Method method) {
                Class[] parameterTypes = method.getParameterTypes();
                Class[] exceptionTypes = method.getExceptionTypes();
                return parameterTypes.length == 1
                        && String.class.equals(parameterTypes[0])
                        && exceptionTypes.length == 1
                        && NullPointerException.class.equals(exceptionTypes[0]);
            }
        });
    }
}

```