# 120-Extensible-XML-authoring拓展原理

[TOC]

## 实战

 [110-基于ExtensibleXML-authoring拓展SpringXML元素.md](110-基于ExtensibleXML-authoring拓展SpringXML元素.md) 

## 触发时机

```
- AbstractApplicationContext#obtainFreshBeanFactory
  - AbstractRefreshableApplicationContext#refreshBeanFactory
    - AbstractXmlApplicationContext#loadBeanDefinitions
      - ...
        - XmlBeanDefinitionReader#doLoadBeanDefinitions
          - ...
            - BeanDefinitionParserDelagte#parseCustomElement
```

## 核心流程

- BeanDefinitionParserDelagte#parseCustomElement(org.w3c.dom.Element,BeanDefinition)
  - 获取namespace
  - 通过namespace解析NamespaceHandler
  - 构造ParserContext
  - 解析元素，获取了BeanDefinition

