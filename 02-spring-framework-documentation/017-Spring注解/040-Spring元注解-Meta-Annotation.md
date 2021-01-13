# 040-Spring元注解-Meta-Annotation

[TOC]

## 什么是元注解Meta-Annotations

A ***meta-annotation*** is an annotation that is declared on another annotation. An annotation is therefore *meta-annotated* if it is annotated with another annotation. For example, any annotation that is declared to be *documented* is meta-annotated with `@Documented` from the `java.lang.annotation` package.

它是一个注解，标注在另外一个注解上面，描述的过程就是元注解，Spring元注解的是基于Java元注解的方式实现的

## 举例说明

- java.lang.annotation.Documented
- java.lang.annotation.Inherited
- java.lang.annotation.Repeatable

