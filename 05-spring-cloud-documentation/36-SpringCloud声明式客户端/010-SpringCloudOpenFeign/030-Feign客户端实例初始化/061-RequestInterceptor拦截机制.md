# 061-RequestInterceptor拦截机制

[TOC]

## RequestInterceptor拦截机制

OpenFeign也提供了RequestInterceptor机制，在由RequestTemplate生成Request的过程中，会调用所有RequestInterceptor对RequestTemplate进行处理。

而Target是生成JAXRS 2.0网络请求Request的接口类。

RequestInterceptor处理的具体实现如下所示：

```java
//SynchronousMethodHandler.java
//按照RequestTemplate来创建Request
Request targetRequest(RequestTemplate template) {
    //使用请求拦截器为每个请求添加固定的header信息。例如BasicAuthRequestInterceptor，
    //它是添加Authorization header字段的
    for (RequestInterceptor interceptor : requestInterceptors) {
        interceptor.apply(template);
    }
    return target.apply(new RequestTemplate(template));
}
```

Client是用来发送网络请求的接口类，有OkHttpClient和RibbonClient两个子类。OkhttpClient调用OkHttp的相关组件进行网络请求的发送。OkHttpClient的具体实现如下所示：

```java
//OkHttpClient.java
public feign.Response execute(feign.Request input, feign.Request.Options options)
        throws IOException {
    //将feign.Request转换为Oktthp的Request对象
    Request request = toOkHttpRequest(input);
    //使用Okhttp的同步操作发送网络请求
    Response response = requestOkHttpClient.newCall(request).execute();
    //将Okhttp的Response转换为feign.Response
    return toFeignResponse(response).toBuilder().request(input).build();
}
```

