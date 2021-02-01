# 031-BeanFactory准备阶段-增加对属性编辑器的支持

[TOC]



## 一言蔽之



## BeanFactory准备阶段-增加对属性编辑器的支持

```java
protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
  // Tell the internal bean factory to use the context's class loader etc.
  beanFactory.setBeanClassLoader(getClassLoader());
  beanFactory.setBeanExpressionResolver(new StandardBeanExpressionResolver(beanFactory.getBeanClassLoader()));

  //----------↓↓↓↓↓↓-----本段关注的重点---------↓↓↓↓↓↓↓------------//
  beanFactory.addPropertyEditorRegistrar(new ResourceEditorRegistrar(this, getEnvironment()));
  //----------↑↑↑↑↑↑-----本段关注的重点---------↑↑↑↑↑↑------------//

}
```



## 什么是属性编辑器

在Spring DI注入的时候可以把普通属性注入进来，但是像Date类型就无法被识别。例如：

```java
public class UserManager {  
    private Date dataValue;  

    public Date getDataValue() {  
        return dataValue;  
    }  

   public void setDataValue(Date dataValue) {  
        this.dataValue = dataValue;  
    }  

   public String toString(){  
        return "dataValue: " + dataValue;  
    }  
 }  

```

上面代码中，需要对日期型属性进行注入：

```xml
<bean id="userManager" class="com.test.UserManager">  
    <property name="dataValue">  
        <value>2013-03-15</value>  
    </property>  
</bean>
```

测试代码：

```java
@Test  
public void testDate(){  
    ApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");  
    UserManager userManager = (UserManager)ctx.getBean("userManager");  
    System.out.println(userManager);  
}
```

如果直接这样使用，程序则会报异常，类型转换不成功。因为在UserManager中的dataValue属性是Date类型的，而在XML中配置的却是String类型的，所以当然会报异常。

Spring提供了两种方式解决

- 使用自定义属性编辑器
- 将自定义属性编辑器注册到Spring

#### 使用自定义属性编辑器

```java
public class DatePropertyEditor extends PropertyEditorSupport {  
    private String format = "yyyy-MM-dd";  
    public void setFormat(String format) {  
        this.format = format;  
    }  
    public void setAsText(String arg0) throws IllegalArgumentException {  
        System.out.println("arg0: " + arg0);  
        SimpleDateFormat sdf = new SimpleDateFormat(format);
      );  
        try {  
            Date d = sdf.parse(arg0);  
            this.setValue(d);  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
    }  
}  
```

将自定义属性编辑器注册到Spring中。

```xml
<!-- 自定义属性编辑器 -->  
<bean class="org.Springframework.beans.factory.config.CustomEditorConfigurer">  
     <property name="customEditors">  
                <map>  
               <entry key="java.util.Date">  
                <bean class="com.test.DatePropertyEditor">  
                       <property name="format" value="yyyy-MM-dd"/>  
                </bean>  
            		</entry>  
                </map>
		</property>  
</bean>
```


在配置文件中引入类型为`org.Springframework.beans.factory.config.CustomEditorConfigurer`的bean，并在属性customEditors中加入自定义的属性编辑器，其中key为属性编辑器所对应的类型。通过这样的配置，当Spring在注入bean的属性时一旦遇到了java.util.Date类型的属性会自动调用自定义的DatePropertyEditor解析器进行解析，并用解析结果代替配置属性进行注入。