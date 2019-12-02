# Client Size Load Balancer:Ribbon

客户端负载均衡器:Ribbon

> Ribbon is a client-side load balancer that gives you a lot of control over the behavior of HTTP and TCP clients. Feign already uses Ribbon, so, if you use `@FeignClient`, this section also applies.
>
> A central concept in Ribbon is that of the named client. Each load balancer is part of an ensemble of components that work together to contact a remote server on demand, and the ensemble has a name that you give it as an application developer (for example, by using the `@FeignClient` annotation). On demand, Spring Cloud creates a new ensemble as an `ApplicationContext` for each named client by using `RibbonClientConfiguration`. This contains (amongst other things) an `ILoadBalancer`, a `RestClient`, and a `ServerListFilter`.

- Ribbon 是客户端负载均衡器,提供了对HTTP和TCP客户机行为的大量控制。
- Feign 使用 Rebbon ,当你使用`@FeignClient`时
- Ribbon 中的一个核心概念是客户端,每个负载均衡器根据需要联系服务端
- 