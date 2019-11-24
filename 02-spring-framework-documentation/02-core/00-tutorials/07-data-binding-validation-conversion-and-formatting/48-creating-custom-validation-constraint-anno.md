# 创建自定义的校验常量注解

Creating Custom Validation Constraint Annotation

在 Spring 中如果我么注册了`LocalValidatorFactoryBean`去引导`javax.validation.ValidatorFactory`,然后 custom `ConstrantValidator` class 就被 Spring Bean 加载了,这意味着我们可以通过 Spring 的依赖注入方式去校验类





## 代码实例

```java
/**
 * 本实例展示了如何使用注解的方式校验 bean中的某个参数是否是有效参数
 * 1. 创建自定义注解 {@link MyLanguage} ,标注有这个注解的 field 会进行是否有这个语言的校验
 * 2. 创建测试实体类 {@link ClientBean} 和 {@link Book}, 并在 Book 实体类的属性上加上我们刚刚创建的注解
 * 3. 创建语言提供者 {@link LanguageProvider},主要获取所有语言
 * 4. {@link LanguageValidator } 对标有注解的属性进行校验
 *
 * @author EricChen 2019/11/22 22:18
 */
@Configuration
@ComponentScan("cn.eccto.study.springframework.tutorials.messageResolver")
public class LanguageValidatorExample {

    @Bean
    public Validator validatorFactory() {
        return new LocalValidatorFactoryBean();
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(
                        LanguageValidatorExample.class);

        ClientBean bean = context.getBean(ClientBean.class);
        bean.run();
    }


}

```



```java

/**
 * 本实例展示了如何使用注解的方式校验 bean中的某个参数是否是有效参数
 * 1. 创建自定义注解 {@link MyLanguage} ,标注有这个注解的 field 会进行是否有这个语言的校验
 * 2. 创建测试实体类 {@link ClientBean} 和 {@link Book}, 并在 Book 实体类的属性上加上我们刚刚创建的注解
 * 3. 创建语言提供者 {@link LanguageProvider},主要获取所有语言
 * 4. {@link LanguageValidator } 对标有注解的属性进行校验
 * @author EricChen 2019/11/22 22:28
 */
public class Book {
    @NotNull
    private String name;
    @MyLanguage
    private String language;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
```



```java
/**
 * 本实例展示了如何使用注解的方式校验 bean中的某个参数是否是有效参数
 * 1. 创建自定义注解 {@link MyLanguage} ,标注有这个注解的 field 会进行是否有这个语言的校验
 * 2. 创建测试实体类 {@link ClientBean} 和 {@link Book}, 并在 Book 实体类的属性上加上我们刚刚创建的注解
 * 3. 创建语言提供者 {@link LanguageProvider},主要获取所有语言
 * 4. {@link LanguageValidator } 对标有注解的属性进行校验
 *
 * @author EricChen 2019/11/22 22:29
 */
@Component
public class ClientBean {
    @Autowired
    private Validator validator;

    public void run() {
        Book book = new Book();
        book.setName("Alien Explorer");
        book.setLanguage("Chines");

        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        for (ConstraintViolation<Book> violation : violations) {
            String message = violation.getMessage();
            Path path = violation.getPropertyPath();
            System.err.println(path + " " + message);
        }
    }
}
```

```java

/**
 * 本实例展示了如何使用注解的方式校验 bean中的某个参数是否是有效参数
 * 1. 创建自定义注解 {@link MyLanguage} ,标注有这个注解的 field 会进行是否有这个语言的校验
 * 2. 创建测试实体类 {@link ClientBean} 和 {@link Book}, 并在 Book 实体类的属性上加上我们刚刚创建的注解
 * 3. 创建语言提供者 {@link LanguageProvider},主要获取所有语言
 * 4. {@link LanguageValidator } 对标有注解的属性进行校验
 *
 * @author EricChen 2019/11/22 22:31
 */
@Component
public class DefaultMyLanguageProvider implements LanguageProvider {

    @Override
    public List<String> getLanguages() {
        List<String> languageList = new ArrayList<>();
        for (Locale locale : Locale.getAvailableLocales()) {
            languageList.add(locale.getDisplayLanguage());
        }
        return languageList;
    }
}


```

```java

/**
 * 本实例展示了如何使用注解的方式校验 bean中的某个参数是否是有效参数
 * 1. 创建自定义注解 {@link MyLanguage} ,标注有这个注解的 field 会进行是否有这个语言的校验
 * 2. 创建测试实体类 {@link ClientBean} 和 {@link Book}, 并在 Book 实体类的属性上加上我们刚刚创建的注解
 * 3. 创建语言提供者 {@link LanguageProvider},主要获取所有语言
 * 4. {@link LanguageValidator } 对标有注解的属性进行校验
 *
 * @author EricChen 2019/11/22 22:30
 */
public interface LanguageProvider {

    List<String> getLanguages();

}

```

```java
/**
 * 本实例展示了如何使用注解的方式校验 bean中的某个参数是否是有效参数
 * 1. 创建自定义注解 {@link MyLanguage} ,标注有这个注解的 field 会进行是否有这个语言的校验
 * 2. 创建测试实体类 {@link ClientBean} 和 {@link Book}, 并在 Book 实体类的属性上加上我们刚刚创建的注解
 * 3. 创建语言提供者 {@link LanguageProvider},主要获取所有语言
 * 4. {@link LanguageValidator } 对标有注解的属性进行校验
 *
 * @author EricChen 2019/11/22 22:22
 */
public class LanguageValidator implements ConstraintValidator<MyLanguage, String> {
    @Autowired
    private LanguageProvider languageProvider;

    public void initialize(Language constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return languageProvider.getLanguages()
                .stream()
                .anyMatch(value::equalsIgnoreCase);
    }
}
```

```java
/**
 * 本实例展示了如何使用注解的方式校验 bean中的某个参数是否是有效参数
 * 1. 创建自定义注解 {@link MyLanguage} ,标注有这个注解的 field 会进行是否有这个语言的校验
 * 2. 创建测试实体类 {@link ClientBean} 和 {@link Book}, 并在 Book 实体类的属性上加上我们刚刚创建的注解
 * 3. 创建语言提供者 {@link LanguageProvider},主要获取所有语言
 * 4. {@link LanguageValidator } 对标有注解的属性进行校验
 *
 * @author EricChen 2019/11/22 22:14
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LanguageValidator.class)
public @interface MyLanguage {

    String message() default "must be a valid language display name found: ${validatedValue}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

```

