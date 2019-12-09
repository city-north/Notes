# 管理 HttpMessageConverters

`HttpMessageConverters` 提供了一个方便的方法管理 HttpMessageConverters

他可以被注册成为一个 bean,我们可以用以下共有的构造方法:

- ```
   HttpMessageConverters(HttpMessageConverter<?>... additionalConverters)
  ```

- ```
   HttpMessageConverters(Collection<HttpMessageConverter<?>> additionalConverters)
  ```

- ```
  HttpMessageConverters(boolean addDefaultConverters, Collection<HttpMessageConverter<?>> converters)
  ```

## 代码实例

```java
/**
 * 通过 {@link HttpMessageConverters} 来注册一个 {@link StringHttpMessageConverter} 的实现类
 *
 * @author EricChen 2019/12/06 19:24
 */
@SpringBootApplication
public class MessageConverterExample {

    @Bean
    public HttpMessageConverters addMessageConverter(){
        return new HttpMessageConverters(new TheCustomConverter());
    }

    public static void main(String[] args) {
        SpringApplication.run(MessageConverterExample.class);
    }


}

class TheCustomConverter extends StringHttpMessageConverter {

}


@Component
class RefreshListener {
    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        handlerAdapter.getMessageConverters()
                .stream()
                .forEach(System.out::println);
    }
}
```

输出

```
org.springframework.http.converter.ByteArrayHttpMessageConverter@7ecec90d
cn.eccto.study.sb.messageConverter.TheCustomConverter@588f63c
org.springframework.http.converter.StringHttpMessageConverter@5a6fa56e
org.springframework.http.converter.ResourceHttpMessageConverter@1981d861
org.springframework.http.converter.ResourceRegionHttpMessageConverter@118ffcfd
org.springframework.http.converter.xml.SourceHttpMessageConverter@53f4c1e6
org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter@74174a23
org.springframework.http.converter.json.MappingJackson2HttpMessageConverter@6342d610
org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter@dc4a691
```

可以看到

```
cn.eccto.study.sb.messageConverter.TheCustomConverter@588f63c
```

## 替换掉默认的 Converter

```java
/**
 * 通过 {@link HttpMessageConverters} 来注册一个 {@link StringHttpMessageConverter} 的实现类
 *
 * @author EricChen 2019/12/06 19:24
 */
@SpringBootApplication
public class MessageConverterExample {

//    @Bean
//    public HttpMessageConverters addMessageConverter(){
//        return new HttpMessageConverters(new TheCustomConverter());
//    }

    /**
     * 替换掉默认的 HttpMessageConverters
     *
     * @return
     */
    @Bean
    public HttpMessageConverters addMessageConverter() {
        return new HttpMessageConverters(
                false, Arrays.asList(new TheCustomConverter()));
    }

    public static void main(String[] args) {
        SpringApplication.run(MessageConverterExample.class);
    }


}

class TheCustomConverter extends StringHttpMessageConverter {

}


@Component
class RefreshListener {
    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        handlerAdapter.getMessageConverters()
                .stream()
                .forEach(System.out::println);
    }
}
```

输出

```
cn.eccto.study.sb.messageConverter.TheCustomConverter@6ad16c5d

```

## 直接注册 HttpMessageConverter 成为一个 Bean

```java

/**
 * 通过 {@link HttpMessageConverters} 来注册一个 {@link StringHttpMessageConverter} 的实现类
 *
 * @author EricChen 2019/12/06 19:24
 */
@SpringBootApplication
public class MessageConverterExample {

    /**
     * 在原有默认的基础上添加一个新的 TheCustomConverter
     */
//    @Bean
//    public HttpMessageConverters addMessageConverter(){
//        return new HttpMessageConverters(new TheCustomConverter());
//    }

    /**
     * 替换掉默认的 HttpMessageConverters
     */
//    @Bean
//    public HttpMessageConverters addMessageConverter() {
//        return new HttpMessageConverters(
//                false, Arrays.asList(new TheCustomConverter()));
//    }

    /**
     * 直接注册为 Bean
     */
    @Bean
    public HttpMessageConverter<?> messageConverter(){
        return new TheCustomConverter();
    }


    public static void main(String[] args) {
        SpringApplication.run(MessageConverterExample.class);
    }


}

class TheCustomConverter extends StringHttpMessageConverter {

}


@Component
class RefreshListener {
    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        handlerAdapter.getMessageConverters()
                .stream()
                .forEach(System.out::println);
    }
}
```

输出

```
org.springframework.http.converter.ByteArrayHttpMessageConverter@6b4283c4
cn.eccto.study.sb.messageConverter.TheCustomConverter@211febf3
org.springframework.http.converter.StringHttpMessageConverter@3bd3d05e
org.springframework.http.converter.StringHttpMessageConverter@d0865a3
org.springframework.http.converter.ResourceHttpMessageConverter@636bbbbb
org.springframework.http.converter.ResourceRegionHttpMessageConverter@7eae3764
org.springframework.http.converter.xml.SourceHttpMessageConverter@10dc7d6
org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter@4f668f29
org.springframework.http.converter.json.MappingJackson2HttpMessageConverter@6aba5d30
org.springframework.http.converter.json.MappingJackson2HttpMessageConverter@716e431d
org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter@7e744f43
```

