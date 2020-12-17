# SpEL表达式简述

- [使用SpEL去执行文本表达式](#使用SpEL去执行文本表达式)
- [使用SpEL表达式执行方法调用](#使用SpEL表达式执行方法调用)
- [使用SpEL表达式去访问类的属性](#使用SpEL表达式去访问类的属性)
- [使用SpEL表达式调用构造方法](#使用SpEL表达式调用构造方法)

## 直接使用SpEL去执行文本表达式

```java
ExpressionParser parser = new SpelExpressionParser();
Expression exp = parser.parseExpression("'Hello World'");
String message = (String) exp.getValue();
```

- ExpressionParser 接口负责解析表达式的
- Expression 是解析后的表达式

SpEL 支持很多特性,例如

- 方法调用
- 访问属性
- 访问构造器

## 使用SpEL表达式执行方法调用

```java
ExpressionParser parser = new SpelExpressionParser();
Expression exp = parser.parseExpression("'Hello World'.concat('!')");
String message = (String) exp.getValue();
```

上面方法执行了concat 方法,这个方法 将 ! 增加到 字符串后

## 使用SpEL表达式去访问类的属性

当访问属性 bytes 时, 实际上是调用的JavaBean 协议的getBytes

```java
ExpressionParser parser = new SpelExpressionParser();

// invokes 'getBytes()'
Expression exp = parser.parseExpression("'Hello World'.bytes");
byte[] bytes = (byte[]) exp.getValue();
```

可以调用public 标识符的length

```java
ExpressionParser parser = new SpelExpressionParser();

// invokes 'getBytes().length'
Expression exp = parser.parseExpression("'Hello World'.bytes.length");
int length = (Integer) exp.getValue();
```

可以看到调用的的是String的

## 使用SpEL表达式调用构造方法

可以使用SpEL 表达式直接调用String的构造器

```java
ExpressionParser parser = new SpelExpressionParser();
Expression exp = parser.parseExpression("new String('hello  world').toUpperCase()");
String message = exp.getValue(String.class);
```

## 使用泛型直接进行类型转换

```java
final String value = expression.getValue(String.class);
```

