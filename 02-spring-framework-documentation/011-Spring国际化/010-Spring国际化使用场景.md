# 010-Spring国际化使用场景

[TOC]

## 使用场景总结

- 普通国际化文案
- Bean Validation校验国际化文案
- Web站点页面渲染
- Web MVC 错误消息提示

## 普通国际化文案

直接调用 getMessage 方法获取国际化文案

```java
MessageSource#getMessage(String code, Object[] args, String default, Locale loc)MessageSource#
```

## Bean Validation校验国际化文案

SpringBoot中使用比较多

SpringBoot场景中会默认使用Bean Validation,也就是Bean校验,默认使用的是 Hibernate Validator API实现

## Web站点页面渲染

什么是页面渲染

通过不同的国家或者地区Ip , 会收到不同的页面渲染 , 这时文字包括布局会发生变化

## Web MVC 错误消息提示

在国际化的常见下,如果报错,会有一些文字性的描述,友好提示

