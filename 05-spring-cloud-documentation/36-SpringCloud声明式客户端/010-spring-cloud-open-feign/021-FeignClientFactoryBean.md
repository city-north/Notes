## **FeignClientFactoryBean**

FeignClientFactoryBean是工厂类，Spring容器通过调用它的getObject方法来获取对应的Bean实例。被@FeignClient修饰的接口类都是通过FeignClientFactoryBean的getObject方法来进行实例化的，具体实现如下代码所示：

```java
//FeignClientFactoryBean.java
public Object getObject() throws Exception {
    FeignContext context = applicationContext.getBean(FeignContext.class);
    Feign.Builder builder = feign(context);
    if (StringUtils.hasText(this.url) &amp;&amp; !this.url.startsWith("http")) {
        this.url = "http://" + this.url;
    }
    String url = this.url + cleanPath();
    //调用FeignContext的getInstance方法获取Client对象
    Client client = getOptional(context, Client.class);
    //因为有具体的Url,所以就不需要负载均衡，所以除去LoadBalancerFeignClient实例
    if (client != null) {
        if (client instanceof LoadBalancerFeignClient) {
            client = ((LoadBalancerFeignClient)client).getDelegate();
        }
        builder.client(client);
    }
    Targeter targeter = get(context, Targeter.class);
    return targeter.target(this, builder, context, new HardCodedTarget〈〉(
            this.type, this.name, url));
}
```

这里就用到了FeignContext的getInstance方法，我们在前边已经讲解了FeignContext的作用，getOptional方法调用了FeignContext的getInstance方法，从FeignContext的对应名称的子上下文中获取到Client类型的Bean实例，其具体实现如下所示：

```java
//NamedContextFactory.java
public 〈T〉 T getInstance(String name, Class〈T〉 type) {
    AnnotationConfigApplicationContext context = getContext(name);
    if (BeanFactoryUtils.beanNamesForTypeIncludingAncestors(context,
            type).length 〉 0) {
        //从对应的context中获取Bean实例,如果对应的子上下文没有则直接从父上下文中获取
        return context.getBean(type);
    }
    return null;
}
```

默认情况下，子上下文并没有这些类型的BeanDefinition，只能从父上下文中获取，而父上下文Client类型的BeanDefinition是在FeignAutoConfiguration中进行注册的。但是当子上下文注册的配置类提供了Client实例时，子上下文会直接将自己配置类的Client实例进行返回，否则都是由父上下文返回默认Client实例。Client在FeignAutoConfiguration中的配置如下所示。

```java
//FeignAutoConfiguration.java
@Bean
@ConditionalOnMissingBean(Client.class)
public Client feignClient(HttpClient httpClient) {
    return new ApacheHttpClient(httpClient);
}
```

Targeter是一个接口，它的target方法会生成对应的实例对象。它有两个实现类，分别为DefaultTargeter和HystrixTargeter。OpenFeign使用HystrixTargeter这一层抽象来封装关于Hystrix的实现。DefaultTargeter的实现如下所示，只是调用了Feign.Builder的target方法：

```java
//DefaultTargeter.java
class DefaultTargeter implements Targeter {
@Override
    public 〈T〉 T target(FeignClientFactoryBean factory, Feign.Builder feign, FeignContext context,
                        Target.HardCodedTarget〈T〉 target) {
        return feign.target(target);
    }
}
```

而Feign.Builder是由FeignClientFactoryBean对象的feign方法创建的。Feign.Builder会设置FeignLoggerFactory、EncoderDecoder和Contract等组件，这些组件的Bean实例都是通过FeignContext获取的，也就是说这些实例都是可配置的，你可以通过OpenFeign的配置机制为不同的FeignClient配置不同的组件实例。feign方法的实现如下所示：

```java
//FeignClientFactoryBean.java
protected Feign.Builder feign(FeignContext context) {
    FeignLoggerFactory loggerFactory = get(context, FeignLoggerFactory.class);
    Logger logger = loggerFactory.create(this.type);
    Feign.Builder builder = get(context, Feign.Builder.class)
        .logger(logger)
        .encoder(get(context, Encoder.class))
        .decoder(get(context, Decoder.class))
        .contract(get(context, Contract.class));
    configureFeign(context, builder);
    return builder;
}
```

Feign.Builder负责生成被@FeignClient修饰的FeignClient接口类实例。它通过Java反射机制，构造InvocationHandler实例并将其注册到FeignClient上，当FeignClient的方法被调用时，InvocationHandler的回调函数会被调用，OpenFeign会在其回调函数中发送网络请求。build方法如下所示：

```java
//Feign.Builder
public Feign build() {
    SynchronousMethodHandler.Factory synchronousMethodHandlerFactory =
        new SynchronousMethodHandler.Factory(client, retryer, requestInterceptors,
        logger, logLevel, decode404);
    ParseHandlersByName handlersByName = new ParseHandlersByName(contract, options, encoder, decoder, errorDecoder, synchronousMethodHandlerFactory);
    return new ReflectiveFeign(handlersByName, invocationHandlerFactory);
}
```

ReflectiveFeign的newInstance方法是生成FeignClient实例的关键实现。它主要做了两件事情，一是扫描FeignClient接口类的所有函数，生成对应的Handler；二是使用Proxy生成FeignClient的实例对象，代码如下所示：

