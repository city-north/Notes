# 040-Feign客户端实例初始化-扫描函数信息

[TOC]

## 一言蔽之









在扫描FeignClient接口类所有函数生成对应Handler的过程中，OpenFeign会生成调用该函数时发送网络请求的模板，也就是RequestTemplate实例。

RequestTemplate中包含了发送网络请求的URL和函数参数填充的信息。@RequestMapping、@PathVariable等注解信息也会包含到RequestTemplate中，用于函数参数的填充。

ParseHandlersByName类的apply方法就是这一过程的具体实现。它首先会使用Contract来解析接口类中的函数信息，并检查函数的合法性，然后根据函数的不同类型来为每个函数生成一个BuildTemplateByResolvingArgs对象，最后使用SynchronousMethodHandler.Factory来创建MethodHandler实例。ParseHandlersByName的apply实现如下代码所示：

```java
//ParseHandlersByName.java
public Map〈String, MethodHandler〉 apply(Target key) {
    // 获取type的所有方法的信息,会根据注解生成每个方法的RequestTemplate
    List〈MethodMetadata〉 metadata = contract.parseAndValidatateMetadata(key.type());
    Map〈String, MethodHandler〉 result = new LinkedHashMap〈String, MethodHandler〉();
    for (MethodMetadata md : metadata) {
    BuildTemplateByResolvingArgs buildTemplate;
    if (!md.formParams().isEmpty() && md.template().bodyTemplate() == null) {
        buildTemplate = new BuildFormEncodedTemplateFromArgs(md, encoder);
    } else if (md.bodyIndex() != null) {
        buildTemplate = new BuildEncodedTemplateFromArgs(md, encoder);
    } else {
        buildTemplate = new BuildTemplateByResolvingArgs(md);
    }
    result.put(md.configKey(),
        factory.create(key, md, buildTemplate, options, decoder, errorDecoder));
    }
    return result;
}
```



## SpringMvcContract:Openfeign默认协议实现

OpenFeign默认的Contract实现是SpringMvcContract。SpringMvcContract的父类为BaseContract，而BaseContract是Contract众多子类中的一员，其他还有JAXRSContract和HystrixDelegatingContract等。

Contract的parseAndValidateMetadata方法会解析与HTTP请求相关的所有函数的基本信息和注解信息，代码如下所示：

```java
//SpringMvcContract.java
@Override
public MethodMetadata parseAndValidateMetadata(Class〈?〉 targetType, Method method) {
    this.processedMethods.put(Feign.configKey(targetType, method), method);
    //调用父类BaseContract的函数
    MethodMetadata md = super.parseAndValidateMetadata(targetType, method);
    RequestMapping classAnnotation = findMergedAnnotation(targetType,
            RequestMapping.class);
    //处理RequestMapping注解
    if (classAnnotation != null) {
        if (!md.template().headers().containsKey(ACCEPT)) {
            parseProduces(md, method, classAnnotation);
        }
        if (!md.template().headers().containsKey(CONTENT_TYPE)) {
            parseConsumes(md, method, classAnnotation);
        }
        parseHeaders(md, method, classAnnotation);
    }
    return md;
}
```

BaseContract的parseAndValidateMetadata方法会依次解析接口类的注解，函数注解和函数的参数注解，将这些注解包含的信息封装到MethodMetadata对象中，然后返回，代码如下所示：

