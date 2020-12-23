# 070-DataBinder数据校验

[TOC]

```java
public void bind(PropertyValues pvs) {
   MutablePropertyValues mpvs = (pvs instanceof MutablePropertyValues ?
         (MutablePropertyValues) pvs : new MutablePropertyValues(pvs));
   doBind(mpvs);
}
```

```java
	protected void doBind(MutablePropertyValues mpvs) {
		checkAllowedFields(mpvs);//检查允许字段
		checkRequiredFields(mpvs);//检查必输字段
		applyPropertyValues(mpvs);
	}
```



```java
	protected void applyPropertyValues(MutablePropertyValues mpvs) {
		try {
			// Bind request parameters onto target object.
      //BeanPropertyBindingResult
			getPropertyAccessor().setPropertyValues(mpvs, isIgnoreUnknownFields(), isIgnoreInvalidFields());
		}
		catch (PropertyBatchUpdateException ex) {
			// Use bind error processor to create FieldErrors.
			for (PropertyAccessException pae : ex.getPropertyAccessExceptions()) {
				getBindingErrorProcessor().processPropertyAccessException(pae, getInternalBindingResult());
			}
		}
	}


```

