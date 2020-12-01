# 060-通用型BeanDefinition

![image-20201116213208976](../../assets/image-20201116213208976.png)

- BeanDefinition
  - AbstractBeanDefinition
    - GenericBeanDefinition
  - AnnotatedBeanDefinition
  - RootBeanDefinition

## GenericBeanDefinition

```java
public class GenericBeanDefinition extends AbstractBeanDefinition {
	//获取双亲BeanDefinition
   @Nullable
   private String parentName;

   /**
    * Create a new GenericBeanDefinition, to be configured through its bean
    * properties and configuration methods.
    * @see #setBeanClass
    * @see #setScope
    * @see #setConstructorArgumentValues
    * @see #setPropertyValues
    */
   public GenericBeanDefinition() {
      super();
   }

   /**
    * Create a new GenericBeanDefinition as deep copy of the given
    * bean definition.
    * @param original the original bean definition to copy from
    */
   public GenericBeanDefinition(BeanDefinition original) {
      super(original);
   }


   @Override
   public void setParentName(@Nullable String parentName) {
      this.parentName = parentName;
   }

   @Override
   @Nullable
   public String getParentName() {
      return this.parentName;
   }


   @Override
   public AbstractBeanDefinition cloneBeanDefinition() {
      return new GenericBeanDefinition(this);
   }


}
```

## AbstractBeanDefinition