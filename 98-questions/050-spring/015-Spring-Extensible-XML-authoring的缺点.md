# 015-Spring-Extensible-XML-authoring的缺点

- 搞复杂度：开发人员需要熟悉
  - XML Schema
  - spring.handlers
  - spring.schemas
  - 已经相应的API
- 嵌套元素支持比较弱：通常需要使用方法递归或者器嵌套解析的方式去处理嵌套子元素
- XML处理性能差，基于DOM level3 API实现， 易于理解，性能较差
- XML框架移植性差：很难适配高性能和便利的XML框架，如JAXB