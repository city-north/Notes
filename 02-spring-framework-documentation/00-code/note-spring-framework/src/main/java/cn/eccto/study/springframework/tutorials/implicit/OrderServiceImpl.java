package cn.eccto.study.springframework.tutorials.implicit;

import org.springframework.stereotype.Service;

/**
 * description
 *
 * @author JonathanChen 2019/11/14 20:14
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Override
    public String getOrderDetails(String orderId) {
        return "Order details from impl 1, for order id=" + orderId;
    }
}
