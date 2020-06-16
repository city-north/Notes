package gateway;

import gateway.filter.ratelimiter.GatewayRateLimitFilterByIp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/06/12 12:21
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }


//    @Bean
//    public RouteLocator afterRouteLocator(RouteLocatorBuilder builder) {
//        //生成比当前时间早一个小时的UTC时间
//        ZonedDateTime minusTime = LocalDateTime.now().minusHours(1).atZone(ZoneId.systemDefault());
//        return builder.routes()
//                .route("after_route", r -> r.after(minusTime)
//                        .uri("http://baidu.com"))
//                .build();
//    }

//    /**
//     * before 断言
//     */
//    @Bean
//    public RouteLocator beforeRouteLocator(RouteLocatorBuilder builder) {
//
//        ZonedDateTime datetime = LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault());
//        return builder.routes()
//                .route("before_route", r -> r.before(datetime)
//                        .uri("http://baidu.com"))
//
//                .build();
//    }
//
//    @Bean
//    public RouteLocator betweenRouteLocator(RouteLocatorBuilder builder) {
//
//        ZonedDateTime datetime1 = LocalDateTime.now().minusDays(1).atZone(ZoneId.systemDefault());
//        ZonedDateTime datetime2 = LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault());
//        return builder.routes()
//                .route("between_route", r -> r.between(datetime1, datetime2)
//                        .uri("http://baidu.com"))
//
//                .build();
//    }

    @Bean
    public RouteLocator customerRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/test/rateLimit")
                        .filters(f -> f.filter(new GatewayRateLimitFilterByIp(10, 1, Duration.ofSeconds(1))))
                        .uri("http://baidu.com")
                        .id("rateLimit_route")
                ).build();
    }
}
