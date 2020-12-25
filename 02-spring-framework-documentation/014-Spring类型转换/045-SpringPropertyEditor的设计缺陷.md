# 045-SpringPropertyEditor的设计缺陷

[TOC]

## 为什么基于PropertyEditor拓展并不合适作为类型转换

- 违反职责单一原则
  - java.beans.PropertyEditor 接口职责太多,除了类型转换,还包括JavaBeans 实现和Java GUI交互
- java.beans.PropertyEditor实现类型局限
  - 来源类型只能为java.lang.String类型

- java.beansPropertyEditor实现缺少类型安全
  - 除了实现类命名可以表达语义,实现类无法感知目标转换类型

```java
  public void setValue(Object value) {
        this.value = value;
        firePropertyChange();
    }

    public Object getValue() {
        return value;
    }
```

setValue和getValue缺少类型安全，无法感知目标准换类型

所以Spring对API进行了改造与重构