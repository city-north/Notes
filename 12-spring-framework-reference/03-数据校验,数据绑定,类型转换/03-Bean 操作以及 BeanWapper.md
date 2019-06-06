# Bean Manipulation and the BeanWrapper
Bean操作和`BeanWrapper`

`org.springframework.beans.BeanWrapper `接口和其实现类`org.springframework.beans.BeanWrapperImpl ` 、`BeanWrapper`提供了
- 设置和获取属性值(单独或批量)、获取属性描述符以及查询属性以确定它们是否可读或可写的功能。
- 提供了对嵌套属性的支持，允许在子属性上设置无限深度的属性。
- 支持添加标准javabean `PropertyChangeListeners`和`VetoableChangeListeners`的能力，而不需要在目标类中支持代码。
- `BeanWrapper`提供了对设置索引属性的支持。
- `BeanWrapper`通常不被应用程序代码直接使用，而是被`DataBinder`和`BeanFactory`使用。

## Setting and Getting Basic and Nested Properties
## 设置和获取基本的和嵌套的属性

```java
public class Company {

    private String name;
    private Employee managingDirector;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Employee getManagingDirector() {
        return this.managingDirector;
    }

    public void setManagingDirector(Employee managingDirector) {
        this.managingDirector = managingDirector;
    }
}
```
```java
public class Employee {

    private String name;

    private float salary;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }
}
```
使用`BeanWapper`设置一个 Bean 的属性使用方式:

```
BeanWrapper company = new BeanWrapperImpl(new Company());
// 设置 company 的 name
company.setPropertyValue("name", "Some Company Inc.");
// 设置 company 的 name的另外一种方式
PropertyValue value = new PropertyValue("name", "Some Company Inc.");
company.setPropertyValue(value);

// ok, let's create the director and tie it to the company:
BeanWrapper jim = new BeanWrapperImpl(new Employee());
jim.setPropertyValue("name", "Jim Stravinsky");
company.setPropertyValue("managingDirector", jim.getWrappedInstance());

// retrieving the salary of the managingDirector through the company
Float salary = (Float) company.getPropertyValue("managingDirector.salary");
```

## Built-in PropertyEditor Implementations
## 内置的 PropertyEditor 实现类
`PropertyEditor ` 可以用来将一个 `Object`转化成 `Spring` ,用不同于对象本身的方式表示属性是很方便,比如,一个`Date`类型可以使用一个更可读的字符串`2019-09-09`来代替,然后可以将这个字符串再转化成 Date 类型的数据,这种行为可以注册一个自定义`editor`来实现,也就是`java.beans.PropertyEditor`这个类

下面有两个例子:
- 在bean上设置属性是通过使用PropertyEditor实现实现的。当您使用String作为在XML文件中声明的某个bean的属性的值时，Spring(如果相应属性的setter有一个类参数)使用ClassEditor尝试将该参数解析为一个类对象。
- 在Spring的MVC框架中解析HTTP请求参数是通过使用各种`PropertyEditor`实现来完成的，您可以在`CommandController`的所有子类中手动绑定这些实现。




| Class                     | Explanation                                                  |
| :------------------------ | :----------------------------------------------------------- |
| `ByteArrayPropertyEditor` | Editor for byte arrays. Converts strings to their corresponding byte representations. Registered by default by `BeanWrapperImpl`. |
| `ClassEditor`             | Parses Strings that represent classes to actual classes and vice-versa. When a class is not found, an `IllegalArgumentException` is thrown. By default, registered by`BeanWrapperImpl`. |
| `CustomBooleanEditor`     | Customizable property editor for `Boolean` properties. By default, registered by`BeanWrapperImpl` but can be overridden by registering a custom instance of it as a custom editor. |
| `CustomCollectionEditor`  | Property editor for collections, converting any source `Collection` to a given target`Collection` type. |
| `CustomDateEditor`        | Customizable property editor for `java.util.Date`, supporting a custom `DateFormat`. NOT registered by default. Must be user-registered with the appropriate format as needed. |
| `CustomNumberEditor`      | Customizable property editor for any `Number` subclass, such as `Integer`, `Long`, `Float`, or `Double`. By default, registered by `BeanWrapperImpl` but can be overridden by registering a custom instance of it as a custom editor. |
| `FileEditor`              | Resolves strings to `java.io.File` objects. By default, registered by`BeanWrapperImpl`. |
| `InputStreamEditor`       | One-way property editor that can take a string and produce (through an intermediate `ResourceEditor` and `Resource`) an `InputStream` so that `InputStream` properties may be directly set as strings. Note that the default usage does not close the `InputStream` for you. By default, registered by `BeanWrapperImpl`. |
| `LocaleEditor`            | Can resolve strings to `Locale` objects and vice-versa (the string format is `*[country]*[variant]`, same as the `toString()` method of `Locale`). By default, registered by `BeanWrapperImpl`. |
| `PatternEditor`           | Can resolve strings to `java.util.regex.Pattern` objects and vice-versa. |
| `PropertiesEditor`        | Can convert strings (formatted with the format defined in the javadoc of the`java.util.Properties` class) to `Properties` objects. By default, registered by `BeanWrapperImpl`. |
| `StringTrimmerEditor`     | Property editor that trims strings. Optionally allows transforming an empty string into a `null` value. NOT registered by default — must be user-registered. |
| `URLEditor`               | Can resolve a string representation of a URL to an actual `URL` object. By default, registered by `BeanWrapperImpl`. |