```java
//BaseContract.java
protected MethodMetadata parseAndValidateMetadata(Class〈?〉 targetType, Method method) {
    MethodMetadata data = new MethodMetadata();
    //函数的返回值
    data.returnType(Types.resolve(targetType, targetType, method.getGenericReturnType()));
    //函数Feign相关的唯一配置键
    data.configKey(Feign.configKey(targetType, method));
    //获取并处理修饰class的注解信息
    if(targetType.getInterfaces().length == 1) {
        processAnnotationOnClass(data, targetType.getInterfaces()[0]);
    }
    //调用子类processAnnotationOnClass的实现
    processAnnotationOnClass(data, targetType);
    //处理修饰method的注解信息
    for (Annotation methodAnnotation : method.getAnnotations()) {
        processAnnotationOnMethod(data, methodAnnotation, method);
    }
    //函数参数类型
    Class〈?〉[] parameterTypes = method.getParameterTypes();
    Type[] genericParameterTypes = method.getGenericParameterTypes();
    //函数参数的注解类型
    Annotation[][] parameterAnnotations = method.getParameterAnnotations();
    int count = parameterAnnotations.length;
    //依次处理各个函数参数注解
    for (int i = 0; i 〈 count; i++) {
        boolean isHttpAnnotation = false;
        if (parameterAnnotations[i] != null) {
            // 处理参数的注解，并且返回该参数来指明是否为将要发送请求的body。除了body之外，还
               可能是path，param等
            isHttpAnnotation = processAnnotationsOnParameter(data, parameterAnnotations[i], i);
        }
        if (parameterTypes[i] == URI.class) {
            data.urlIndex(i);
        } else if (!isHttpAnnotation) {
            //表明发送请求body的参数位置和参数类型
            data.bodyIndex(i);
            data.bodyType(Types.resolve(targetType, targetType, genericParameterTypes[i]));
        }
    }
    return data;
}
```

processAnnotationOnClass方法用于处理接口类注解。该函数在parseAndValidateMetadata方法中可能会被调用两次，如果targetType只继承或者实现一种接口时，先处理该接口的注解，再处理targetType的注解；否则只会处理targetType的注解。@RequestMapping在修饰FeignClient接口类时，其value所代表的值会被记录下来，它是该FeignClient下所有请求URL的前置路径，处理接口类注解的函数代码如下所示：

```java
//SpringMvcContract.java
protected void processAnnotationOnClass(MethodMetadata data, Class〈?〉 clz) {
    if (clz.getInterfaces().length == 0) {
        //获取RequestMapping的注解信息，并设置MethodMetadata.template的数据
        RequestMapping classAnnotation = findMergedAnnotation(clz,
                RequestMapping.class);
        if (classAnnotation != null) {
            if (classAnnotation.value().length 〉 0) {
                String pathValue = emptyToNull(classAnnotation.value()[0]);
                pathValue = resolve(pathValue);
                if (!pathValue.startsWith("/")) {
                    pathValue = "/" + pathValue;
                }
                //处理@RequestMapping的value,一般都是发送请求的path
                data.template().insert(0, pathValue);
            }
        }
    }
}
```



processAnnotationOnMethod方法的主要作用是处理修饰函数的注解。它会首先校验该函数是否被@RequestMapping修饰，如果没有就会直接返回。然后获取该函数所对应的HTTP请求的方法，默认的方法是GET。接着会处理@RequestMapping中的value属性，解析value属性中的pathValue，比如说value属性值为/instance/{instanceId}，那么pathValue的值就是instanceId。最后处理消费(consumes)和生产(produces)相关的信息，记录媒体类型(media types)，代码如下所示：

```java
//SpringMvcContract.java
protected void processAnnotationOnMethod(MethodMetadata data, Annotation methodAnnotation, Method method) {
    if (!RequestMapping.class.isInstance(methodAnnotation) && !methodAnnotation
            .annotationType().isAnnotationPresent(RequestMapping.class)) {
        return;
    }
    RequestMapping methodMapping = findMergedAnnotation(method, RequestMapping.class);
    // 处理HTTP Method
    RequestMethod[] methods = methodMapping.method();
    //默认的method是GET
    if (methods.length == 0) {
        methods = new RequestMethod[] { RequestMethod.GET };
    }
    data.template().method(methods[0].name());
    // 处理请求的路径
    checkAtMostOne(method, methodMapping.value(), "value");
    if (methodMapping.value().length 〉 0) {
        String pathValue = emptyToNull(methodMapping.value()[0]);
        if (pathValue != null) {
        pathValue = resolve(pathValue);
        // Append path from @RequestMapping if value is present on method
        if (!pathValue.startsWith("/")
                &amp;&amp; !data.template().toString().endsWith("/")) {
            pathValue = "/" + pathValue;
        }
        data.template().append(pathValue);
    }
}
// 处理生产
parseProduces(data, method, methodMapping);
// 处理消费
parseConsumes(data, method, methodMapping);
// 处理头部
parseHeaders(data, method, methodMapping);
    data.indexToExpander(new LinkedHashMap〈Integer, Param.Expander〉());
}
```

