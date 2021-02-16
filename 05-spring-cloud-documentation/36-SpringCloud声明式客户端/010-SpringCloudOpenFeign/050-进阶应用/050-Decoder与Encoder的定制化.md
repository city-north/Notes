# 050-Decoder与Encoder的定制化

[TOC]

## 为什么要定制Encoder和Decoder

Encoder用于将Object对象转化为HTTP的请求Body，而Decoder用于将网络响应转化为对应的Object对象。对于二者，OpenFeign都提供了默认的实现，但是使用者可以根据自己的业务来选择其他的编解码方式。

只需要在自定义配置类中给出Decoder和Encoder的自定义Bean实例，那么OpenFeign就可以根据配置，自动使用我们提供的自定义实例进行编解码操作如下代码所示，CustomFeignConfig配置类将ResponseEntityDecoder和SpringEncoder配置为Feign的Decoder与Encoder实例。

```java
public class CustomFeignConfig {
    @Bean
    public Decoder feignDecoder() {
        HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter (customObjectMapper());
        ObjectFactory〈HttpMessageConverters〉 objectFactory = () -〉 new HttpMessage Converters(jacksonConverter);
        return new ResponseEntityDecoder(new SpringDecoder(objectFactory));
    }
    @Bean
    public Encoder feignEncoder(){
        HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter (customObjectMapper());
        ObjectFactory〈HttpMessageConverters〉 objectFactory = () -〉 new HttpMessage Converters(jacksonConverter);
        return new SpringEncoder(objectFactory);
    }
    public ObjectMapper customObjectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        return objectMapper;
    }
}
```

MappingJackson2HttpMessageConverter是转换JSON的底层转换器