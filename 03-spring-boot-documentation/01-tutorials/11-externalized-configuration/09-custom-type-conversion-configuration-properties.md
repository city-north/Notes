# Custom Type Conversion with @ConfigurationProperties

上一章介绍了 Spring 当绑定到`@ConfigurationProperties `的时候,如何自动的转换外部配置到一个自定义的类型

如何自定义类型转换

- 自定义一个`Converter`
- 通过`@ConfigurationPropertiesBinding`注解来注册

## Example

#### src/main/resources/application.properties

```java
app.exit-on-errors=true
app.trade-start-date=03-25-2016
```

上面的属性转换为我们指定的 Bean

```java
@Component
@ConfigurationProperties("app")
public class MyAppProperties {
  private boolean exitOnErrors;
  private LocalDate tradeStartDate;
    .............
}
```

#### 自定义 Converter

- 标注注解`@ConfigurationPropertiesBinding`
- `ConfigurationPropertiesBindingPostProcessor`类负责绑定属性到`@ConfigurationProperties`Bean
- `ConfigurationPropertiesBindingPostProcessor`是一个BeanPostProcessor 实现,检测自定义转换器

```java
@Component
@ConfigurationPropertiesBinding
public class LocalDateConverter implements Converter<String, LocalDate> {

    @Override
    public LocalDate convert(String source) {
        if(source==null){
            return null;
        }
        return LocalDate.parse(source, DateTimeFormatter.ofPattern("MM-dd-yyyy"));
    }
}
```

main方法

```java
@SpringBootApplication
public class CustomConverterExample {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(CustomConverterExample.class)
                .profiles("custom-converter")
                .web(WebApplicationType.NONE)
                .run();
        MyAppProperties bean = context.getBean(MyAppProperties.class);
        System.out.println(bean);
    }

}
```

输出

```
MyAppProperties(exitOnErrors=true, tradeStartDate=2016-03-25)
```

## 值得注意的是

- 除了使用`Converter`自定义转换器我们还有其他的方式

Spring 文档:

> If you need custom type conversion you can provide a ConversionService bean (with bean id conversionService) or custom property editors (via a CustomEditorConfigurer bean) or custom Converters (with bean definitions annotated as @ConfigurationPropertiesBinding).

如果需要自定义类型转换，可以提供一个`ConversionService `bean(bean id 是 `ConversionService`)或自定义属性编辑器(通过`CustomEditorConfigurer` bean)或自定义转换器(使用注释为`@ConfigurationPropertiesBinding`的bean定义)。