而processAnnotationsOnParameter方法则主要处理修饰函数参数的注解。它会根据注解类型来调用不同的AnnotatedParameterProcessor的实现类，解析注解的属性信息。函数参数的注解类型包括@RequestParam、@RequestHeader和@PathVariable。processAnnotationsOnParameter方法的具体实现如下代码所示：

```java
//SpringMvcContract.java
protected boolean processAnnotationsOnParameter(MethodMetadata data,
        Annotation[] annotations, int paramIndex) {
    boolean isHttpAnnotation = false;
    AnnotatedParameterProcessor.AnnotatedParameterContext context = new SimpleAnnotatedParameterContext(
            data, paramIndex);
    Method method = this.processedMethods.get(data.configKey());
    //遍历所有的参数注解
    for (Annotation parameterAnnotation : annotations) {
        //不同的注解类型有不同的Processor
        AnnotatedParameterProcessor processor = this.annotatedArgumentProcessors
                .get(parameterAnnotation.annotationType());
        if (processor != null) {
            Annotation processParameterAnnotation;
            //如果没有缓存的Processor，则生成一个
            processParameterAnnotation = synthesizeWithMethodParameterNameAsFallbackValue(
                    parameterAnnotation, method, paramIndex);
            isHttpAnnotation |= processor.processArgument(context,
                    processParameterAnnotation, method);
        }
    }
    return isHttpAnnotation;
}
```

AnnotatedParameterProcessor是一个接口，有三个实现类：PathVariableParameterProcessor、RequestHeaderParameterProcessor和RequestParamParameterProcessor，三者分别用于处理@RequestParam、@RequestHeader和@PathVariable注解。三者的类图如图5-5所示，我们具体看一下PathVariableParameterProcessor的实现。

```java
//PathVariableParameterProcessor.java
public boolean processArgument(AnnotatedParameterContext context, Annotation annotation, Method method) {
    //ANNOTATION就是@PathVariable,所以就获取它的值，也就是@RequestMapping value中{}内的值
    String name = ANNOTATION.cast(annotation).value();
    //将name设置为ParameterName
    context.setParameterName(name);
    MethodMetadata data = context.getMethodMetadata();
    //当varName在url、queries、headers中不存在时，将name添加到formParams中。因为无法找到
      对应的值
    String varName = '{' + name + '}';
    if (!data.template().url().contains(varName)
            &amp;&amp; !searchMapValues(data.template().queries(), varName)
            &amp;&amp; !searchMapValues(data.template().headers(), varName)) {
        data.formParams().add(name);
    }
    return true;
}
```

![image-20210203182928897](../../../../assets/image-20210203182928897.png)

如上述代码所示，PathVariableParameterProcessor的processArgument方法用于处理被@PathVariable注解修饰的参数。
ParseHandlersByName的apply方法通过Contract的parseAndValidatateMetadata方法获得了接口类中所有方法的元数据，这些信息中包含了每个方法所对应的网络请求信息。比如说请求的路径(path)、参数(params)、头部(headers)和body。接下来apply方法会为每个方法生成一个MethodHandler。SynchronousMethodHandler.Factory的create方法能直接创建SynchronousMethodHandler对象并返回，如下所示：

```java
//SynchronousMethodHandler.Factory
public MethodHandler create(Target〈?〉 target, MethodMetadata md,
    RequestTemplate.Factory buildTemplateFromArgs,,
    Options options, Decoder decoder, ErrorDecoder errorDecoder) {
    return new SynchronousMethodHandler(target, client, retryer, requestInterceptors, logger, logLevel, md, buildTemplateFromArgs,
                        options, decoder, errorDecoder, decode404);
}
```

ParseHandlersByName的apply方法作为ReflectiveFeign的newInstance方法的第一部分，其作用就是解析对应接口类的所有方法信息，并生成对应的MethodHandler。