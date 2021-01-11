# 070-Spring注解属性别名-Attribute-Aliases

[toc]

## 什么Spring中的属性别名

一个属性别名的意思是，注解A上的属性传递到注解B上的属性，标注在注解A上的属性的效果和标注在B上是一致的。

- 显式别名

  ​	在一个注解中，使用@AliaseFor互相标注的注解是显式别名

- 隐式别名

  - 如果一个注解上有两个或者更多属性被声明成显性，并覆盖掉相同的元信息上面的@AliasFor（层次中的覆盖）

- 传递性的显式别名

  - 给定两个或者

An ***attribute alias*** is an alias from one annotation attribute to another annotation attribute. Attributes within a set of aliases can be used interchangeably and are treated as equivalent. Attribute aliases can be categorized as follows.

1. **Explicit Aliases**: if two attributes in one annotation are declared as aliases for each other via `@AliasFor`, they are *explicit aliases*.
2. **Implicit Aliases**: if two or more attributes in one annotation are declared as explicit overrides for the same attribute in a meta-annotation via `@AliasFor`, they are *implicit aliases*.
3. **Transitive Implicit Aliases**: given two or more attributes in one annotation that are declared as explicit overrides for attributes in meta-annotations via `@AliasFor`, if the attributes *effectively* override the same attribute in a meta-annotation following the [law of transitivity](https://en.wikipedia.org/wiki/Transitive_relation), they are *transitive implicit aliases*.

