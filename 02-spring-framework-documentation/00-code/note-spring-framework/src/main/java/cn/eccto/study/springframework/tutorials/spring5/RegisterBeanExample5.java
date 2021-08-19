package cn.eccto.study.springframework.tutorials.spring5;

import org.springframework.context.support.GenericApplicationContext;

/**
 * description
 *
 * @author JonathanChen 2019/11/27 18:10
 */
public class RegisterBeanExample5 {
    //injecting other bean via constructor
    public static void main(String[] args) {
        GenericApplicationContext gac = new GenericApplicationContext();
        gac.registerBean(LogService.class, LogService.LogServiceImpl::new, Customizers::lazy, Customizers::defaultInitMethod);
        gac.registerBean(OrderService.OrderServiceImpl.class, Customizers::defaultInitMethod, Customizers::defaultDestroyMethod);
        gac.refresh();
        System.out.println("context refreshed");
        OrderService os = gac.getBean(OrderService.class);
        os.placeOrder("Laptop", 2);
        gac.close();
    }
}