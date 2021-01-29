# 012-Spring校验

-  [010-Spring校验使用场景.md](010-Spring校验使用场景.md) 
-  [020-Validator接口设计.md](020-Validator接口设计.md) 
-  [030-Errors接口设计.md](030-Errors接口设计.md) 
-  [040-Errors文案来源.md](040-Errors文案来源.md) 
-  [050-自定义Validator.md](050-自定义Validator.md) 

## 校验组件

| 组件名称                 | 类                                                           |
| ------------------------ | ------------------------------------------------------------ |
| 校验器                   | org.springframework.validation.Validator                     |
| 错误收集器               | org.springframework.validation.Errors                        |
| Java Bean 错误描述       | org.springframework.validation.ObjectError                   |
| Java Bean  属性 错误描述 | org.springframework.validation.FieldError                    |
| Bean Validation 适配     | org.springframework.validation.beanvalidation.LocalValidatorFactoryBean |
| 后置处理器               | org.springframework.validation.beanvalidation.MethodValidationPostProcessor |

