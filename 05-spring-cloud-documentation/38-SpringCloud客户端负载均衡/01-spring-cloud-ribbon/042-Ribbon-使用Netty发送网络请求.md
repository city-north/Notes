# 042-Ribbon-使用Netty发送网络请求

Ribbon除了可以和RestTemplate、OpenFeign一起使用之外，还可以与Netty进行集成，也就是说，Ribbon使用负载均衡策略选择完服务器之后，再交给Netty处理网络请求。其实，上一小节介绍的Ribbon的LoadBalancerCommand的submit方法可以直接使用Netty框架，也就是在ServerOperation的call方法中使用Netty代替HttpURLConnection来发送网络请求。但是，Ribbon已经封装好了与Netty进行集成的相关实现。RibbonTransport封装了生成LoadBalancingHttpClient〈ByteBuf，ByteBuf〉对象的各类工厂函数。

使用LoadBalancingHttpClient〈ByteBuf，ByteBuf〉的submit方法发送网络请求，底层默认使用Netty框架来进行网络请求(其中ByteBuf是Netty框架内的字符串缓存实例)，代码如下所

```java
public class SimpleGet {
    @edu.umd.cs.findbugs.annotations.SuppressWarnings
    public static void main(String[] args) throws Exception {
        LoadBalancingHttpClient〈ByteBuf, ByteBuf〉 client = RibbonTransport.newHttpClient();
        //HttpClientRequest.createGet接口可以直接生成对应的请求
        HttpClientRequest〈ByteBuf〉 request = HttpClientRequest.createGet("http://www.google.com/");
        final CountDownLatch latch = new CountDownLatch(1);
        client.submit(request)
            .toBlocking()
            .forEach(new Action1〈HttpClientResponse〈ByteBuf〉〉() {
                @Override
                public void call(HttpClientResponse〈ByteBuf〉 t1) {
                    System.out.println("Status code: " + t1.getStatus());
                    t1.getContent().subscribe(new Action1〈ByteBuf〉() {
                        @Override
                        public void call(ByteBuf content) {
                            //可以直接对Netty的ByteBuf进行操作
                            System.out.println("Response content: " + content.toString(Charset.defaultCharset()));
                            latch.countDown();
                        }
                    });
                }
            });
        latch.await(2, TimeUnit.SECONDS);
    }
}
```