## Registering Additional Custom `PropertyEditor` Implementations

## 注册一个额外的自定义的`PropertyEditor`实现类

Java的标准 JavaBeans `PropertyEditor` 查找机制允许`PropertyEditor`对类的`PropertyEditor`进行适当的命名,并将其放置在与其提供支持的类相同的包中,以便能够自动找到该类

使用一下方式来注册一个自定义的`PropertyEditors`:

- 手动注册(不推荐):使用`ConfigurableBeanFactory`接口内的`registerCustomEditor()`方法
- (推荐)使用 Bean 工程的前置执行器(post-processer)`CustomEditorConfigurer`

下面这个例子定义了一个`ExoticType`和`DependsOnExoticType`

```java
package example;

public class ExoticType {

    private String name;

    public ExoticType(String name) {
        this.name = name;
    }
}

public class DependsOnExoticType {
		// 引用了ExoticType类型的参数
    private ExoticType type;

    public void setType(ExoticType type) {
        this.type = type;
    }
}
```

定义一个`PropertyEditor`实现类看起来很相似

```java
// converts string representation to  ExoticType 类型 object
// 将 String 类型转换为 ExoticType 类型
package example;

public class ExoticTypeEditor extends PropertyEditorSupport {

    public void setAsText(String text) {
        setValue(new ExoticType(text.toUpperCase()));
    }
}
```

将它注册到 Spring

```xml
<bean class="org.springframework.beans.factory.config.CustomEditorConfigurer">
    <property name="customEditors">
        <map>
          <!-- key 为类型, value 为Editor -->
            <entry key="example.ExoticType" value="example.ExoticTypeEditor"/>
        </map>
    </property>
</bean>
```

### 使用 `PropertyEditorRegistrar`

另外一个注册属性编辑器(property editors)的机制(Spring 容器中的属性) 是创建并使用`PropertyEditorRegistrar`

当您需要在几种不同的情况下使用同一组属性编辑器时，此接口尤其有用。编写相应的注册表，并在每种情况下重用它。

`PropertyEditorRegistrar`实例与名为`PropertyEditorRegistry`的接口一起工作

``BeanWrapper` 和`DataBinder`实现了`PropertyEditorRegistry`这个接口

下面这个例子显示了如何去创建一个`PropertyEditorRegisterar`实现类

```java
package com.foo.editors.spring;

public final class CustomPropertyEditorRegistrar implements PropertyEditorRegistrar {

    public void registerCustomEditors(PropertyEditorRegistry registry) {

        // it is expected that new PropertyEditor instances are created
        registry.registerCustomEditor(ExoticType.class, new ExoticTypeEditor());

        // you could register as many custom property editors as are required here...
    }
}
```

还有一例子是 Spring 内部的`org.springframework.beans.support.ResourceEditorRegistrar` 也是一个`PropertyEditorRegistrar`实现类,请注意这个实现类的`registerCustomEditors(..)`,它为每个属性编辑器创建新的实例。

The next example shows how to configure a `CustomEditorConfigurer` and inject an instance of our`CustomPropertyEditorRegistrar` into it:

下面这个例子展示了如何配置一个`CustomEditorConfigurer`然后将我们的`CustomPropertyEditorRegistrar`的一个实例注入其中:

```xml
<bean class="org.springframework.beans.factory.config.CustomEditorConfigurer">
    <property name="propertyEditorRegistrars">
        <list>
            <ref bean="customPropertyEditorRegistrar"/>
        </list>
    </property>
</bean>

<bean id="customPropertyEditorRegistrar"
    class="com.foo.editors.spring.CustomPropertyEditorRegistrar"/>
```

最后,使用`PropertyEditorRegistrars`结合数据绑定 Controller 例如(`SimpleFormController`)就会非常方便了,下面这个例子使用了`PropertyEditorRegistrar`在实现类的`initBinder(…)`方法:



```java
public final class RegisterUserController extends SimpleFormController {

    private final PropertyEditorRegistrar customPropertyEditorRegistrar;

    public RegisterUserController(PropertyEditorRegistrar propertyEditorRegistrar) {
        this.customPropertyEditorRegistrar = propertyEditorRegistrar;
    }

    protected void initBinder(HttpServletRequest request,
            ServletRequestDataBinder binder) throws Exception {
        this.customPropertyEditorRegistrar.registerCustomEditors(binder);
    }

    // other methods to do with registering a User
}
```

