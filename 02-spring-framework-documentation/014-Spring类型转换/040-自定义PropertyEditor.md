# 040-自定义PropertyEditor

[TOC]

## 拓展模式

- 拓展 java.beans.PropertyEditorSupport 类

- 实现 org.springframework.beans.PropertyEditorRegistrar
  - 调用`void registerCustomEditors(PropertyEditorRegistry registry);` 方法
  - `PropertyEditorRegistrar`实现注册为SpringBean

- 向 `org.springframework.beans.PropertyEditorRegistry` 注册自定义 PropertyEditor实现
  - 通用类型`void registerCustomEditor(Class<?> requiredType, PropertyEditor propertyEditor);`
  - JavaBean属性类型实现`void registerCustomEditor(@Nullable Class<?> requiredType, @Nullable String propertyPath, PropertyEditor propertyEditor);`

