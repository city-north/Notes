# 090-第九步-onRefresh-调用子类特殊的bean初始化逻辑

[TOC]

![image-20201007151953236](../../assets/image-20201007151953236.png)



## 简介

第九步实际上就是给子类一个回调,让自诶进行特殊处理

## AbstractApplicationContext

```java
protected void onRefresh() throws BeansException {
  // For subclasses: do nothing by default.
}
```

## 子类-AbstractRefreshableWebApplicationContext

```java
@Override
protected void onRefresh() {
  this.themeSource = UiApplicationContextUtils.initThemeSource(this);
}
```

## 子类-GenericWebApplicationContext

```java
@Override
protected void onRefresh() {
  this.themeSource = UiApplicationContextUtils.initThemeSource(this);
}
```

### 孙子类-ServletWebServerApplicationContext

```java
@Override
protected void onRefresh() {
  super.onRefresh();
  try {
    createWebServer();
  }
  catch (Throwable ex) {
    throw new ApplicationContextException("Unable to start web server", ex);
  }
}
```

