# 050-Java文本格式化

[TOC]

## 核心接口

java.text.MessageFormat(线程不安全 )

## 基本用法

- 设置消息格式模式 - new MessageFormat(...)
- 格式化 - format(new Object [] {...})

## 消息格式模式

- 格式元素 {ArgumentIndex (,FormartType, (FormatStyle))}

- FormatType: 消息格式类型, 可选项 , 每种类型在以下选择

  - number
  - date
  - time
  - choice

- FormatStyle: 消息格式风格,可选项包括

  - short
  - medium
  - long
  - full
  - integer
  - currency
  - percent

  