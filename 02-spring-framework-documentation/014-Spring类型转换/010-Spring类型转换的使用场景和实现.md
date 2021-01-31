# 010-Spring类型转换的使用场景和实现

[TOC]

## Spring类型转换的历史

- Spring3.0之前，基于 JavaBeans 接口的类型转换实现
  
  - 基于 java.beans.PropertyEditor 接口拓展
  
- Spring3.0之后，通用类型转换实现

## Spring类型转换的使用场景

| 场景                                                         | 基于JavaBeans接口的类型转换实现PropertyEditor | Spring3.0+通用类型的实现 |
| ------------------------------------------------------------ | --------------------------------------------- | ------------------------ |
| [数据绑定](011-数据绑定-DataBinder中类型转换的实现.md)       | 是                                            | 是                       |
| [BeanWrapper](012-BeanWrapper中数据绑定的实现.md)            | 是                                            | 是                       |
| [Bean属性类型转换](013-Bean属性类型转换中数据绑定的实现.md)  | 是                                            | 是                       |
| [外部化配置类型转换](014-外部化配置类转换中类型转换的实现.md) | **否**（SpringBoot使用到了）                  | 是                       |

Bean属性类型转换主要是在BeanFactory在构造Bean的时候使用BeanWrapper进行类型转换

SpringBoot在处理外部化配置类型转换使用到了PropertyEditor

## BeanWrapper

 [012-BeanWrapper中数据绑定的实现.md](012-BeanWrapper中数据绑定的实现.md) 

## Bean属性类型转换

 [013-Bean属性类型转换中数据绑定的实现.md](013-Bean属性类型转换中数据绑定的实现.md) 

## 外部化配置类型转换

 [014-外部化配置类转换中类型转换的实现.md](014-外部化配置类转换中类型转换的实现.md) 