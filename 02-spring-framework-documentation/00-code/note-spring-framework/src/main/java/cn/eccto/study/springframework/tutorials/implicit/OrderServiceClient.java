package cn.eccto.study.springframework.tutorials.implicit;

import org.springframework.stereotype.Component;

/**
 * description
 *
 * @author JonathanChen 2019/11/14 20:13
 */
@Component
public class OrderServiceClient {

    private OrderService orderService;

    //@Autowired is no longer required in Spring 4.3 and later.
    public OrderServiceClient(OrderService orderService) {
        this.orderService = orderService;
    }

    public void showPendingOrderDetails() {
        System.out.println(orderService.getOrderDetails("100"));
    }
}