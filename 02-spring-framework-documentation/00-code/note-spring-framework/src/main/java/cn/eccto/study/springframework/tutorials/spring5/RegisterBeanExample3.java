package cn.eccto.study.springframework.tutorials.spring5;

import org.springframework.context.support.GenericApplicationContext;

/**
 * description
 *
 * @author JonathanChen 2019/11/27 18:09
 */
public class RegisterBeanExample3 {
    //using registerBean(beanName, beanClass, customizers)
    public static void main(String[] args) {
        GenericApplicationContext gac = new GenericApplicationContext();
        gac.registerBean("orderBean", OrderService.OrderServiceImpl.class, Customizers::lazy);
        gac.refresh();
        System.out.println("context refreshed");
        OrderService os = (OrderService) gac.getBean("orderBean");
        os.placeOrder("Laptop", 2);
        gac.close();
    }
}
