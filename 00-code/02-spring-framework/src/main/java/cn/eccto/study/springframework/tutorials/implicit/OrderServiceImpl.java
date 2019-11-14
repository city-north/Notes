package cn.eccto.study.springframework.tutorials.implicit;

import org.springframework.stereotype.Service;

/**
 * description
 *
 * @author qiang.chen04@hand-china.com 2019/11/14 17:14
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Override
    public String getOrderDetails(String orderId) {
        return "Order details from impl 1, for order id=" + orderId;
    }
}
