package cn.eccto.study.springframework.tutorials.spring5;

import org.springframework.context.support.GenericApplicationContext;

/**
 * description
 *
 * @author JonathanChen 2019/11/27 18:09
 */
public class RegisterBeanExample2 {
    //using registerBean(beanClass, customizers)
    public static void main(String[] args) {
        GenericApplicationContext gac = new GenericApplicationContext();
        gac.registerBean(OrderService.OrderServiceImpl.class, Customizers::prototypeScoped);
        gac.refresh();
        System.out.println("context refreshed");
        OrderService os = gac.getBean(OrderService.class);
        os.placeOrder("Laptop", 2);
        System.out.println("-----------");
        //retrieving the bean one more time
        os = gac.getBean(OrderService.class);
        os.placeOrder("Desktop", 3);
        gac.close();
    }
}