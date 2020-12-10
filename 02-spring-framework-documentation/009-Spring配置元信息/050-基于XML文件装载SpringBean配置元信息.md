# 050-基于XML文件装载SpringBean配置元信息

[TOC]

## 一言蔽之

SpringXML方式装载SpringBean配置元信息使用的是Dom解析XML,XmlBeanDefinitionReader的doLoadBeanDefinitions方法进行解析成BeanDefinition并最终通过注册中心BeanDefinitionRegistry注册

## 底层实现-XmlBeanDefinitionReader

org.springframework.beans.factory.xml.XmlBeanDefinitionReader#registerBeanDefinitions

```java
//org.springframework.beans.factory.xml.XmlBeanDefinitionReader#registerBeanDefinitions

//按照Spring的Bean语义要求将Bean定义资源解析并转换为容器内部数据结构
public int registerBeanDefinitions(Document doc, Resource resource) throws BeanDefinitionStoreException {
  //得到BeanDefinitionDocumentReader来对xml格式的BeanDefinition解析
  BeanDefinitionDocumentReader documentReader = createBeanDefinitionDocumentReader();
  //获得容器中注册的Bean数量
  int countBefore = getRegistry().getBeanDefinitionCount();
  //解析过程入口，这里使用了委派模式，BeanDefinitionDocumentReader只是个接口,
  //具体的解析实现过程有实现类DefaultBeanDefinitionDocumentReader完成
  documentReader.registerBeanDefinitions(doc, createReaderContext(resource));
  //统计解析的Bean数量
  return getRegistry().getBeanDefinitionCount() - countBefore;
}
```

#### 解析核心逻辑

```java
//使用Spring的Bean规则从Document的根元素开始进行Bean定义的Document对象
protected void parseBeanDefinitions(Element root, BeanDefinitionParserDelegate delegate) {
   //Bean定义的Document对象使用了Spring默认的XML命名空间
   if (delegate.isDefaultNamespace(root)) {
      //获取Bean定义的Document对象根元素的所有子节点
      NodeList nl = root.getChildNodes();
      for (int i = 0; i < nl.getLength(); i++) {
         Node node = nl.item(i);
         //获得Document节点是XML元素节点
         if (node instanceof Element) {
            Element ele = (Element) node;
            //Bean定义的Document的元素节点使用的是Spring默认的XML命名空间
            if (delegate.isDefaultNamespace(ele)) {
               //使用Spring的Bean规则解析元素节点
               parseDefaultElement(ele, delegate);
            }
            else {
               //没有使用Spring默认的XML命名空间，则使用用户自定义的解//析规则解析元素节点
               delegate.parseCustomElement(ele);
            }
         }
      }
   }
   else {
      //Document的根节点没有使用Spring默认的命名空间，则使用用户自定义的
      //解析规则解析Document根节点
      delegate.parseCustomElement(root);
   }
}
```