```java

//ReflectiveFeign.java
public 〈T〉 T newInstance(Target〈T〉 target) {
    Map〈String, MethodHandler〉 nameToHandler = targetToHandlersByName.apply(target);
    Map〈Method, MethodHandler〉 methodToHandler = new LinkedHashMap〈Method, MethodHandler〉();
    List〈DefaultMethodHandler〉 defaultMethodHandlers = new LinkedList〈DefaultMethodHandler〉();
    for (Method method : target.type().getMethods()) {
        if (method.getDeclaringClass() == Object.class) {
            continue;
        } else if(Util.isDefault(method)) {
            //为每个默认方法生成一个DefaultMethodHandler
            defaultMethodHandler handler = new DefaultMethodHandler(method);
            defaultMethodHandlers.add(handler);
            methodToHandler.put(method, handler);
        } else {
            methodToHandler.put(method, nameToHandler.get(Feign.configKey(target.type(), method)));
        }
    }
    //生成java reflective的InvocationHandler
  
  
  InvocationHandler handler = factory.create(target, methodToHandler);
    T proxy = (T) Proxy.newProxyInstance(target.type().getClassLoader(), new Class〈?〉[] {target.type()}, handler);
    //将defaultMethodHandler绑定到proxy中
    for(DefaultMethodHandler defaultMethodHandler : defaultMethodHandlers) {
        defaultMethodHandler.bindTo(proxy);
    }
    return proxy;
}

```

## 扫描函数信息

在扫描FeignClient接口类所有函数生成对应Handler的过程中，OpenFeign会生成调用该函数时发送网络请求的模板，也就是RequestTemplate实例。RequestTemplate中包含了发送网络请求的URL和函数参数填充的信息。@RequestMapping、@PathVariable等注解信息也会包含到RequestTemplate中，用于函数参数的填充。ParseHandlersByName类的apply方法就是这一过程的具体实现。它首先会使用Contract来解析接口类中的函数信息，并检查函数的合法性，然后根据函数的不同类型来为每个函数生成一个BuildTemplateByResolvingArgs对象，最后使用SynchronousMethodHandler.Factory来创建MethodHandler实例。ParseHandlersByName的apply实现如下代码所示：

```java
//ParseHandlersByName.java
public Map〈String, MethodHandler〉 apply(Target key) {
    // 获取type的所有方法的信息,会根据注解生成每个方法的RequestTemplate
    List〈MethodMetadata〉 metadata = contract.parseAndValidatateMetadata(key.type());
    Map〈String, MethodHandler〉 result = new LinkedHashMap〈String, MethodHandler〉();
    for (MethodMetadata md : metadata) {
    BuildTemplateByResolvingArgs buildTemplate;
    if (!md.formParams().isEmpty() &amp;&amp; md.template().bodyTemplate() == null) {
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



OpenFeign默认的Contract实现是SpringMvcContract。SpringMvcContract的父类为BaseContract，而BaseContract是Contract众多子类中的一员，其他还有JAXRSContract和HystrixDelegatingContract等。Contract的parseAndValidateMetadata方法会解析与HTTP请求相关的所有函数的基本信息和注解信息，代码如下所示：

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

BaseContract的parseAndValidateMetadata方法会依次解析接口类的注解，函数注解和函数的参数注解，将这些注解包含的信息封装到MethodMetadata对象中，然后返回，代码如下所示

```java
//BaseContract.java
protected MethodMetadata parseAndValidateMetadata(Class〈?〉 targetType, Method method) {
    MethodMetadata data = new MethodMetadata();
    //函数的返回值
    data.returnType(Types.resolve(targetType, targetType, method.getGenericReturnType()));
  ()));
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
protected void processAnnotationOnMethod(MethodMetadata data,,
        Annotation methodAnnotation, Method method) {
    if (!RequestMapping.class.isInstance(methodAnnotation) &amp;&amp; !methodAnnotation
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

![image-20201011142410910](../../../assets/image-20201011142410910.png)

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
2.生成Proxy接口类

```java
//ReflectiveFeign.java
//生成Java反射的InvocationHandler
InvocationHandler handler = factory.create(target, methodToHandler);
T proxy = (T) Proxy.newProxyInstance(target.type().getClassLoader(), new Class〈?〉[] {target.type()}, handler);
//将defaultMethodHandler绑定到proxy中。
for(DefaultMethodHandler defaultMethodHandler : defaultMethodHandlers) {
    defaultMethodHandler.bindTo(proxy);
}
return proxy;
```



OpenFeign使用Proxy的newProxyInstance方法来创建FeignClient接口类的实例，然后将InvocationHandler绑定到接口类实例上，用于处理接口类函数调用，如下所示：

```java
//Default.java
static final class Default implements InvocationHandlerFactory {
    @Override
    public InvocationHandler create(Target target, Map〈Method, MethodHandler〉 dispatch) {
        return new ReflectiveFeign.FeignInvocationHandler(target, dispatch);
    }
}
```

Default实现了InvocationHandlerFactory接口，其create方法返回ReflectiveFeign.FeignInvocationHandler实例。
ReflectiveFeign的内部类FeignInvocationHandler是InvocationHandler的实现类，其主要作用是将接口类相关函数的调用分配给对应的MethodToHandler实例，即SynchronousMethodHandler来处理。当调用接口类实例的函数时，会直接调用到FeignInvocationHandler的invoke方法。invoke方法会根据函数名称来调用不同的MethodHandler实例的invoke方法，如下所示：

```java
//FeignInvocationHandler.java
public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    if ("equals".equals(method.getName())) {
        try {
            Object
                otherHandler =
                args.length 〉 0 && args[0] != null ? Proxy.getInvocationHandler(args[0]): null;
            return equals(otherHandler);
        } catch (IllegalArgumentException e) {
            return false;
        }
    } else if ("hashCode".equals(method.getName())) {
        return hashCode();
    } else if ("toString".equals(method.getName())) {
        return toString();
    }
    //dispatch就是Map〈Method, MethodHandler〉，所以就是将某个函数的调用交给对应的MethodHandler
      来处理
    return dispatch.get(method).invoke(args);
}
```

