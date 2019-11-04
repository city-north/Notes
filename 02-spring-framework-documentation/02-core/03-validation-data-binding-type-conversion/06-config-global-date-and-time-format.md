# 配置一个全局的Date 和 Time 类型

默认情况下,没有标注 `@DateTimeFormat`注解的date 和time 字段默认转换时使用风格`DateFormat.SHORT`,你可以全局修改这个默认

```java
/**
 * Java配置方式注册全局的日期转换格式
 *
 *
 * @author EricChen 2019/11/04 18:32
 */
@Configuration
public class AppConfig {
    @Bean
    public FormattingConversionService conversionService() {

        // Use the DefaultFormattingConversionService but do not register defaults  
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService(false);

        // Ensure @NumberFormat is still supported 确保@NumberFormat 依然适用
        conversionService.addFormatterForFieldAnnotation(new NumberFormatAnnotationFormatterFactory());

        // Register date conversion with a specific global format //注册一个日期转换器,指定一个全局转换的格式
        DateFormatterRegistrar registrar = new DateFormatterRegistrar();
        registrar.setFormatter(new DateFormatter("yyyyMMdd"));
        registrar.registerFormatters(conversionService);

        return conversionService;
    }
}

```



配置文件方式:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
        http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

<bean id="conversionService" class="org.springframework.format.support.FormattingConversionServiceFactoryBean">
    <property name="registerDefaultFormatters" value="false"/>
    <property name="formatters">
        <set>
            <bean class="org.springframework.format.number.NumberFormatAnnotationFormatterFactory"/>
        </set>
    </property>
    <property name="formatterRegistrars">
        <set>
            <bean class="org.springframework.format.datetime.joda.JodaTimeFormatterRegistrar">
                <property name="dateFormatter">
                    <bean class="org.springframework.format.datetime.joda.DateTimeFormatterFactoryBean">
                        <property name="pattern" value="yyyyMMdd"/>
                    </bean>
                </property>
            </bean>
        </set>
    </property>
</bean>
</beans>
```

